package com.mygdx.nextlevel.Util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.*;

public class ErrorDialog {

    public Dialog errorDialog;
    public String errorMessage;

    private CreatedLevelsDB dbCreated;
    private ServerDBHandler dbHandler;

    public ErrorDialog(Skin skin, String message, final Stage stage, String buttonOpLeft, final String buttonOpRight,
                       final String id, final TextButton button, final LevelInfo levelInfo) {
        dbCreated = new CreatedLevelsDB();
        dbHandler = new ServerDBHandler();

        this.errorMessage = message;
        this.errorDialog = new Dialog("Publish Level", skin) {
            protected void result(Object object) {
                System.out.println("Option: " + object);
                if (object.equals(1)) {
                    errorDialog.hide();
                } else if (object.equals(2)) {
                    System.out.println("obj = 2");
                    if ((buttonOpRight.compareTo("Publish")) == 0) {
                        dbHandler.publishLevel(id);
                        button.setText("Unpublish");
                        levelInfo.setPublic(true);
                    } else {
                        levelInfo.setPlayCount(0);
                        levelInfo.setRating(0);
                        int res = dbHandler.updateLevel(levelInfo);
                        //System.out.println("This is the result of update level: " + res);
                        dbHandler.unpublishLevel(id);
                        //System.out.println("Current Rating count: " + levelInfo.getPlayCount());
//                        levelInfo.setPlayCount(0);
//                        dbHandler.updateLevel(levelInfo);
                        button.setText("Publish");
                        levelInfo.setPublic(false);
                    }
                } else {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            errorDialog.show(stage);
                        }
                    }, 0.5f);
                }
            }
        };

        errorDialog.text(message);
        errorDialog.button(buttonOpLeft, 1);
        errorDialog.button(buttonOpRight, 2);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                errorDialog.show(stage);
            }
        }, 0.1f);
    }

    public ErrorDialog(Skin skin, String message, final Stage stage) {
        this.errorMessage = message;
        this.errorDialog = new Dialog("Error", skin) {
            protected void result(Object object) {
                System.out.println("Option: " + object);
                if ((Boolean) object) {
                    errorDialog.hide();
                } else {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            errorDialog.show(stage);
                        }
                    }, 0.5f);
                }
            }
        };

        errorDialog.text(message);
        errorDialog.button("Close", true);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                errorDialog.show(stage);
            }
        }, 0.1f);
    }

    public ErrorDialog(String title, final String nextScreen, final NextLevel game, Skin skin, String message, final Stage stage) {
        this.errorMessage = message;

        this.errorDialog = new Dialog(title, skin) {
            protected void result(Object object) {
                System.out.println("Option: " + object);
                if ((Boolean) object) {
                    switch (nextScreen) {
                        case("MainMenuScreen"):
                            game.setScreen(new MainMenuScreen(game));
                            break;
                        case("RegisterScreen"):
                            game.setScreen(new RegisterScreen(game));
                            break;
                        case("LoginScreen"):
                            game.setScreen(new LoginScreen(game));
                            break;
                        case("ForgetPasswordScreen"):
                            game.setScreen(new ForgetPasswordScreen(game));
                            break;
                        case("ChangePasswordScreen"):
                            game.setScreen(new ChangePasswordScreen(game));
                            break;
                        case("ProfileMainMenu"):
                            game.setScreen(new ProfileMainMenu(game));
                            break;
                        case("LevelDownloadScreen"):
                            game.setScreen(new LevelDownloadScreen(game));
                            break;
                        default:
                            break;
                        //errorDialog.hide();
                    }
                } else {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            errorDialog.show(stage);
                        }
                    }, 0.5f);
                }
            }
        };

        errorDialog.text(message);
        errorDialog.button("Close", true);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                errorDialog.show(stage);
            }
        }, 0.1f);
    }

    public Dialog getErrorDialog() {
        return errorDialog;
    }
}
