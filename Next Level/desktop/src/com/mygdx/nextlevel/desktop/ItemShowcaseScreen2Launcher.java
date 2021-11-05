package com.mygdx.nextlevel.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.screens.EditLevelScreen;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.ItemShowcaseScreen2;

public class ItemShowcaseScreen2Launcher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 960;
        config.height = 500;

        new LwjglApplication(new ItemShowcaseScreen2Game(), config);
    }
}

class ItemShowcaseScreen2Game extends NextLevel {
    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(960, 500, camera);

        ItemShowcaseScreen2 screen = new ItemShowcaseScreen2(this);

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