package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.scenes.Lives;


import java.util.Iterator;
public class GameScreen implements Screen {
    NextLevel game;
    Texture heroImage;
    Texture enemyImage;
    Rectangle hero;
    Rectangle enemy;
    OrthographicCamera camera;
    Viewport viewport;
    Lives lives;

    private World world;
    private Box2DDebugRenderer debugRenderer;


    public GameScreen(NextLevel game) {
        this.game = game;
        enemyImage = new Texture(Gdx.files.internal("boss-sprite.jpeg"));
        heroImage = new Texture(Gdx.files.internal("hero.png"));
        camera = new OrthographicCamera();
        viewport = new FitViewport(NextLevel.WIDTH,NextLevel.HEIGHT, camera);
        lives = new Lives(game.batch);
        camera.setToOrtho(false, 800, 480);
        enemy = new Rectangle();
        hero = new Rectangle();
        hero.x = 200;
        hero.y = 20;
        hero.width = 64;
        hero.height = 64;
        enemy.x = 400;
        enemy.y = 20;
        enemy.width = 15;
        enemy.height = 15;

        world = new World(new Vector2(0,0), true);
        debugRenderer = new Box2DDebugRenderer();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(enemy.getX(), enemy.getY() );
        body = world.createBody(bdef);
        shape.setAsBox(enemy.getWidth(), enemy.getHeight());
        fdef.shape = shape;
        body.createFixture(fdef);

    }

    public void update(float dt) {
        world.step(1/60f, 6, 2);
    }

    @Override
    public void render(float delta) {
//        ScreenUtils.clear(0, 0, 0.2f, 1);
//        camera.update();
//        game.batch.setProjectionMatrix(camera.combined);
//        game.batch.begin();
//        game.font.draw(game.batch, "Lives: " + lives, 0, 480);
//        game.batch.draw(enemyImage, enemy.x, enemy.y, enemy.width, enemy.height);
//        game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
//        game.batch.end();
//
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
//            hero.x -= 200 * Gdx.graphics.getDeltaTime();
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//            hero.x += 200 * Gdx.graphics.getDeltaTime();
//        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
//            hero.y += 800 * Gdx.graphics.getDeltaTime();
////        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
////            hero.y -= 400 * Gdx.graphics.getDeltaTime();
//        if (hero.x < 0)
//            hero.x = 0;
//        if (hero.x > 800 - 64)
//            hero.x = 800 - 64;
//        if (hero.overlaps(enemy) && hero.y > enemy.y) {
//            game.batch.begin();
//            hero.x = 10;
//            hero.y = 20;
//            hero.width = 100;
//            hero.height = 100;
//            enemy.x = 500;
//            enemy.y = 20;
//            enemy.width = 64;
//            enemy.height = 64;
//            game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
//            game.batch.draw(enemyImage, enemy.x, enemy.y, enemy.width, enemy.height);
//
//            game.batch.end();
//
//        } else if (hero.overlaps(enemy)) {
//            lives--;
//            game.batch.begin();
//            hero.x = 10;
//            hero.y = 20;
//            hero.width = 100;
//            hero.height = 100;
//            game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
//            game.batch.end();
//        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, camera.combined);
        game.batch.setProjectionMatrix(lives.stage.getCamera().combined);
        lives.stage.draw();
        game.batch.begin();
        game.batch.draw(enemyImage,10,10);
        game.batch.end();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
