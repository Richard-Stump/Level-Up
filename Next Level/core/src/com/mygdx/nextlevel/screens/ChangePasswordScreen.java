package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.ErrorDialog;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordScreen extends LoginScreen implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    private String oldPassword;
    private String newPassword;
    private String verifyNewPassword;

    //static vars
    public static int textBoxWidth = 350;
    public static int textBoxBottomPadding = 10;
    public static int labelWidth = 350;
    public static int labelBottomPadding = 5;
    public static int buttonWidth = 350;

    boolean passLengthError = false;
    boolean passMatchError = false;
    boolean passRegexError = false;
    boolean oldPassError = false;
    boolean isInfoCorrect = true;
    ServerDBHandler db = new ServerDBHandler();

    Dialog passMatchDialog;
    Dialog oldPassMatchDialog;
    Dialog newPassLenDialog;
    Dialog newPassRegDialog;
    Dialog changedPassDialog;

    public ChangePasswordScreen(NextLevel game) {
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
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Table table = new Table();
        //table.setDebug(true);
        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);

        Label title = new Label("Change Password", titleStyle);
        Label oldPasswordLabel = new Label("Current Password", skin);
        Label newPasswordLabel = new Label("New Password", skin);
        Label verifyNewPasswordLabel = new Label("Re-enter Your New Password", skin);

        Label passReqText = new Label("In order to protect your account, make sure your password:", skin);
        Label lenText = new Label(" - has 8-16 characters", skin);
        Label upperText = new Label(" - at least one uppercase letter (A-Z)", skin);
        Label lowerText = new Label(" - at least one lowercase letter (a-z)", skin);
        Label numText = new Label(" - at least one number (0-9)", skin);
        Label spCharText = new Label(" - at least one special character ($%#@!%^&*)", skin);

        passReqText.setWrap(true);
        passReqText.setWidth(220);

        Table reqTable = new Table();
        reqTable.add(passReqText).left().width(380f).padLeft(5);
        reqTable.row();
        reqTable.add(lenText).left().padLeft(5);
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

        final TextField oldPasswordField = new TextField("", skin);
        final TextField newPasswordField = new TextField("", skin);
        final TextField verifyNewPasswordField = new TextField("", skin);

        oldPasswordField.setMessageText("Password");
        newPasswordField.setMessageText("********");
        verifyNewPasswordField.setMessageText("********");

        oldPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordMode(true);
        verifyNewPasswordField.setPasswordMode(true);

        oldPasswordField.setPasswordCharacter('*');
        newPasswordField.setPasswordCharacter('*');
        verifyNewPasswordField.setPasswordCharacter('*');

        final CheckBox oldPassBox = new CheckBox(null, skin);
        final CheckBox newPassBox = new CheckBox(null, skin);
        final CheckBox verifyNewPassBox = new CheckBox(null, skin);

        oldPassBox.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //Gdx.graphics.setContinuousRendering(passwordBox.isChecked());
                if (oldPassBox.isChecked()) {
                    oldPasswordField.setPasswordMode(false);
                }

                if (!oldPassBox.isChecked()) {
                    oldPasswordField.setPasswordMode(true);
                }
            }
        });
        newPassBox.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //Gdx.graphics.setContinuousRendering(passwordBox.isChecked());
                if (newPassBox.isChecked()) {
                    newPasswordField.setPasswordMode(false);
                }

                if (!newPassBox.isChecked()) {
                    newPasswordField.setPasswordMode(true);
                }
            }
        });
        verifyNewPassBox.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //Gdx.graphics.setContinuousRendering(passwordBox.isChecked());
                if (verifyNewPassBox.isChecked()) {
                    verifyNewPasswordField.setPasswordMode(false);
                }

                if (!verifyNewPassBox.isChecked()) {
                    verifyNewPasswordField.setPasswordMode(true);
                }
            }
        });

        Table oldPassTable = new Table();
        oldPassTable.add(oldPasswordField).width(textBoxWidth + 4);
        Table oldPassBoxTable = new Table();
        //oldPassBoxTable.setDebug(true);
        oldPassBoxTable.add(oldPassBox).padLeft(textBoxWidth - 20);

        Table newPassTable = new Table();
        newPassTable.add(newPasswordField).width(textBoxWidth + 4);
        Table newPassBoxTable = new Table();
        newPassBoxTable.add(newPassBox).padLeft(textBoxWidth - 20);

        Table verifyNewPassTable = new Table();
        verifyNewPassTable.add(verifyNewPasswordField).width(textBoxWidth + 4);
        Table verifyNewPassBoxTable = new Table();
        verifyNewPassBoxTable.add(verifyNewPassBox).padLeft(textBoxWidth - 20);

        Stack oldPassStack = new Stack();
        oldPassStack.add(oldPassTable);
        oldPassStack.add(oldPassBoxTable);

        Stack newPassStack = new Stack();
        newPassStack.add(newPassTable);
        newPassStack.add(newPassBoxTable);

        Stack verifyNewPassStack = new Stack();
        verifyNewPassStack.add(verifyNewPassTable);
        verifyNewPassStack.add(verifyNewPassBoxTable);

        TextButton backButton = new TextButton("Back", skin);
        final TextButton changePasswordButton = new TextButton("Change Password", skin);

        table.add(backButton).left().expandX().padLeft(20).padTop(15);
        //table.add(new Label("", skin)).width(labelWidth).expandX();
        table.row();
        table.add(title).padBottom(labelBottomPadding + 10);
        table.row();
        table.add(mainStack).padBottom(labelBottomPadding);
        table.row();
        table.add(oldPasswordLabel).width(labelWidth).padTop(textBoxBottomPadding);
        table.row();
        table.add(oldPassStack);
        //table.add(oldPasswordField).width(textBoxWidth + 4);
        table.row();
        table.add(newPasswordLabel).width(labelWidth);
        table.row();
        table.add(newPassStack);
        //table.add(newPasswordField).width(textBoxWidth + 4);
        table.row();
        table.add(verifyNewPasswordLabel).width(labelWidth);
        table.row();
        table.add(verifyNewPassStack);
        //table.add(verifyNewPasswordField).width(textBoxWidth + 4).padBottom(textBoxBottomPadding);
        table.row();
        table.add(changePasswordButton).width(buttonWidth + 18).expandY().top().padBottom(10).padTop(10);

        //event listeners
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ProfileMainMenu(game));
            }
        });
        backButton.addListener(new HoverListener());
        changePasswordButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                isInfoCorrect = true;

                //TODO: update database and if error send message if successful send message
                // send back to main screen or ?
                //game.setScreen(new MainMenuScreen(game));
                oldPassword = oldPasswordField.getText();
                newPassword = newPasswordField.getText();
                verifyNewPassword = verifyNewPasswordField.getText();
                if (!newPassword.equals(verifyNewPassword)) {
                    isInfoCorrect = false;
                    //passMatchError = true;
                    ErrorDialog passMatch = new ErrorDialog(skin, "New passwords do not match", stage);
                    passMatchDialog = passMatch.getErrorDialog();
                } else {
                    if (verifyNewPassword.length() < 8 || verifyNewPassword.length() > 16) {
                        System.out.println("Password must be at least 8 characters and no more than 16 characters");
                        isInfoCorrect = false;
                        //passLengthError = true;
                        ErrorDialog passLen = new ErrorDialog(skin, "New password must be at least 8 characters and no more than 16 characters", stage);
                        newPassLenDialog = passLen.getErrorDialog();
                    } else {
                        String regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(verifyNewPassword);
                        if (m.matches()) {
                            if (isInfoCorrect) {
                                System.out.println(getCurAcc());
                                if (db.getPassword(getCurAcc()).equals(oldPassword)) {
                                    db.changePassword(getCurAcc(), newPassword);
                                } else {
                                    isInfoCorrect = false;
                                    //oldPassError = true;
                                    ErrorDialog oldPass = new ErrorDialog(skin, "Old password does not match your existing password", stage);
                                    oldPassMatchDialog = oldPass.getErrorDialog();
                                }

                            }

                            //successful add to database, user is automatically set to main menu
                        } else {
                            System.out.println("Password must have upper, lower, symbol, and digit");
                            isInfoCorrect = false;
                            //passRegexError = true;
                            ErrorDialog passReg = new ErrorDialog(skin, "New password must have upper, lower, symbol, and digit", stage);
                            newPassRegDialog = passReg.getErrorDialog();
                        }
                    }
                }
                oldPasswordField.setText("");
                newPasswordField.setText("");
                verifyNewPasswordField.setText("");

                if (isInfoCorrect) {
                    //((Game) Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
                    ErrorDialog changedPass = new ErrorDialog("Password Changed", "LoginScreen", game, skin, "Password successfully changed. Please login again", stage);
                    changedPassDialog = changedPass.getErrorDialog();
                }
