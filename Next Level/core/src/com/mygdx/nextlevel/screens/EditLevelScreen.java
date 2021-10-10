package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.nextlevel.NextLevel;
import com.kotcrab.*;

public class EditLevelScreen implements Screen, InputProcessor {
    private NextLevel game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private int screenWidth, screenHeight;
    private float pixelsPerTile;

    private final Color backgroundColor = new Color(0.9f, 0.9f, 0.9f,1.0f);
    private final Color gridColor = new Color(0.2f, 0.2f, 0.2f,1.0f);

    public EditLevelScreen(NextLevel game) {
        this.game = game;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera(screenWidth, screenHeight);
        this.shapeRenderer = new ShapeRenderer(1000);

        pixelsPerTile = 64.0f;
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(backgroundColor);
        camera.zoom = 20.0f;

        renderGrid();
    }

    private void renderGrid() {
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(gridColor);

        for(int y = 0; y <= (float)screenHeight; y++) {  
            shapeRenderer.line(0, y, screenWidth, y);
        }

        for(int x = 0; x <= (float)screenHeight; x++) {
            shapeRenderer.line(x, 0, x, screenHeight);
        }

        shapeRenderer.end();
    }


    @Override
    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        camera.viewportWidth = width;
        camera.viewportHeight = height;
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
        return false;
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
