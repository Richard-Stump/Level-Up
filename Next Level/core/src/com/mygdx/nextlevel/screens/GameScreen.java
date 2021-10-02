package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
//import com.mygdx.nextlevel.scenes.Lives;


import java.util.Iterator;
public class GameScreen implements Screen {
//    NextLevel game;
    Stage stage;
    SpriteBatch batch;
    Texture heroImage, enemyImage;
    Rectangle hero, enemy;
    OrthographicCamera camera;
    float heroImagex = 200, heroImagey = 20;
    float enemyImagex = 400, enemyImagey = 20;
//    Viewport viewport;
//    Lives lives;

    World world;
//    private Box2DDebugRenderer debugRenderer;


//    public GameScreen(NextLevel game) {
//        this.game = game;
//        enemyImage = new Texture(Gdx.files.internal("enemy.png"));
//        heroImage = new Texture(Gdx.files.internal("hero.png"));
//        camera = new OrthographicCamera();
////        viewport = new FitViewport(NextLevel.WIDTH,NextLevel.HEIGHT, camera);
////        lives = new Lives(game.batch);
//        camera.setToOrtho(false, 800, 480);
//        enemy = new Rectangle();
//        hero = new Rectangle();
//        hero.x = 200;
//        hero.y = 20;
//        hero.width = 100;
//        hero.height = 100;
//        enemy.x = 600;
//        enemy.y = 20;
//        enemy.width = 100;
//        enemy.height = 100;
//        Body body;
//        world = new World(new Vector2(0,-1f), true);
////        debugRenderer = new Box2DDebugRenderer();
////        BodyDef = new BodyDef();
//        BodyDef bdef = new BodyDef();
//        bdef.type = BodyDef.BodyType.StaticBody;
//        bdef.position.set(enemy.getX(), enemy.getY() );
//        body = world.createBody(bdef);
//        PolygonShape pShape = new PolygonShape();
//        pShape.setAsBox(enemy.getWidth(), enemy.getHeight());
//        FixtureDef fdef = new FixtureDef();
//        fdef.shape = pShape;
////        fdef.filter.categoryBits =
//        body.createFixture(fdef);
//
//    }

//    public void update(float dt) {
//        world.step(1/60f, 6, 2);
//    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1, 0);
        ScreenUtils.clear(1,1,1,0);
        batch.begin();
        stage.draw();
        batch.draw(heroImage, heroImagex, heroImagey, 50, 50);
        batch.draw(enemyImage, 400, 20, 50, 50);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            heroImagex -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            heroImagex += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            heroImagey += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            heroImagey -= 200 * Gdx.graphics.getDeltaTime();
        batch.end();
        hero = new Rectangle(heroImagex, heroImagey, 10,10);
        enemy = new Rectangle(400, 20, 10,10);
        if (hero.overlaps(enemy) && heroImagey > enemyImagey && heroImagex > enemyImagex && heroImagey < enemyImagey) {
            System.out.println("Enemy has been killed.");
        } else if (hero.overlaps(enemy)) {
            System.out.println("Hero has been killed.");
        }
//        camera.update();
//        game.batch.setProjectionMatrix(camera.combined);
//        game.batch.begin();
////        game.font.draw(game.batch, "Lives: " + lives, 0, 480);
//        game.batch.draw(enemyImage, enemy.x, enemy.y, enemy.width, enemy.height);
//        game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
//        game.batch.end();
//
//        if (hero.x < 0)
//            hero.x = 0;
//        if (hero.x > 800 - 64)
//            hero.x = 800 - 64;
//        if (hero.overlaps(enemy)) {
//            game.batch.begin();
//            game.batch.draw(enemyImage, enemy.x, enemy.y, enemy.width, enemy.height);
//            game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
//            game.batch.end();
//        }
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
//        Gdx.gl.glClearColor(0,0,0,1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*debugRenderer.render(world, camera.combined);
        game.batch.setProjectionMatrix(lives.stage.getCamera().combined);
        lives.stage.draw();*/
//        game.batch.begin();
//        game.batch.draw(enemyImage,10,10);
//        game.batch.end();
    }
    @Override
    public void resize(int width, int height) {
//        viewport.update(width, height);
    }

    @Override
    public void show() {
        heroImage = new Texture("hero.png");
        enemyImage = new Texture("enemy.png");
        stage = new Stage();
        hero = new Rectangle(heroImagex, heroImagey, 50,50);
        enemy = new Rectangle(400, 20, 50,50);
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
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
