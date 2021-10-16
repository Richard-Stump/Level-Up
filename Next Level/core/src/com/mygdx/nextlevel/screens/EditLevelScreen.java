package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.*;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane.TabbedPaneStyle;
import com.mygdx.nextlevel.NextLevel;
//import com.kotcrab.*;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.ui.Cell.defaults;

/* TODO: Make it so that the object TabbedPane always aligns to the left.
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

    private final Color backgroundColor = new Color(0.1f, 0.1f, 0.1f,1.0f);
    private final Color gridColor = new Color(0.2f, 0.2f, 0.2f,1.0f);

    public static final int STAGE_WIDTH = 1920;
    public static final int STAGE_HEIGHT = 1000;

    public EditLevelScreen(NextLevel game) {
        this.game = game;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera(screenWidth, screenHeight);

        this.atlas = new TextureAtlas(Gdx.files.internal("skin/neon-ui.atlas"));
        this.skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);


        // Create a new orthographic camera and set it to view the center of the screen.
        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        //Set up the viewport
        viewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT, camera);
        viewport.apply();

        batch = game.batch;
        stage = new Stage(viewport, batch);

        // For some reason the tabbed pane won't work with the other skin.
        // TODO: Figure out how to use the same skin as the main menu
        if(!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        //back button
        TextButton backButton = new TextButton("Back", skin);

        //back button listener
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        final VisTable table = new VisTable();
        table.setFillParent(true);
        table.add(backButton).top().padLeft(300);

        table.add(new Label("Create Level", VisUI.getSkin())).center().expand();
        table.row();

        TabWindow win = new TabWindow();
        stage.addActor(win);
        MenuWindow win2 = new MenuWindow();
        stage.addActor(win2);

        stage.addActor(table);
    }

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

    @Override
    public void pause() {

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
}

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

// This window class draws and handles the tabbed pane which will let players select
// the various objects to place in their level.
// This is incomplete!
class TabWindow extends VisWindow {
    public TabWindow() {
        super("Level Objects:");
        TableUtils.setSpacingDefaults(this);

        setResizable(false);
        setMovable(false);

        // This table holds the contents of the tab we are currently looking at.
        // This needs to be updated whenever a different tab is selected because the
        // tab pane doesn't handle the content drawing for us.
        final VisTable table = new VisTable();

        TabbedPaneStyle style = VisUI.getSkin().get("default", TabbedPaneStyle.class);
        final TabbedPane pane = new TabbedPane(style);

        // listener to recreate the tab's content when a new tab is selected
        pane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab (Tab tab) {
                table.clearChildren();
                table.add(tab.getContentTable()).expandX().fillX();
            }
        });

        //The minHeight field ensures that the buttons don't get squashed with the scroll pane
        //increases in height.
        add(pane.getTable()).expandX().fillX().minHeight(50.0f);
        row();
        add(table).expand().fill();

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

        ArrayList<String> actorNames = new ArrayList<String>() {{
            add("badlogic.jpg");
            add("enemy.jpg");
            add("goomba.png");
            add("mushroom.jpeg");
        }};

        ArrayList<String> enemyNames = new ArrayList<String>() {{
            add("goomba.png");
            add("paragoomba.png");
        }};

        pane.add(new TestTab("Tiles", tileNames));
        pane.add(new TestTab("Actors", actorNames));
        pane.add(new TestTab("Enemies", enemyNames));

        setSize(400, 500 * 2 - 50);
        setPosition(0, -50);
    }
}

class TestTab extends Tab {
    private String name;
    private ScrollPane scrollPane;
    private VisTable table;

    public TestTab(String name, ArrayList<String> imageNames) {
        super(false, false);
        this.name = name;

        // This table holds the content the scroll pane will contain
        Table innerTable = new Table();
        innerTable.top();

        // We want the scroll pane to move smoothly, and we don't want the bar to disappear
        scrollPane = new ScrollPane(innerTable, VisUI.getSkin());
        scrollPane.setSmoothScrolling(true);
        scrollPane.setFadeScrollBars(false);

        final float size = 128;
        final float pad = 10.0f;

        // Add the images for each of the resources to the table.
        int i = 0;
        for(String imageName : imageNames) {
            Image im = new Image(new Texture(imageName));
            innerTable.add(im).expand().fill().padBottom(pad).padTop(pad).size(size);

            // A new row every 2 lines
            if(++i % 2 == 0)
                innerTable.row();
        }

        table = new VisTable();
        table.add(scrollPane).expand().fillX().align(Align.top);
    }

    @Override
    public String getTabTitle() {
        return name;
    }

    @Override
    public Table getContentTable() {
        return table;
    }
}