//                else if (passMatchError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "New passwords do not match", "ChangePasswordScreen"));
//                } else if (oldPassError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Old password does not match your existing password", "ChangePasswordScreen"));
//                }
//                else if (passLengthError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "New password must be at least 8 characters and no more than 16 characters", "ChangePasswordScreen"));
//                } else if (passRegexError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "New password must have upper, lower, symbol, and digit", "ChangePasswordScreen"));
//                }

            }
        });
        verifyNewPassStack.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    isInfoCorrect = true;

                    //game.setScreen(new MainMenuScreen(game));
                    oldPassword = oldPasswordField.getText();
                    newPassword = newPasswordField.getText();
                    verifyNewPassword = verifyNewPasswordField.getText();
                    if (!newPassword.equals(verifyNewPassword)) {
                        isInfoCorrect = false;
                        //passMatchError = true;
                        ErrorDialog passMatch = new ErrorDialog(skin, "New passwords do not match", stage);
                        passMatchDialog = passMatch.getErrorDialog();
                    } else {
                        if (verifyNewPassword.length() < 8 || verifyNewPassword.length() > 16) {
                            System.out.println("Password must be at least 8 characters and no more than 16 characters");
                            isInfoCorrect = false;
                            //passLengthError = true;
                            ErrorDialog passLen = new ErrorDialog(skin, "New password must be at least 8 characters and no more than 16 characters", stage);
                            newPassLenDialog = passLen.getErrorDialog();
                        } else {
                            String regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*., ?]).+$";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(verifyNewPassword);
                            if (m.matches()) {
                                if (isInfoCorrect) {
                                    System.out.println(getCurAcc());
                                    if (db.getPassword(getCurAcc()).equals(oldPassword)) {
                                        db.changePassword(getCurAcc(), newPassword);
                                    } else {
                                        isInfoCorrect = false;
                                        System.out.println("Does not match current password");
                                        //oldPassError = true;
                                        ErrorDialog oldPass = new ErrorDialog(skin, "Old password does not match your existing password", stage);
                                        oldPassMatchDialog = oldPass.getErrorDialog();
                                    }

                                }

                                //successful add to database, user is automatically set to main menu
                            } else {
                                System.out.println("Password must have upper, lower, symbol, and digit");
                                isInfoCorrect = false;
                                //passRegexError = true;
                                ErrorDialog passReg = new ErrorDialog(skin, "New password must have upper, lower, symbol, and digit", stage);
                                newPassRegDialog = passReg.getErrorDialog();
                            }
                        }
                    }
                    oldPasswordField.setText("");
                    newPasswordField.setText("");
                    verifyNewPasswordField.setText("");

                    if (isInfoCorrect) {
                        //((Game) Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
                        ErrorDialog changedPass = new ErrorDialog("Password Changed", "LoginScreen", game, skin, "Password successfully changed. Please login again", stage);
                        changedPassDialog = changedPass.getErrorDialog();
                    }
//                else if (passMatchError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "New passwords do not match", "ChangePasswordScreen"));
//                } else if (oldPassError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "Old password does not match your existing password", "ChangePasswordScreen"));
//                }
//                else if (passLengthError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "New password must be at least 8 characters and no more than 16 characters", "ChangePasswordScreen"));
//                } else if (passRegexError) {
//                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "New password must have upper, lower, symbol, and digit", "ChangePasswordScreen"));
//                }
                }
                return false;
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
}
