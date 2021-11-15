package com.mygdx.nextlevel.Util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.screens.*;

public class ErrorDialog {

    public Dialog errorDialog;
    public String errorMessage;

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
