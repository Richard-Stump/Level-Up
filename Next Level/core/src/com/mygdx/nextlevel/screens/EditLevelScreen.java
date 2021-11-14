package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.screens.editor.*;
//import jdk.internal.org.jline.reader.Editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;

/* TODO: Make it so that the object TabbedPane always aligns to the left.
 */

/**
 * Screen for the level editor
 *
 * This is the screen that the user is taken to when they want to edit a level.
 */
public class EditLevelScreen implements Screen {
    private NextLevel game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Skin skin;
    private Stage stage;
    private Viewport viewport;
    private int screenWidth, screenHeight;
    private EditorLevel level;

    private LevelView levelView;
    private AssetSelectorWindow win;

    private ArrayList<Texture> tiles;
    private ArrayList<Texture> actorTextures;

    private final Color backgroundColor = new Color(0.1f, 0.1f, 0.1f,1.0f);

    public static final int STAGE_WIDTH = 1920;
    public static final int STAGE_HEIGHT = 1000;


    /**
     * Initializes the level editor with an empty level of size 32x32
     *
     * @param game A reference to the NextLevel class in use
     */
    public EditLevelScreen(NextLevel game) {
        this(game, 32, 32);
    }

    /**
     * Initializes the level editor with an empty level of the given size
     *
     * @param game A reference to the NextLevel class in use
     * @param level The EditorLevel to edit.
     */
    public EditLevelScreen(NextLevel game, EditorLevel level) {
        this.game = game;
        this.level = level;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera(screenWidth, screenHeight);

        this.atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        this.tiles = new ArrayList<Texture>();

        // Create a new orthographic camera and set it to view the center of the screen.
        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        //Set up the viewport
        viewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT, camera);
        viewport.apply();

        batch = game.batch;
        stage = new Stage(viewport, batch);

        loadTiles();
        loadActors();
        levelView = new LevelView(this, level, STAGE_WIDTH, STAGE_HEIGHT);

        // For some reason the tabbed pane won't work with the other skin.
        // TODO: Figure out how to use the same skin as the main menu
        if(!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);
    }

    public EditLevelScreen(NextLevel game, int mapWidth, int mapHeight) {
       this(game, new EditorLevel(mapWidth, mapHeight));
    }

    /**
     * Called by LibGDX when the screen is shown
     */
    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        //back button
        TextButton backButton = new TextButton("Back", VisUI.getSkin());

        //back button listener
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(levelView);
        levelView.addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                stage.setScrollFocus(levelView);
            }
        });

        win = new AssetSelectorWindow(tiles, actorTextures);
        stage.addActor(win);
        MenuWindow win2 = new MenuWindow(level, stage);
        stage.addActor(win2);

        backButton.setPosition(0.0f, STAGE_HEIGHT - backButton.getHeight());
        stage.addActor(backButton);

        final EditLevelScreen editLevelScreen = this;

        win2.addLevelSettingsListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(new LevelSettingsWindow(editLevelScreen, level));
            }
        });
    }

    /**
     * Called by libGDX each frame.
     * @param delta The change in time since the last frame
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(backgroundColor);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override public void pause () {
    }


    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }

    /**
     * Loads the list of tiles into a list for the level editor
     */
    public void loadTiles() {
        tiles = new ArrayList<Texture>();

        ArrayList<String> tileNames = new ArrayList<String>() {{
            add("block.png");
            add("checkpoint.png");
            add("flag.png");
            add("item-block.png");
            add("pipe.png");
            add("tile1.png");
            add("tile2.png");
            add("used-item-block.png");
        }};

        for(String name : tileNames) {
            tiles.add(new Texture(name));
        }
    }

    /**
     * Loads the list of actors for use in the level editor
     */
    public void loadActors() {
        ArrayList<String> actorNames = new ArrayList<String>() {{
            add("badlogic.jpg");
            add("enemy.jpg");
            add("goomba.png");
            add("mushroom.jpeg");
            add("goomba.png");
            add("paragoomba.png");
        }};

        actorTextures = new ArrayList<Texture>();

        for(String name : actorNames) {
            actorTextures.add(new Texture(name));
        }
    }

    public ArrayList<Texture> getTiles() {
        return tiles;
    }

    public ArrayList<Texture> getActorTextures() {
        return actorTextures;
    }

    public AssetSelectorWindow getSelectorWindow() {
        return win;
    }

    public void setScrollFocus(Actor actor) {
        stage.setScrollFocus(actor);
    }

    public Stage getStage() { return stage; }
}

/**
 * The window that handles load/save etc...
 */
class MenuWindow extends VisWindow {
    Button levelSettingsButton;
    Button saveButton;
    Button loadButton;

    EditorLevel level;

