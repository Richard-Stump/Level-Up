package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionManager;
import com.mygdx.nextlevel.NextLevel;

public class GameScreen2 implements Screen, InputProcessor {
    private NextLevel game;
    private Box2DDebugRenderer box2dRenderer;
    private OrthographicCamera camera;

    private Vector2 b1Vel;
    private BoxCollider b1, b2;

    public GameScreen2() {
        CollisionManager.init();

        box2dRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        b1 = new BoxCollider(new Vector2(0, 0), new Vector2(1, 1), true);
        b2 = new BoxCollider(new Vector2(0, -3), new Vector2(1, 1), false);
        b1Vel = new Vector2(Vector2.Zero);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        final float speed = 1.0f;

        if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            b1Vel.add(-speed, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            b1Vel.add(speed, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Keys.UP)) {
            b1Vel.add(0.0f, speed);
        }
        if(Gdx.input.isKeyPressed(Keys.DOWN)) {
            b1Vel.add(0.0f, -speed);
        }

        b1.setVelocity(b1Vel);
        b1Vel.set(Vector2.Zero);

        CollisionManager.getWorld().step(delta, 9, 3);

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
