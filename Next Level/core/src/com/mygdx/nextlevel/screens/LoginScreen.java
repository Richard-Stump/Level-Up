package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.AccountList;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;

public class LoginScreen extends AccountList implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    //textFields
    public TextField textUsername, textPass;
//    public TextField textPass;

    //inputted strings
    public String username, pass;
//    public String pass;

    //buttons
    public TextButton loginButton;
    public Label regButton;
    public Label forgotPassButton;

    //static vars
    public static int textBoxWidth = 300;
    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 170;

    private String error = "";
    public static String curAcc = "";
    public ServerDBHandler db;
    public Dialog errorDialog;

    public float countdown;

    public LoginScreen() {}

    public LoginScreen(NextLevel game)  {
        db = new ServerDBHandler();
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
    }

    @Override
    public void show() {
        System.out.println();
        String[][] tab = db.getTable();
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[0].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }

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
//        textPass.setPasswordMode(true);
//        textPass.setPasswordCharacter('*');

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
        table.add(welcomeLabel).padBottom(textBoxBottomPadding + 25);
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

                //check if user exists
                while (true) {
                    if (db.userExists(username)) {
                        String ret = db.getPassword(username);
                        //check password is correct
                        if (pass.equals(ret)) {
                            curAcc = username;
                            break;
//                            loadDB();
                        } else if (!pass.equals(ret)) {
//                        errorDialog = new Dialog("Warning", skin);
//                        errorDialog.text("Incorrect Password. Please try again");
//                        errorDialog.show(stage);
                            textPass.setMessageText("Password");
                            error = "IncorrectPass";
                            break;
                        }
                    } else {
                        error = "NoAccount";
                        break;
//                    errorDialog = new Dialog("Warning", skin) {
//                        protected void result(Object object)
//                        {
//                            System.out.println("Option: " + object);
//                            Timer.schedule(new Timer.Task()
//                            {
//
//                                @Override
//                                public void run()
//                                {
//                                    errorDialog.show(stage);
//                                }
//                            }, 1);
//                        }
//                    };
//
//                    Timer.schedule(new Timer.Task()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            errorDialog.show(stage);
//                            //errorDialog.cancel();
//
//                            errorDialog.hide();
//                        }
//                    }, 1);
//
//                    errorDialog.text("No account linked to this username");
                        //errorDialog.show(stage);
                    }
                }
                String[] retVal = errorDisplay(error);
                if (retVal[0].equals("")) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
                } else {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, retVal[0], retVal[1]));
                }


            }
        });
        loginButton.addListener(new HoverListener());

        table.setFillParent(true);
        stage.addActor(table);
    }


    private void loadDB() {
        ServerDBHandler serverDB = new ServerDBHandler();
        CreatedLevelsDB createdDB = new CreatedLevelsDB();

        for (LevelInfo licreated: createdDB.sortByTitle()) {
            createdDB.removeLevelInfo(licreated.getId());
        }

        /*
        String id = createdDB.generateUniqueID(username);
        LevelInfo levelInfo = new LevelInfo(id, "Level Test " + id, username);
        levelInfo.addTag(Tag.ART);
        levelInfo.setDifficulty(Difficulty.HARD.ordinal());

        String id2 = createdDB.generateUniqueID(username);
        LevelInfo levelInfo2 = new LevelInfo(id2, "Level Test " + id2, username);
        levelInfo2.addTag(Tag.BOSSBATTLE);
        levelInfo2.addTag(Tag.ART);
        levelInfo2.setDifficulty(Difficulty.EASY.ordinal());

        serverDB.addLevel(levelInfo);
        serverDB.addLevel(levelInfo2);

         */

        for (LevelInfo li: serverDB.sortByTitle()) {
            if (li.getAuthor().equals(username)) {
                createdDB.addLevelInfo(li);
            }
        }
        serverDB.closeConnection();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        //errorDialog.act(delta);
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

    private String[] errorDisplay(String error) {
        switch (error) {
            case "IncorrectPass" :
                return new String[] {"Password is incorrect", "LoginScreen"};
            case "NoAccount" :
                return new String[] {"No account associated with this username", "LoginScreen"};
            default :
                return new String[] {"", ""};
        }
    }
    public static String getCurAcc() {
        return curAcc;
    }
}
