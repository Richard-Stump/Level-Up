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
import com.mygdx.nextlevel.screens.editor.*;

import java.util.ArrayList;
import java.util.HashMap;

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
     * @param mapWidth How many tiles wide the new level should be
     * @param mapHeight How many tiles tall the new level should be
     */
    public EditLevelScreen(NextLevel game, int mapWidth, int mapHeight) {
        this.game = game;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera(screenWidth, screenHeight);

        this.atlas = new TextureAtlas(Gdx.files.internal("skin/neon-ui.atlas"));
        this.skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

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
        levelView = new LevelView(this, new EditorLevel(mapWidth, mapHeight), STAGE_WIDTH, STAGE_HEIGHT);

        // For some reason the tabbed pane won't work with the other skin.
        // TODO: Figure out how to use the same skin as the main menu
        if(!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);
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
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game, MainMenuScreen.username));
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
        MenuWindow win2 = new MenuWindow();
        stage.addActor(win2);

        backButton.setPosition(0.0f, STAGE_HEIGHT - backButton.getHeight());
        stage.addActor(backButton);
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
}

/**
 * The window that handles load/save etc...
 */
class MenuWindow extends VisWindow {
    Button addAssetButton;
    Button addFinishFlagButton;
    Button levelSettingsButton;
    Button levelInfoButton;
    Button testLevelButton;
    Button saveAndExitButton;

    final float BUTTON_WIDTH = 160.0f;
    final float BUTTON_PADDING = 10.0f;

    public MenuWindow() {
        super("Menu:");

        VisTable table = new VisTable();

        constructButtons(table);

        add(table).fill();

        int numButtons = getButtonList().size();

        float width = 50 + BUTTON_WIDTH * numButtons + BUTTON_PADDING * numButtons;
        float x = EditLevelScreen.STAGE_WIDTH - width;
        float y = EditLevelScreen.STAGE_HEIGHT;

        setSize(width, 150);
        setPosition(x, y);
    }

    private void constructButtons(Table table) {
        ArrayList<Button> buttons = getButtonList();

        // List of titles for the buttons. There should be one here for each button
        String[] buttonTitles = {
            "Add\nAssets",
            "Add Finish\nFlag",
            "Level\nSettings",
            "Edit Level\nInfo",
            "Test\nLevel",
            "Save and\nExit",
        };

        for(int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);

            b = new TextButton(buttonTitles[i], VisUI.getSkin());
            table.add(b).expand().fill().pad(BUTTON_PADDING).width(BUTTON_WIDTH);
        }
    }

    private ArrayList<Button> getButtonList() {
        ArrayList<Button> l = new ArrayList<Button>();

        l.add(addAssetButton);
        l.add(addFinishFlagButton);
        l.add(levelSettingsButton);
        l.add(levelInfoButton);
        l.add(saveAndExitButton);
        l.add(testLevelButton);

        return l;
    }
}