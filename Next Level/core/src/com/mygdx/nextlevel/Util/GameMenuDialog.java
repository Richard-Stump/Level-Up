package com.mygdx.nextlevel.Util;

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
import sun.rmi.rmic.Main;

public class GameMenuDialog extends GameScreen2 {
    public Dialog menuDialog;
    public String menuMessage;


    private CreatedLevelsDB dbCreated;
    private ServerDBHandler dbHandler;

    public GameMenuDialog(Skin skin, String message, final Stage stage, final String resume, final String restart, String exit, final GameScreen2 screen, final NextLevel game) {

//        this.menuMessage = message;
        this.menuDialog = new Dialog("Game Menu", skin) {
            protected void result(Object object) {
                System.out.println("Option: " + object);
                if (object.equals(1)) {
                    System.out.println("Hide menu");
//                    menuDialog.hide();
                    screen.setPaused(false);
                    System.out.println(screen.getPaused());
                } else if (object.equals(2)) {
                    System.out.println("Restart");
                    screen.setPaused(false);
                    screen.setShouldReset(true);
                } else if (object.equals(3)) {
                    System.out.println("Exit");
                    screen.setPaused(false);
                    game.setScreen(new MainMenuScreen(game));
                }
                else {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            menuDialog.show(stage);
                        }
                    }, 0.5f);
                }
            }
        };

//        menuDialog.text(message);
        menuDialog.button(resume, 1);
        menuDialog.button(restart, 2);
        menuDialog.button(exit, 3);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                menuDialog.show(stage);
            }
        }, 0.1f);
    }
}
