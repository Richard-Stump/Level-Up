package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;

public class CreateLevelMenuScreen implements Screen {
    private NextLevel           game;
    private Skin                skin;
    private TextureAtlas        atlas;
    private SpriteBatch         batch;
    private OrthographicCamera  camera;
    private Viewport            viewport;
    private Stage               stage;

    private final String titleText = "Create New Level";

    public CreateLevelMenuScreen(NextLevel game) {
        this.game = game;
        this.atlas = new TextureAtlas(Gdx.files.internal("skin/neon-ui.atlas"));
        this.skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);
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

        // Back button to return back to the main menu
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Label title = new Label(titleText, skin);

        // Create the main table and set it to fill the screen, and align at the top
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        // add the back button and title to the same row, setting the back button to the left of the screen
        mainTable.add(backButton).width(75);
        mainTable.add(title).center().expandX();

        // Create the fields to set the window width and height
        TextField widthField = new TextField("32", skin);
        TextField heightField = new TextField("32", skin);

        // Use a new table to center the settings. This is probably not the best to do this,
        // but it's what I found simple.
        Table settingsTable = new Table();
        settingsTable.top();
        settingsTable.add(new Label("Width:  ", skin));
        settingsTable.add(widthField);
        settingsTable.row();
        settingsTable.add(new Label("Height: ", skin));
        settingsTable.add(heightField);

        mainTable.row();
        mainTable.add(settingsTable).colspan(2).center().expand();

        // Button to create the new level
        TextButton createButton = new TextButton("Create", skin);
        createButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EditLevelScreen(game));
            }
        });

        mainTable.row();
        mainTable.add(createButton).colspan(2).center().expandX();

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
