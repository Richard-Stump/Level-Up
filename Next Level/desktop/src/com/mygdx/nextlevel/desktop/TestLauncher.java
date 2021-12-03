package com.mygdx.nextlevel.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.nextlevel.TestRunner;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

public class TestLauncher {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        ServerDBHandler db = new ServerDBHandler();
        if (db.isDBActive()) {
            new LwjglApplication(new Listener(args), config);
        } else {
            System.out.println();
            System.out.println("Please connect to the internet");
        }
    }
}

class Listener implements ApplicationListener {
    String[] args;

    public Listener(String[] args) {
        this.args = args;
    }

    @Override
    public void create() {
        TestRunner.main(args);
        Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}