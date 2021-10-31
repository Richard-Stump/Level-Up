package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.AccountList;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterScreen extends AccountList implements Screen{

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    //text fields
    public TextField textUsername;
    public TextField textEmail;
    public TextField textPass;
    public TextField textVerifyPass;

    //User inputted strings
    public String username;
    public String email;
    public String pass;
    public String verifyPass;

    //static vars
    public static int textBoxWidth = 200;
    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 170;

    public boolean isInfoCorrect = true;
    public ServerDBHandler db = new ServerDBHandler();

    public RegisterScreen() {

    }

    public RegisterScreen (NextLevel game) {
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

        //Title
        Label title = new Label("Next Level", skin);
        Label welcome = new Label("Welcome", skin);

        //creating text boxes
        textUsername = new TextField("", skin);
        textEmail = new TextField("", skin);
        textPass = new TextField("", skin);
        textVerifyPass = new TextField("", skin);

        //adding message to text boxes
        textUsername.setMessageText("Username");
        textEmail.setMessageText("Email");
        textPass.setMessageText("Password");
        textVerifyPass.setMessageText("Renter Password");

        //password mode
        textPass.setPasswordMode(true);
        textVerifyPass.setPasswordMode(true);
        textPass.setPasswordCharacter('*');
        textVerifyPass.setPasswordCharacter('*');

        //buttons
        TextButton back = new TextButton("I have an account!", skin);
        TextButton signUp = new TextButton("Sign Up", skin);

        final Table table = new Table();
        Table textFieldTable = new Table();

        //debug lines table, cell, and widgets
        //table.setDebug(true);
        //textFieldTable.setDebug(true);

        table.add(title).colspan(2).padBottom(textBoxBottomPadding);
        table.row();
        table.add(welcome).colspan(2).padBottom(textBoxBottomPadding + 20);
        table.row();
        textFieldTable.add(textUsername).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
        textFieldTable.row();
        textFieldTable.add(textEmail).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
        textFieldTable.row();
        textFieldTable.add(textPass).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
        textFieldTable.row();
        textFieldTable.add(textVerifyPass).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
        textFieldTable.row();
        table.add(textFieldTable).colspan(2);
        table.row();
        table.add(back).width(buttonWidth).colspan(1);
        table.add(signUp).width(buttonWidth).colspan(1);

        table.setFillParent(true);
        stage.addActor(table);

        //button event listeners
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: send back to login page
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
            }
        });
        back.addListener(new HoverListener());
        signUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isInfoCorrect = true;

                //after clicking sign up code should get all the inputs
                //getting the inputs
                username = textUsername.getText();
                email = textEmail.getText();
                pass = textPass.getText();
                verifyPass = textVerifyPass.getText();

                //TODO: check if password contains at least one capital, number, lowercase, and special character
                //TODO: fix bug where account is still added to arraylist (temp database) despite it not meeting requirements

                //check if username or email exists already
                for (Account account : accList) {
                    if (account.getUsername().equals(username)) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Username already exists", "RegisterScreen"));
                        isInfoCorrect = false;
                    } else if (account.getEmail().equals(email)) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Email is already connected to an account", "RegisterScreen"));
                        isInfoCorrect = false;
                    }
                }
                //TODO: if there is no input
                if ((username.isEmpty()) & (email.isEmpty()) & (pass.isEmpty())) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Not enough information", "RegisterScreen"));

                    isInfoCorrect = false;
                }

                //TODO: error message if pass and verify pass do not match
                if (pass.compareTo(verifyPass) != 0) {
//                        errorStage = new Stage(viewport, batch);
//                        Table errorTable = new Table();
//                        errorTable.add(new Label("Passwords do not match", skin));
//                        RunnableAction setErrorMessage = new RunnableAction();
//                        setErrorMessage.setRunnable(new Runnable() {
//                            @Override
//                            public void run() {
//                                game.setScreen(new ErrorMessageScreen(game, "Passwords do not match"));
//                                dispose();
//                            }
//                        });
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Passwords do not match", "RegisterScreen"));
                    isInfoCorrect = false;
                } else {
                    if (username.length() < 4 || username.length() > 16) {
                        System.out.println("Username must be at least 4 characters and no more than 16 characters");
                        isInfoCorrect = false;
                    }
//                    for (Account a : accList) {
//                        if (a.getUsername().equals(username)) {
//                            System.out.println("Username already exists");
//                            isInfoCorrect = false;
//                        }
//                    }
                    if (db.userExists(username)) {
                        isInfoCorrect = false;
                    }
                    if (verifyPass.length() < 8 || verifyPass.length() > 16) {
                        System.out.println("Password must be at least 8 characters and no more than 16 characters");
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Password must be at least 8 characters and no more than 16 characters", "RegisterScreen"));
                        isInfoCorrect = false;
                    } else {
                        String regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(verifyPass);
                        if (m.matches()) {
                            System.out.println("Password meets requirements. Account Created.");

                            if (isInfoCorrect) {
                                Account a = new Account(username, pass, email);
//                                a.setPassword(pass);
//                                a.setUsername(username);
//                                a.setEmail(email);
                                //TODO: add account into database
//                                getAccList().add(a);
                                db.addUser(a);

                            }

                            //successful add to database, user is automatically set to main menu
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
                        }
                        else {
                            System.out.println("Password must have upper, lower, symbol, and digit");
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Password must have upper, lower, symbol, and digit", "RegisterScreen"));
                            isInfoCorrect = false;
                        }
                    }
                }

                //System.out.println(isInfoCorrect);

                //TODO: Verification

                //reset all textFields
                textUsername.setText("");
                textEmail.setText("");
                textPass.setText("");
                textVerifyPass.setText("");

                //TODO: set to next screen
                if (isInfoCorrect) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
                }

                //using arraylist for now since database is not setup
                for (int i = 0; i < accList.size(); i++) {
                    Account ac = accList.get(i);
                    System.out.println(String.format("User: %s, Pass: %s, Email: %s", ac.getUsername(), ac.getPassword(), ac.getEmail()));
                }
                System.out.println();
                db.closeConnection();
            }
        });
        signUp.addListener(new HoverListener());

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


    public static boolean checkPasswords(String pass, String verify) {
        if (pass.equals(verify)) {
            return true;
        }
        return false;
    }

    public static boolean checkUsername(String user) {
        if (user.length() < 4 || user.length() > 16) {
            return false;
        }
        return true;
    }

    public static boolean checkPassLength(String pass) {
        if (pass.length() < 8 || pass.length() > 16) {
            return false;
        }
        return true;
    }

    public static boolean checkRegex(String pass) {
        String regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pass);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    public static void addAccount(Account a) {
        accList.add(a);
    }

    public static boolean checkUniqueUser(String username) {
        for (Account a : accList) {
            if (a.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
}
