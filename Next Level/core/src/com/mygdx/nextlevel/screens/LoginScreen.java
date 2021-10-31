package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.AccountList;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import sun.rmi.runtime.Log;

import java.util.ArrayList;

public class LoginScreen extends AccountList implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    //textFields
    public TextField textUsername;
    public TextField textPass;

    //inputted strings
    public String username;
    public String pass;

    //buttons
    public TextButton loginButton;
    public Label regButton;
    public Label forgotPassButton;

    //static vars
    public static int textBoxWidth = 300;
    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 170;

    public boolean loginSuccessful = false;
    public boolean incorrectPass = false;
    public ServerDBHandler db;

    public LoginScreen() {}

    public LoginScreen(NextLevel game)  {
        db = new ServerDBHandler();
        atlas = new TextureAtlas("skin/neon-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        this.game = game;
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        Table textFieldTable = new Table();
        Table rightButtonTable = new Table();

        //text
        Label titleLabel = new Label("Next Level", skin);
        Label welcomeLabel = new Label("Welcome", skin);

        //creating text fields
        textUsername = new TextField("", skin);
        textPass = new TextField("", skin);

        //initial information
        textUsername.setMessageText("Username");
        textPass.setMessageText("Password");

        //buttons
        loginButton = new TextButton("Login", skin);
        regButton = new Label("No account? Register for free", skin);
        forgotPassButton = new Label("Forgot Password?", skin);

        //Debug lines
        //table.setDebug(true);
        //textFieldTable.setDebug(true);
        //rightButtonTable.setDebug(true);

        textFieldTable.add(textUsername).width(textBoxWidth).padBottom(textBoxBottomPadding);
        textFieldTable.row();
        textFieldTable.add(textPass).width(textBoxWidth).padBottom(textBoxBottomPadding);

        rightButtonTable.add(regButton).padBottom(textBoxBottomPadding / 2f).right();
        rightButtonTable.row();
        rightButtonTable.add(forgotPassButton).padBottom(textBoxBottomPadding / 2f).right();

        //adding actors to table
        table.add(titleLabel).padBottom(textBoxBottomPadding);
        //table.row();
        //table.add(new Label("", skin)).width(300);
        table.row();
        table.add(welcomeLabel).padBottom(textBoxBottomPadding);
        table.row();
        table.add(textFieldTable);
        table.row();
        table.add(rightButtonTable).right();
        table.row();
        table.row();
        table.add(loginButton).width(buttonWidth).padTop(25);

        //event listeners
        regButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                ((Game)Gdx.app.getApplicationListener()).setScreen(new RegisterScreen(game));
            }
        });
        regButton.addListener(new HoverListener());
        forgotPassButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                //TODO: send back to forget password page
                ((Game)Gdx.app.getApplicationListener()).setScreen(new ForgetPasswordScreen(game));
            }
        });
        forgotPassButton.addListener(new HoverListener());
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                //get information from text fields
                username = textUsername.getText();
                pass = textPass.getText();

                //TODO: verification
//                for (Account a : getAccList()) {
//                    if (a.getUsername().equals(username) && (a.getPassword().equals(pass))) {
//                        loginSuccessful = true;
//                        ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
//                        break;
//                    } else if (a.getUsername().equals(username) && !a.getPassword().equals(pass)) {
//                        System.out.println("Incorrect password.");
//                        incorrectPass = true;
//                        textPass.setMessageText("Password");
//                        break;
//                    }
//                }
//                if (!loginSuccessful && !incorrectPass) {
//                    System.out.println("There is no account associated with this username.");
//                }
//                incorrectPass = false;
                System.out.println(db.userExists(username));
                if (db.userExists(username)) {
                    String ret = db.getPassword(username);
                    System.out.println(ret);
                    if (pass.equals(ret)) {
                        loginSuccessful = true;
                        ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));

                    } else if (ret.equals("")) {
                        textPass.setMessageText("Password");
                        System.out.println("Password is incorrect");
                    }
                } else {
                    System.out.println("No account linked to this username");
                }


            }
        });

        table.setFillParent(true);
        stage.addActor(table);
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

    public static boolean login(String username, String pass) {
        for (Account a : getAccList()) {
            if (a.getUsername().equals(username) && (a.getPassword().equals(pass))) {
                return true;
            }
        }
        return false;

    }
}
