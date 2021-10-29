package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionManager;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.Actor;
import com.mygdx.nextlevel.actors.Player;
import com.mygdx.nextlevel.actors.*;

import java.util.ArrayList;

/**
 * Temporary game screen to use while refactoring code.
 *
 * One big change to note with this part of the code: Box2D calculations now use 1 pixel per meter, and the camera
 * is instead zoomed so that each unit is a tile. This was done to fix Box2D's velocity cap, which kept bodies
 * from moving too fast. This makes the math for placing enemies and everything easier though because a change of
 * 1.0f in position moves exactly one time. No more PixelsPerMeter calculations.
 */
public class GameScreen2 implements Screen, InputProcessor {
    private NextLevel game;
    private Box2DDebugRenderer box2dRenderer;
    private OrthographicCamera camera;

    private Vector2 b1Vel;
    private BoxCollider b1, b2, b3;

    ArrayList<Actor2> actors;

    public GameScreen2(NextLevel game) {
        this.game = game;

        CollisionManager.init();

        box2dRenderer = new Box2DDebugRenderer();

        float aspect = (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
        float numTilesVisibleY = 15.0f;
        camera = new OrthographicCamera(numTilesVisibleY * aspect, numTilesVisibleY);
        camera.translate(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f);
        camera.update();

        b2 = new BoxCollider(new Vector2(15, 0), new Vector2(30, 1), false);
        b2.debugPrint = false;
        b1Vel = new Vector2(Vector2.Zero);

        actors = new ArrayList<>();
        actors.add(new Player2(7,2));
        actors.add(new Enemy2(5, 2));
        actors.add(new Block2(7, 4, true));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    private void update(float delta) {
        //I think higher iteration constants decreases the chances for side detection failure.
        CollisionManager.getWorld().step(delta, 18, 9);

        for(Actor2 a : actors) {
            a.update(delta);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.BLACK);

        Batch batch = game.batch;
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for(Actor2 a : actors) {
            a.draw(batch);
        }

        batch.end();

        box2dRenderer.render(CollisionManager.getWorld(), camera.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
