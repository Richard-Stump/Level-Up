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
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

public class ProfileMainMenu extends LoginScreen implements Screen {
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
    public static int bottomPadding = 20;
    public static int buttonWidth = 300;
    public static int rightMargin = 870;
    public static int topMargin = 450;
    ServerDBHandler db = new ServerDBHandler();

    public ProfileMainMenu (NextLevel game) {
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
        //this.username = MainMenuScreen.curAcc;
        this.username = "michelle";
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Table mainTable = new Table();

        //TODO: set up format for profile menu
        TextButton backButton = new TextButton("Back", skin);

        Image playerPic = new Image(new Texture(Gdx.files.internal("userIcon.png")));
        playerPic.scaleBy(3f);

        Label usernameLabel = new Label(username, skin);

        TextButton changeProfilePicButton = new TextButton("Change Profile Picture", skin);
        TextButton changePasswordButton = new TextButton("Change Password", skin);
        TextButton deleteLevelsButton = new TextButton("Delete Levels", skin);


        //TODO: rewire the buttons
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        backButton.addListener(new HoverListener());
        changePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new ChangePasswordScreen(game));
            }
        });
        changePasswordButton.addListener(new HoverListener());
        deleteLevelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new UserAccountScreen(game));
            }
        });
        deleteLevelsButton.addListener(new HoverListener());


        //vertical groups
//        VerticalGroup profileGroup = new VerticalGroup();
//        profileGroup.addActor(playerPic);
//        profileGroup.addActor(usernameLabel);

        Table profileTable = new Table();
        //profileTable.setDebug(true);
        profileTable.add(playerPic).padRight(100).padBottom(bottomPadding).padLeft(100);
        profileTable.row();
        profileTable.add(usernameLabel).padLeft(100);

//        VerticalGroup buttonGroup = new VerticalGroup();
//        buttonGroup.addActor(changeProfilePicButton);
//        buttonGroup.addActor(changePasswordButton);
//        buttonGroup.addActor(deleteLevelsButton);

        Table buttonTable = new Table();
        buttonTable.add(changeProfilePicButton).width(buttonWidth).padBottom(bottomPadding);
        buttonTable.row();
        buttonTable.add(changePasswordButton).width(buttonWidth).padBottom(bottomPadding);
        buttonTable.row();
        buttonTable.add(deleteLevelsButton).width(buttonWidth).padBottom(bottomPadding);

        //adding to main table
        //mainTable.setDebug(true);
        mainTable.add(backButton).left().padLeft(20).padTop(15);
        mainTable.row();
        mainTable.add(profileTable).expandY().width(450);
        mainTable.add(buttonTable).expandY().expandX().padRight(100).padBottom(50);

//        mainTable.add(playerPic).expandY();
//        mainTable.add(usernameLabel);
//        mainTable.row();
//        mainTable.add(changeProfilePicButton);
//        mainTable.row();
//        mainTable.add(changePasswordButton);
//        mainTable.row();
//        mainTable.add(deleteLevelsButton);

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
