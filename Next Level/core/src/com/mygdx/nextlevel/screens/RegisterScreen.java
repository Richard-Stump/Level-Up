package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.AccountList;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.ErrorDialog;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

import javax.swing.plaf.synth.SynthEditorPaneUI;
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

    //Dialogs
    Dialog noInfoDialog;
    Dialog passMatchDialog;
    Dialog passRegexDialog;
    Dialog passLenDialog;
    Dialog userLenDialog;
    Dialog userExistDialog;
    Dialog emailRegexDialog;
    Dialog emailExistDialog;
    Dialog createdDialog;

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
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);

        //Title
        Label title = new Label("Next Level", titleStyle);
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
        textPass.setPasswordMode(true);
        textVerifyPass.setPasswordMode(true);
        textPass.setPasswordCharacter('*');
        textVerifyPass.setPasswordCharacter('*');

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

        final CheckBox passwordBox = new CheckBox(null, skin);
        final CheckBox verifyPasswordBox = new CheckBox(null, skin);

        passwordBox.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //Gdx.graphics.setContinuousRendering(passwordBox.isChecked());
                if (passwordBox.isChecked()) {
                    textPass.setPasswordMode(false);
                }

                if (!passwordBox.isChecked()) {
                    textPass.setPasswordMode(true);
                }
            }
        });
        verifyPasswordBox.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //Gdx.graphics.setContinuousRendering(passwordBox.isChecked());
                if (verifyPasswordBox.isChecked()) {
                    textVerifyPass.setPasswordMode(false);
                }

                if (!verifyPasswordBox.isChecked()) {
                    textVerifyPass.setPasswordMode(true);
                }
            }
        });

        Table passFieldTable = new Table();
        passFieldTable.add(textPass).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
        Table checkboxPassTable = new Table();
        //checkboxPassTable.setDebug(true);
        checkboxPassTable.add(passwordBox).padLeft(textBoxWidth - 20).padBottom(20);

        Table verifyPassTable = new Table();
        verifyPassTable.add(textVerifyPass).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
        Table checkboxVPassTable = new Table();
        checkboxVPassTable.add(verifyPasswordBox).padLeft(textBoxWidth - 20).padBottom(20);

        Stack passStack = new Stack();
        passStack.add(passFieldTable);
        passStack.add(checkboxPassTable);

        Stack passVerifyStack = new Stack();
        passVerifyStack.add(verifyPassTable);
        passVerifyStack.add(checkboxVPassTable);

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
        textFieldTable.add(passStack);
        //textFieldTable.add(textPass).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
        textFieldTable.row();
        textFieldTable.add(passVerifyStack);
        //textFieldTable.add(textVerifyPass).prefWidth(textBoxWidth).padBottom(textBoxBottomPadding);
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
                        isInfoCorrect = false;
                        ErrorDialog noInfo = new ErrorDialog(skin, "Not Enough Information", stage);
                        noInfoDialog = noInfo.getErrorDialog();
                        break;
                    }

                    //check if passwords match
                    if (pass.compareTo(verifyPass) != 0) {
                        isInfoCorrect = false;
                        ErrorDialog noMatch = new ErrorDialog(skin, "Passwords Do Not Match", stage);
                        passMatchDialog = noMatch.getErrorDialog();
                        break;
                    } else {
                        //check username length
                        if (username.length() < 4 || username.length() > 16) {
                            isInfoCorrect = false;
                            ErrorDialog userLen = new ErrorDialog(skin, "Username must be at least 4 characters and no more than 16 characters", stage);
                            userLenDialog = userLen.getErrorDialog();
                            break;
                        }
                        //check if username exists already
                        if (db.userExists(username)) {
                            isInfoCorrect = false;
                            ErrorDialog userExists = new ErrorDialog(skin, "Account with this username already exists", stage);
                            userExistDialog = userExists.getErrorDialog();
                            break;
                        }
                        //check password length
                        if (!email.equals("")) {
                            String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                            Pattern p = Pattern.compile(emailRegex);
                            Matcher m = p.matcher(email);
                            if (!m.matches()) {
                                isInfoCorrect = false;
                                ErrorDialog emailRex = new ErrorDialog(skin, "Invalid email format", stage);
                                emailRegexDialog = emailRex.getErrorDialog();
                                break;
                            }
                            if (db.emailExists(email) > 0) {
                                isInfoCorrect = false;
                                ErrorDialog emailExists = new ErrorDialog(skin, "Email already associated with an account", stage);
                                emailExistDialog = emailExists.getErrorDialog();
                                break;
                            }
                        }
                        if (verifyPass.length() < 8 || verifyPass.length() > 16) {
                            isInfoCorrect = false;
                            ErrorDialog passLen = new ErrorDialog(skin, "Password must be at least 4 characters and no more than 16 characters", stage);
                            passLenDialog = passLen.getErrorDialog();
                            break;
                        } else {
                            String regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(verifyPass);
                            //check password requirements
                            if (m.matches()) {
                                if (isInfoCorrect) {
                                    System.out.println("Correct Information");
                                    //add account to database if everything is good
                                    Account a = new Account(username, pass, email);
                                    db.addUser(a);
                                    ErrorDialog newAccount = new ErrorDialog("Created", "LoginScreen", game, skin, "Account successfully created", stage);
                                    createdDialog = newAccount.getErrorDialog();
                                    break;
                                }
                            } else {
                                isInfoCorrect = false;
                                //error = "PassRegexError";
                                ErrorDialog passReg = new ErrorDialog(skin, "Password must have an uppercase, lowercase, special symbol, and a number", stage);
                                passRegexDialog = passReg.getErrorDialog();
                                isInfoCorrect = true;
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

//                if (isInfoCorrect) {
//                    //((Game) Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
//                    ErrorDialog newAccount = new ErrorDialog("Created", "LoginScreen", game, skin, "Account successfully created", stage);
//                    createdDialog = newAccount.getErrorDialog();
//                }

                //TODO: set to next screen
//                String[] ret = errorDisplay(error);
//                ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, ret[0], ret[1]));

            }
        });
        signUp.addListener(new HoverListener());
        textVerifyPass.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    isInfoCorrect = true;

                    //getting the inputs
                    username = textUsername.getText();
                    email = textEmail.getText();
                    pass = textPass.getText();
                    verifyPass = textVerifyPass.getText();

                    //check if all fields are filled
                    while (true) {
                        if ((username.isEmpty()) & (email.isEmpty()) & (pass.isEmpty())) {
                            //error = "NoInfoError";
                            isInfoCorrect = false;
                            ErrorDialog noInfo = new ErrorDialog(skin, "Not Enough Information", stage);
                            noInfoDialog = noInfo.getErrorDialog();
                            break;
                        }

                        //check if passwords match
                        if (pass.compareTo(verifyPass) != 0) {
                            isInfoCorrect = false;
                            //error = "PassMatchError";
                            ErrorDialog noMatch = new ErrorDialog(skin, "Passwords Do Not Match", stage);
                            passMatchDialog = noMatch.getErrorDialog();
                            break;
                        } else {
                            //check username length
                            if (username.length() < 4 || username.length() > 16) {
                                isInfoCorrect = false;
                                //error = "UsernameError";
                                ErrorDialog userLen = new ErrorDialog(skin, "Username must be at least 4 characters and no more than 16 characters", stage);
                                userLenDialog = userLen.getErrorDialog();
                                break;
                            }
                            //check if username exists already
                            if (db.userExists(username)) {
                                isInfoCorrect = false;
                                //error = "UserExistsError";
                                ErrorDialog userExists = new ErrorDialog(skin, "Account with this username already exists", stage);
                                userExistDialog = userExists.getErrorDialog();
                                break;
                            }
                            //check password length
                            if (!email.equals("")) {
                                String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                                Pattern p = Pattern.compile(emailRegex);
                                Matcher m = p.matcher(email);
                                if (!m.matches()) {
                                    isInfoCorrect = false;
                                    //error = "EmailRegexError";
                                    ErrorDialog emailRex = new ErrorDialog(skin, "Invalid email format", stage);
                                    emailRegexDialog = emailRex.getErrorDialog();
                                    break;
                                }
                                if (db.emailExists(email) > 0) {
                                    //error = "EmailExistsError";
                                    isInfoCorrect = false;
                                    ErrorDialog emailExists = new ErrorDialog(skin, "Email already associated with an account", stage);
                                    emailExistDialog = emailExists.getErrorDialog();
                                    break;
                                }
                            }
                            if (verifyPass.length() < 8 || verifyPass.length() > 16) {
                                isInfoCorrect = false;
                                //error = "PassLengthError";
                                ErrorDialog passLen = new ErrorDialog(skin, "Password must be at least 4 characters and no more than 16 characters", stage);
                                passLenDialog = passLen.getErrorDialog();
                                break;
                            } else {
                                String regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$";
                                Pattern p = Pattern.compile(regex);
                                Matcher m = p.matcher(verifyPass);
                                //check password requirements
                                if (m.matches()) {
                                    if (isInfoCorrect) {
                                        //add account to database if everything is good
                                        Account a = new Account(username, pass, email, "userIcon.png");
                                        db.addUser(a);
                                        ErrorDialog newAccount = new ErrorDialog("Created", "LoginScreen", game, skin, "Account successfully created", stage);
                                        createdDialog = newAccount.getErrorDialog();
                                        break;
                                    }
                                } else {
                                    isInfoCorrect = false;
                                    //error = "PassRegexError";
                                    ErrorDialog passReg = new ErrorDialog(skin, "Password must have an uppercase, lowercase, special symbol, and a number", stage);
                                    passRegexDialog = passReg.getErrorDialog();
                                    isInfoCorrect = true;
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

                }
                return false;
            }
        });
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

    public static boolean checkEmailRegex(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(emailRegex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            return true;
        }
        return false;
    }
}
