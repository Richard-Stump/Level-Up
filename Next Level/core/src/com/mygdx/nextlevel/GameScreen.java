package com.mygdx.nextlevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final NextLevel game;
    Texture heroImage;
    Texture enemyImage;
    Rectangle hero;
    Rectangle enemy;
    OrthographicCamera camera;
    int lives = 3;

    public GameScreen(final NextLevel game) {
        this.game = game;
        enemyImage = new Texture(Gdx.files.internal("boss-sprite.jpeg"));
        heroImage = new Texture(Gdx.files.internal("hero.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        enemy = new Rectangle();
        hero = new Rectangle();
        hero.x = 200;
        hero.y = 20;
        hero.width = 64;
        hero.height = 64;
        enemy.x = 800/2 - 64/2;
        enemy.y = 20;
        enemy.width = 64;
        enemy.height = 64;
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Lives: " + lives, 0, 480);
        game.batch.draw(enemyImage, enemy.x, enemy.y, enemy.width, enemy.height);
        game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            hero.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            hero.x += 200 * Gdx.graphics.getDeltaTime();
        if (hero.x < 0)
            hero.x = 0;
        if (hero.x > 800 - 64)
            hero.x = 800 - 64;
        if (hero.overlaps(enemy)) {
            lives--;
            game.batch.begin();
            hero.x = 10;
            hero.y = 20;
            hero.width = 100;
            hero.height = 100;
            game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
            game.batch.end();
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
        enemyImage.dispose();
        heroImage.dispose();

    }
}
