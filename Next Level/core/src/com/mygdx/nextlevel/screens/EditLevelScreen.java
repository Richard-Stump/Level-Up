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
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.screens.editor.*;
import org.reflections.Reflections;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

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
    private ObjectSelectionWindow win3;
    private PropertyWindow propWindow;

    private LevelView levelView;

    private ArrayList<PlaceableObject> placeableObjects;

    private final Color backgroundColor = new Color(0.1f, 0.1f, 0.1f,1.0f);

    public static final int STAGE_WIDTH = 1920;
    public static final int STAGE_HEIGHT = 1000;

    /**
     * Constructor called passing an already loaded level. This should be used for creating new levels
     *
     * @param game      The game that owns this screen
     * @param levelInfo The level info object for the passed level
     * @param level     The actual level data
     */
    public EditLevelScreen(NextLevel game, LevelInfo levelInfo, EditorLevel level) {
        this.game = game;
        this.level = level;
        level.info = levelInfo;

        placeableObjects = new ArrayList<>();
        loadPlaceableObjects();
        level.setPlaceableObjects(placeableObjects);

        initializeUi();
    }

    /**
     * Constructor to be called when the level is not already loaded into an object.
     *
     * @param game      The game that owns this screen
     * @param levelInfo The level info object to load the level data for
     */
    public EditLevelScreen(NextLevel game, LevelInfo levelInfo) {
        this.game = game;
        this.level = new EditorLevel(0, 0);
        placeableObjects = new ArrayList<>();
        loadPlaceableObjects();
        level.setPlaceableObjects(placeableObjects);
        level.importFrom(levelInfo.getId() + ".tmx");

        level.info = levelInfo;

        initializeUi();

    }

    protected void initializeUi() {
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera(screenWidth, screenHeight);

        this.atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);


        // Create a new orthographic camera and set it to view the center of the screen.
        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        //Set up the viewport
        viewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT, camera);
        viewport.apply();

        batch = game.batch;
        stage = new Stage(viewport, batch);

        levelView = new LevelView(this, level, STAGE_WIDTH, STAGE_HEIGHT);

        // For some reason the tabbed pane won't work with the other skin.
        // TODO: Figure out how to use the same skin as the main menu
        if(!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

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

        //property window needs to go first
        propWindow = new PropertyWindow();
        stage.addActor(propWindow);

        MenuWindow win2 = new MenuWindow(this, level, stage);
        stage.addActor(win2);

        win3 = new ObjectSelectionWindow(this, placeableObjects);
        stage.addActor(win3);


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
     * Called by LibGDX when the screen is shown
     */
    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    /**
     * Called by libGDX each frame.
     * @param delta The change in time since the last frame
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(backgroundColor);

        Gdx.input.setInputProcessor(stage);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override public void pause () {}

    @Override public void resume() {}

    @Override public void hide() {}

    @Override public void dispose() {
        VisUI.dispose();
    }

    public void linkToPropWindow(Object object) {
        propWindow.setObject(object);
    }

    public void loadPlaceableObjects() {
        Reflections reflections = new Reflections("com.mygdx.nextlevel");

        placeableObjects.clear();

        Set<Class<? extends Object>> classes = reflections.getTypesAnnotatedWith(Placeable.class);

        for(Class clazz : classes) {

            Placeable pa = (Placeable)clazz.getDeclaredAnnotation(Placeable.class);

            for(String textureName : pa.textures()) {
                placeableObjects.add(new PlaceableObject(clazz, textureName));
            }

        }
    }

    public Object getObjectToPlace() {
        propWindow.updateObject();
        return win3.getCurrentObject();
    }

    public PlaceableObject getPlaceableToPlace() {
        return win3.getCurrentPlaceable();
    }

    public void setScrollFocus(Actor actor) {
        stage.setScrollFocus(actor);
    }

    public ArrayList<PlaceableObject> getPlaceableObjects() {
        return placeableObjects;
    }

    public Stage getStage() { return stage; }

    public NextLevel getGame() { return game; }
}

