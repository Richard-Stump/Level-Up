package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TilemapView extends Actor {
    protected ShapeRenderer shapeRenderer;

    protected final Color gridColor = Color.WHITE;

    public TilemapView(int screenWidth, int screenHeight) {
        super();

        setWidth(screenWidth);
        setHeight(screenHeight);

        shapeRenderer = new ShapeRenderer(1000);
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch,parentAlpha);

        setPosition(getX() + 1.0f, getY() + 1.0f);

        //The batch must be shut off before rendering the grid because batches don't
        //play well with shape renderers
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        drawGrid(batch, 32);

        //Reactivate the batch.
        batch.begin();
    }

    protected void drawGrid(Batch batch, int cellSize) {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(0, 0, getWidth(), getHeight());
        shapeRenderer.line(0, getHeight(), getWidth(), 0);

        shapeRenderer.setColor(gridColor);

        for(int x = 0;x < getWidth(); x += cellSize)
            shapeRenderer.line(x, 0, x, getHeight());

        for(int y = 0; y < getWidth(); y += cellSize)
            shapeRenderer.line(0, y, getWidth(), y);

        shapeRenderer.end();
    }
}