    final float BUTTON_WIDTH = 160.0f;
    final float BUTTON_PADDING = 10.0f;

    public MenuWindow(final EditorLevel level, final Stage stage) {
        super("Menu:");

        this.level = level;

        VisTable table = new VisTable();

        saveButton = new TextButton("Save", VisUI.getSkin());
        table.add(saveButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();
        loadButton = new TextButton("Load", VisUI.getSkin());
        table.add(loadButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();
        levelSettingsButton = new TextButton("Level\nSettings", VisUI.getSkin());
        table.add(levelSettingsButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING);

        final EditorLevel lev = level;

        final Stage finalStage = stage;

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(lev.saveName == null)
                    finalStage.addActor(new SaveAsDialog(level, stage));
                else {
                    try {
                        lev.exportTo(lev.saveName);
                    }
                    catch (FileNotFoundException e) {
                        stage.addActor(new MessageDialog("Could not open + \"" + lev.saveName + "\"to save the level"));
                    }
                }
            }
        });

        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(new LoadDialog(level, stage));
            }
        });

        add(table).fill();

        int numButtons = 3;
        float width = 50 + BUTTON_WIDTH * numButtons + BUTTON_PADDING * numButtons;
        float x = EditLevelScreen.STAGE_WIDTH - width;
        float y = EditLevelScreen.STAGE_HEIGHT;

        /*
        levelInfoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //((Game)Gdx.app.getApplicationListener()).setScreen(new EditLevelInfoMenuScreen(game));
            }
        });


         */
        setSize(width, 150);
        setPosition(x, y);
    }

    void addLevelSettingsListener(ClickListener listener) {
        levelSettingsButton.addListener(listener);
    }
}

class LevelSettingsWindow extends VisWindow{
    protected EditorLevel level;
    protected VisTextField nameField;
    protected VisTextField widthField;
    protected VisTextField heightField;
    protected VisTextField gravityField;
    protected SelectBox<Difficulty> difficultySelectBox;
    protected VisTextButton applyButton;
    protected VisTextButton cancelButton;
    protected VisCheckBox collectCoinsCheck;
    protected VisCheckBox beatTimeLimitCheck;
    protected VisCheckBox killAllEnemiesCheck;
    protected VisCheckBox killNoEnemiesCheck;
    protected VisCheckBox keepJewelCheck;

    protected Actor previousScrollFocus;

    public LevelSettingsWindow(EditLevelScreen screen, final EditorLevel level) {
        super("Level Settings");

        screen.setScrollFocus(this);

        this.level = level;
        this.centerWindow();
        this.setModal(true);
        setMovable(false);

        nameField = new VisTextField(level.name);
        widthField = new VisTextField(Integer.toString(level.width));
        widthField.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
        heightField = new VisTextField(Integer.toString(level.height));
        heightField.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
        gravityField = new VisTextField(Float.toString(level.gravity));
        gravityField.setTextFieldFilter(new VisTextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(VisTextField textField, char c) {
                if(c == '-' && textField.getCursorPosition() == 0 && !textField.getText().contains("-"))
                    return true;
                else if(c == '.' && !textField.getText().contains("."))
                    return true;
                else if (Character.isDigit(c))
                    return true;

                return false;
            }
        });
        difficultySelectBox = new SelectBox<Difficulty>(VisUI.getSkin());
        difficultySelectBox.setItems(Difficulty.values());
        difficultySelectBox.setSelected(level.difficulty);

        final Stage stage = screen.getStage();
        previousScrollFocus = stage.getScrollFocus();

        applyButton = new VisTextButton("Apply");
        applyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                String name = nameField.getText();

                level.name = name;
                level.difficulty = difficultySelectBox.getSelected();
                level.resize(width, height);

                level.collectCoins = collectCoinsCheck.isChecked();
                level.beatTimeLimit = beatTimeLimitCheck.isChecked();
                level.killAllEnemies = killAllEnemiesCheck.isChecked();
                level.killNoEnemies = killNoEnemiesCheck.isChecked();
                level.keepJewel = keepJewelCheck.isChecked();

                level.gravity = Float.parseFloat(gravityField.getText());

