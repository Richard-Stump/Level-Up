package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.editor.EditorLevel;

public class CreateLevelMenuScreen implements Screen {
    private NextLevel           game;
    private Skin                skin;
    private TextureAtlas        atlas;
    private SpriteBatch         batch;
    private OrthographicCamera  camera;
    private Viewport            viewport;
    private Stage               stage;

    private final String titleText = "Create New Level";
    public static int buttonWidth = 140;

    public CreateLevelMenuScreen(NextLevel game) {
        this.game = game;
        this.atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        this.batch = game.batch;

        // Create a new orthographic camera and set it to view the center of the screen.
        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        //Set up the viewport
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        // Back button to return back to the main menu
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);

        Label title = new Label(titleText, titleStyle);

        // Create the main table and set it to fill the screen, and align at the top
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // add the back button and title to the same row, setting the back button to the left of the screen
        //mainTable.add(backButton).padTop(10).padLeft(10).left();
        backButton.setPosition(10, 460);
        stage.addActor(backButton);
        mainTable.add(title).padTop(10).colspan(2);

        // Create the fields to set the window width and height
        final TextField nameField = new TextField("", skin);
        final TextField widthField = new TextField("32", skin);
        widthField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        final TextField heightField = new TextField("32", skin);
        heightField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        // Use a new table to center the settings. This is probably not the best to do this,
        // but it's what I found simple.
        Stack mainStack = new Stack();
        mainStack.add(new Image(new Texture(Gdx.files.internal("rect.png"))));

        Table settingsTable = new Table();
        //settingsTable.setDebug(true);
        settingsTable.top();
        settingsTable.add(new Label("Level Settings (These can be changed later)", skin)).colspan(2).padTop(5);
        settingsTable.row();
        settingsTable.add(new Label("", skin));
        settingsTable.row();
        settingsTable.add(new Label("Level Name:  ", skin));
        settingsTable.add(nameField);
        settingsTable.row();
        settingsTable.add(new Label("Width:  ", skin));
        settingsTable.add(widthField);
        settingsTable.row();
        settingsTable.add(new Label("Height: ", skin));
        settingsTable.add(heightField).padBottom(5);

        mainStack.add(settingsTable);

        mainTable.row();
        mainTable.add(mainStack).width(300).height(100).padTop(50).padBottom(50).colspan(2);

        // Button to create the new level
        TextButton createButton = new TextButton("Create", skin);
        createButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String name = nameField.getText();
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());

                EditorLevel level = new EditorLevel(name, width, height);

                ServerDBHandler dbHandler = new ServerDBHandler();
                CreatedLevelsDB createdDb = new CreatedLevelsDB();

                String id = createdDb.generateUniqueID(LoginScreen.curAcc);

                LevelInfo levelInfo = new LevelInfo(id, name, LoginScreen.curAcc);
                levelInfo.setPublic(false);

                dbHandler.addLevel(levelInfo);

                game.setScreen(new EditLevelScreen(game, level));
            }
        });
        createButton.addListener(new HoverListener());


        mainTable.row();
        mainTable.add(createButton).center().width(buttonWidth).colspan(2);


        // Add the ui to the scene
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
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
        skin.dispose();
        atlas.dispose();
    }
}
