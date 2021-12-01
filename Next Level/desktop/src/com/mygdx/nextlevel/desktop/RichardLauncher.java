package com.mygdx.nextlevel.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.EditLevelScreen;
import com.mygdx.nextlevel.screens.LoginScreen;
import com.mygdx.nextlevel.screens.MainMenuScreen;

/**
 * This allows me to bypass the login for testing.
 */
public class RichardLauncher {
    public static void main (String[] arg) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 960;
        config.height = 500;
        ServerDBHandler db = new ServerDBHandler();

        if (db.isDBActive()) {
            new LwjglApplication(new RichardGame(), config);
        } else {
            System.out.println();
            System.out.println("Please connect to the internet");
        }
    }
}

class RichardGame extends NextLevel {@Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(960, 500, camera);

        LoginScreen.curAcc = "stump";
        MainMenuScreen screen = new MainMenuScreen(this);

        this.setScreen(screen);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
        font.dispose();
    }

}
