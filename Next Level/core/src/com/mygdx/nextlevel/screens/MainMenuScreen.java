package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
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
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import org.w3c.dom.Text;

public class MainMenuScreen extends LoginScreen implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    //player information
    public String username;
    public Image profilePic;

    //static vars
    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 200;
    public static int rightMargin = 870;
    public static int topMargin = 450;

    public MainMenuScreen (NextLevel game) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        this.game = game;
        this.username = MainMenuScreen.curAcc;
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        //Create Table
        Table mainTable = new Table();
        Table buttonTable = new Table();

        //Set alignment of contents in the table.
        //mainTable.top();

        //debug lines
        //mainTable.setDebug(true);
        //buttonTable.setDebug(true);

        //Create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton createLevelButton = new TextButton("Create Level", skin);
        TextButton selectLevelButton = new TextButton("Select Level", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        TextButton logoutButton = new TextButton("Logout", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        TextButton changePassButton = new TextButton("Change Password", skin);

        buttonTable.add(playButton).colspan(2).width(buttonWidth * 2);
        buttonTable.row();
        buttonTable.add(createLevelButton).width(buttonWidth);
        buttonTable.add(selectLevelButton).width(buttonWidth);
        buttonTable.row();
        buttonTable.add(tutorialButton).width(buttonWidth);
        buttonTable.add(changePassButton).width(buttonWidth);
        buttonTable.row();
        buttonTable.add(logoutButton).width(buttonWidth);
        buttonTable.add(exitButton).width(buttonWidth);

        //Add buttons to table
        Label titleLabel = new Label("Next Level", skin);
        Label welcomeLabel = new Label("Welcome back!", skin);

        //use player username
        Label usernameLabel = new Label(username, skin);
        //use player profile pic
        Image playerPic = new Image(new Texture(Gdx.files.internal("userIcon.png")));

        HorizontalGroup userGroup = new HorizontalGroup();
        userGroup.addActor(usernameLabel);
        userGroup.addActor(playerPic);
        userGroup.setPosition(rightMargin - 70, topMargin);
        stage.addActor(userGroup);

        //using a horizontal group instead to simplify, don't need this
        //playerPic.setPosition(rightMargin, topMargin);
        //usernameLabel.setPosition(rightMargin - 70, topMargin);
        //stage.addActor(playerPic);
        //stage.addActor(usernameLabel);

        //mainTable.add(usernameLabel).right();
        //mainTable.row();
        mainTable.add(titleLabel).padBottom(textBoxBottomPadding);
        mainTable.row();
        mainTable.add(welcomeLabel).padBottom(textBoxBottomPadding);
        mainTable.row();


        mainTable.add(buttonTable);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });
        playButton.addListener(new HoverListener());
        createLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new CreateLevelMenuScreen(game));
            }
        });
        createLevelButton.addListener(new HoverListener());
        selectLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelSelectionScreen(game));
            }
        });
        selectLevelButton.addListener(new HoverListener());
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new TutorialScreen(game));
            }
        });
        tutorialButton.addListener(new HoverListener());
        changePassButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new ChangePasswordScreen(game));
            }
        });
        changePassButton.addListener(new HoverListener());
        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
            }
        });
        logoutButton.addListener(new HoverListener());
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        exitButton.addListener(new HoverListener());
        userGroup.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new UserAccountScreen(game));
            }
        });

        //Set table to fill stage
        mainTable.setFillParent(true);

        //Add table to stage
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