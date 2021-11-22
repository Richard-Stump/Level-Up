package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A Screen that enables the user to pick a level from their list of created levels and edit it
 */
public class MyLevelsScreen implements Screen {
    protected NextLevel game;
    protected Skin skin;
    protected TextureAtlas atlas;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected String username;
    protected Stage stage;
    protected Viewport viewport;

    /**
     * Constructs the screen
     * @param game The game that created this screen
     */
    public MyLevelsScreen(NextLevel game) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();

        camera.position.set(0, 0, 0);
        camera.update();

        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        stage = new Stage(viewport, batch);
        this.game = game;
        this.username = LoginScreen.getCurAcc();
    }

    @Override
    public void show() {
        //Setup i/o for the screen.
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);
        Table mainTable = new Table();
        Table scrollTable = new Table();
        scrollTable.top();
        scrollTable.pad(50.0f);

        //set up the scroll pane.
        ScrollPane scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        ServerDBHandler dbHandler = new ServerDBHandler();
        ArrayList<LevelInfo> createdLevels = dbHandler.getUsersCreatedLevels(this.username);

        //Use the background for the scroll screen for each of the level's table instead, and set
        //the scroll pane to have no background. This looks more visually appealing.
        Drawable background = scrollPane.getStyle().background;
        scrollPane.getStyle().background = null;

        //Add each of the levels to the table list
        for(final LevelInfo level : createdLevels) {
            Table levelTable = addLevelTable(level, background, titleStyle);

            scrollTable.row();
            scrollTable.add(levelTable);
        }

        //Add everything to the main table
        mainTable.add(new Label("My Levels", titleStyle));
        mainTable.row();
        mainTable.add(scrollPane).expand().align(Align.top);

        //Button to take the user back to the main menu
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        backButton.setPosition(0.0f, 500.0f - backButton.getHeight());

        //Add everything to the stage so it works and is rendered
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        stage.addActor(backButton);
    }

    public Table addLevelTable(final LevelInfo level, Drawable background, Label.LabelStyle titleStyle) {
        Table levelTable = new Table(); //Overall table
        Table leftTable = new Table();  //Table to hold the Title and other information
        Table rightTable = new Table();

        //Add the information on this level.
        //TODO: Add a picture to display?
        leftTable.top();
        leftTable.add(new Label(level.getTitle(), titleStyle)).align(Align.left);
        leftTable.row();
        leftTable.add(new Label("Published: " + level.isPublic(), skin)).align(Align.left).padLeft(20.0f);

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Play Pressed");
            }
        });

        //Add a button that takes the user to the editor with this level
        TextButton editButton = new TextButton("Edit", skin);
        editButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new EditLevelScreen(game, level));
            }
        });

        rightTable.top();
        rightTable.add(playButton);
        rightTable.row();
        rightTable.add(editButton);

        //Set a background that allows the user to easily differentiate the levels
        levelTable.setBackground(background);

        //Add the info and button to the overall table
        levelTable.top();
        levelTable.add(leftTable).padRight(50.0f);
        levelTable.add(rightTable);

        return levelTable;
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
