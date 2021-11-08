package com.mygdx.nextlevel.Util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;

public class ErrorDialog {
//    private Skin skin;
//    private TextureAtlas atlas;
    private Stage stage;

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

    public Dialog getErrorDialog() {
        return errorDialog;
    }
}
