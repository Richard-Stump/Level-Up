package com.mygdx.nextlevel.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.screens.EditLevelScreen;
import com.mygdx.nextlevel.screens.RegisterScreen;

public class EditorLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 960;
        config.height = 500;

        new LwjglApplication(new EditorGame(), config);
    }
}

class EditorGame extends NextLevel {
    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(960, 500, camera);
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
