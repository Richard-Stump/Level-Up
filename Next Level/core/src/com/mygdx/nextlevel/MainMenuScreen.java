package com.mygdx.nextlevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final NextLevel game;
    OrthographicCamera camera;
    public MainMenuScreen(final NextLevel game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 480);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Next Level", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to play!", 100, 100);
        game.batch.end();
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

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