                close();
                stage.setScrollFocus(previousScrollFocus);
            }
        });

        cancelButton = new VisTextButton("Cancel");
        cancelButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                close();
                stage.setScrollFocus(previousScrollFocus);
            }
        });

        collectCoinsCheck = new VisCheckBox("Collect All Coins", level.collectCoins);
        beatTimeLimitCheck = new VisCheckBox("Beat Time Limit", level.beatTimeLimit);
        killAllEnemiesCheck = new VisCheckBox("Kill All Enemies", level.killAllEnemies);
        killNoEnemiesCheck = new VisCheckBox("Don't Kill Any Enemies", level.killNoEnemies);
        keepJewelCheck = new VisCheckBox("Keep The Sacred Jewel", level.keepJewel);

        setupTable();
        padTop(40.0f);
        pack();
    }

    protected void setupTable() {
        VisTable contentTable = new VisTable();

        contentTable.pad(2.0f);
        contentTable.top();
        contentTable.add("Level Properties: ").colspan(2);

        contentTable.row();
        contentTable.add(new VisLabel("Name:    ")).left();
        contentTable.add(nameField).expandX().fillX();

        contentTable.row();
        contentTable.add(new VisLabel("Width:    ")).left();
        contentTable.add(widthField).expandX().fillX();

        contentTable.row();
        contentTable.add(new VisLabel("Height:    ")).left();
        contentTable.add(heightField).expandX().fillX();

        contentTable.row();
        contentTable.add(new VisLabel("Gravity:    ")).left();
        contentTable.add(gravityField).expandX().fillX();

        contentTable.row();
        contentTable.add(new VisLabel("Difficulty: ")).left();
        contentTable.add(difficultySelectBox).expandX().fillX();

        contentTable.row();
        contentTable.add(new VisLabel("Level Completion Criteria:")).left().colspan(2);

        contentTable.row();
        contentTable.add(collectCoinsCheck).colspan(2).left().padLeft(10.0f);
        contentTable.row();
        contentTable.add(beatTimeLimitCheck).colspan(2).left().padLeft(10.0f);
        contentTable.row();
        contentTable.add(killAllEnemiesCheck).colspan(2).left().padLeft(10.0f);
        contentTable.row();
        contentTable.add(killNoEnemiesCheck).colspan(2).left().padLeft(10.0f);
        contentTable.row();
        contentTable.add(keepJewelCheck).colspan(2).left().padLeft(10.0f);

        this.add(contentTable).expand().fill().colspan(2).pad(10.0f);
        this.row();
        this.add(applyButton);
        this.add(cancelButton);
    }
}

class SaveAsDialog extends VisWindow {
    protected VisTextField nameField;
    protected VisTextButton confirmButton;
    protected VisTextButton cancelButton;

    public SaveAsDialog(EditorLevel level, final Stage stage) {
        super("Save Map As:");

        setModal(true);
        setMovable(false);
        centerWindow();

        top();
        nameField = new VisTextField("");
        add(nameField).colspan(2).expandX().fillX().pad(4.0f);
        row();
        confirmButton = new VisTextButton("Confirm");
        cancelButton = new VisTextButton("Cancel");
        add(confirmButton).pad(4.0f);
        add(cancelButton).pad(4.0f);
        pack();

        final EditorLevel finalLevel = level;

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = nameField.getText();

                if(!name.endsWith(".tmx"))
                    name = name + ".tmx";

                try {
                    finalLevel.exportTo(name);
                    finalLevel.saveName = name;
                } catch (FileNotFoundException e) {
                    stage.addActor(new MessageDialog("Could not open \"" + name + "\" to save file"));
                }

                close();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });
    }
}

class LoadDialog extends VisWindow {
    protected VisSelectBox<String> fileSelection;
    protected VisTextButton confirmButton;
    protected VisTextButton cancelButton;

    public LoadDialog(final EditorLevel level, final Stage stage) {
        super("Load From:");

        String[] names = getLevelList(stage);
        if(names != null) {
            fileSelection = new VisSelectBox<>();
            fileSelection.setItems(names);
            confirmButton = new VisTextButton("Confirm");
            cancelButton = new VisTextButton("Cancel");

            top();
            add(fileSelection).expandX().fillX().colspan(2).pad(4.0f);
            row();
            add(confirmButton).pad(4.0f);
            add(cancelButton).pad(4.0f);

            confirmButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String fileName = fileSelection.getSelected();

                    try {
                        level.importFrom(fileName);
                        level.saveName = fileName;
                    } catch (Exception e) {
                        stage.addActor(new MessageDialog("Could not open \"" + fileName + "\" for loading"));
                    }

                    close();
                }
            });

            cancelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    close();
                }
            });

            pack();
            centerWindow();
        }
    }

    protected String[] getLevelList(final Stage stage) {
        ArrayList<String> mapList = new ArrayList<>();

        try {
            File dir = new File("./");

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".tmx");
                }
            };

            return dir.list(filter);
        } catch (Exception e) {
            stage.addActor(new MessageDialog("Could not open "));
            close();
            return null;
        }
    }
}

class MessageDialog extends VisWindow {
    public MessageDialog(String str) {
        super("Warning:");

        VisTextButton button = new VisTextButton("Okay");
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        top();
        add(str).expandX().fillX().pad(4.0f);
        row();
        add(button).pad(4.0f);
    }
}