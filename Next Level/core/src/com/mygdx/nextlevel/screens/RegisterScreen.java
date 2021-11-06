package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    public TextField textUsername, textEmail, textPass, textVerifyPass;

    //User inputted strings
    public String username, email, pass, verifyPass;

    //static vars
    public static int textBoxWidth = 400;
    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 170;

    //checking errors
    public boolean isInfoCorrect = true;
    private String error = "";

    //open database
    public ServerDBHandler db = new ServerDBHandler();


    /**
     * Default constructor
     */
    public RegisterScreen() {

    }

    /**
     * Constructor
     * @param game
     */
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

        Label userReqText = new Label("Make sure your username is more than 4 characters and less than 16 characters.", skin);
        Label passReqText = new Label("Make sure your password has 8-16 characters and contains:", skin);
        Label upperText = new Label(" - at least one uppercase letter (A-Z)", skin);
        Label lowerText = new Label(" - at least one lowercase letter (a-z)", skin);
        Label numText = new Label(" - at least one number (0-9)", skin);
        Label spCharText = new Label(" - at least one special character ($%#@!%^&*)", skin);

        userReqText.setWrap(true);
        userReqText.setWidth(300);
        passReqText.setWrap(true);
        passReqText.setWidth(300);

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
//        textPass.setPasswordMode(true);
//        textVerifyPass.setPasswordMode(true);
//        textPass.setPasswordCharacter('*');
//        textVerifyPass.setPasswordCharacter('*');

        //buttons
        TextButton back = new TextButton("I have an account!", skin);
        TextButton signUp = new TextButton("Sign Up", skin);

        final Table table = new Table();
        Table textFieldTable = new Table();
        Table reqTable = new Table();

        reqTable.add(userReqText).left().width(400f).padBottom(5).padLeft(5);
        reqTable.row();
        reqTable.add(passReqText).left().width(400f).padLeft(5);
        reqTable.row();
        reqTable.add(upperText).left().padLeft(5);
        reqTable.row();
        reqTable.add(lowerText).left().padLeft(5);
        reqTable.row();
        reqTable.add(numText).left().padLeft(5);
        reqTable.row();
        reqTable.add(spCharText).left().padLeft(5);

        Stack mainStack = new Stack();
        mainStack.add(new Image(new Texture(Gdx.files.internal("rect.png"))));
        mainStack.add(reqTable);

        //debug lines table, cell, and widgets
        //table.setDebug(true);
        //reqTable.setDebug(true);
        //textFieldTable.setDebug(true);

        table.add(title).colspan(2).padBottom(textBoxBottomPadding).padTop(5);
        table.row().height(1);
        table.add(welcome).colspan(2).padBottom(textBoxBottomPadding);
        table.row();
        table.add(mainStack).colspan(2).padBottom(textBoxBottomPadding + 5);
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
        table.add(back).width(buttonWidth).colspan(1).expandY();
        table.add(signUp).width(buttonWidth).colspan(1).expandY();
        table.row();
        table.add(new Label("", skin)).colspan(2).expandX();

        Table mainTable = new Table();

        VerticalGroup mainVerticalGroup = new VerticalGroup();
        mainVerticalGroup.addActor(mainTable);

        //ScrollPane scrollPane = new ScrollPane(mainVerticalGroup, skin);
        //scrollPane.setForceScroll(true, false);

        //mainTable.add(scrollPane);
        mainTable.add(table);

        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        //button event listeners
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
            }
        });
        back.addListener(new HoverListener());
        signUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isInfoCorrect = true;

                //getting the inputs
                username = textUsername.getText();
                email = textEmail.getText();
                pass = textPass.getText();
                verifyPass = textVerifyPass.getText();

                //check if all fields are filled
                while (true) {
                    if ((username.isEmpty()) & (email.isEmpty()) & (pass.isEmpty())) {
                        error = "NoInfoError";
                        isInfoCorrect = false;
                        break;
                    }

                    //check if passwords match
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
                        isInfoCorrect = false;
//                    passMatchError = true;
                        error = "PassMatchError";
                        break;
                    } else {
                        //check username length
                        if (username.length() < 4 || username.length() > 16) {
                            isInfoCorrect = false;
                            error = "UsernameError";
                            break;
                        }
                        //check if username exists already
                        if (db.userExists(username)) {
                            isInfoCorrect = false;
                            error = "UserExistsError";
                            break;
                        }
                        //check password length
                        if (verifyPass.length() < 8 || verifyPass.length() > 16) {
                            isInfoCorrect = false;
                            error = "PassLengthError";
                            break;
                        } else {
                            String regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(verifyPass);
                            //check password requirements
                            if (m.matches()) {
                                if (isInfoCorrect) {
                                    //add account to database if everything is good
                                    Account a = new Account(username, pass, email);
                                    db.addUser(a);
                                    break;
                                }
                            } else {
                                isInfoCorrect = false;
                                error = "PassRegexError";
                                break;
                            }
                        }
                    }
                }

                //reset all textFields
                textUsername.setText("");
                textEmail.setText("");
                textPass.setText("");
                textVerifyPass.setText("");

                //TODO: set to next screen
                String[] ret = errorDisplay(error);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, ret[0], ret[1]));

                //small code chunk to create popup (not working)
//                Dialog dialog = new Dialog("Error", null, "dialog");
//                dialog.text("Not enough information");
//                dialog.show(stage);
//                stage.addActor(new MessageDialog("Not enough information"));
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

    private String[] errorDisplay(String error) {
        switch (error) {
            case "NoInfoError" :
                return new String[] {"Not enough information", "RegisterScreen"};
            case "PassMatchError" :
                return new String[] {"Passwords do not match", "RegisterScreen"};
            case "UsernameError":
                return new String[] {"Username must be at least 4 characters and no more than 16 characters", "RegisterScreen"};
            case "PassLengthError" :
                return new String[] {"Password must be at least 4 characters and no more than 16 characters", "RegisterScreen"};
            case "PassRegexError" :
                return new String[] {"Password must have an uppercase, lowercase, special symbol, and a number", "RegisterScreen"};
            case "UserExistsError" :
                return new String[] {"Account with this username already exists", "RegisterScreen"};
            default :
                return new String[] {"Account successfully created", "LoginScreen"};
        }
    }
}
