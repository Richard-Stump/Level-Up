package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Null;

public class TilemapView extends Actor {
    protected ShapeRenderer shapeRenderer;
    protected Texture tex;
    protected float scale;              //The number of pixels per tile
    protected float originX, originY;
    protected float lastX, lastY;
    protected TestTilemap tilemap;
    private int count = 0;
    private InputListener inputListener;

    protected final Color gridColor = Color.WHITE;

    public TilemapView(TestTilemap map, int screenWidth, int screenHeight) {
        this(screenWidth, screenHeight);

        tilemap = map;

        tilemap.map[0][0] = 0;
        tilemap.map[tilemap.width - 1][tilemap.height - 1] = 0;

        originX = 20;
        originY = 8;
    }

    public TilemapView(int screenWidth, int screenHeight) {
        super();

        setWidth(screenWidth);
        setHeight(screenHeight);

        shapeRenderer = new ShapeRenderer(1000);
        tex = new Texture("block.png");

        scale = 32.0f;
        originX = 0.0f;
        originY = 0.0f;

        inputListener = new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                System.out.println("enter " + (count));
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                System.out.println("exit " + (++count));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int tileX = (int)((x / scale) - originX);
                int tileY = (int)((y / scale) - originY);

                System.out.println("Clicked at tile (" + tileX + "," + tileY + ")");

                return true;
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                int tileX = (int)((x / scale) - originX);
                int tileY = (int)((y / scale) - originY);

                System.out.println("un-clicked at tile (" + tileX + "," + tileY + ")");
            }
        };

        addListener(inputListener);
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch,parentAlpha);

        drawTiles(batch, (int)scale);

        setPosition(getX() + 1.0f, getY() + 1.0f);

        //The batch must be shut off before rendering the grid because batches don't
        //play well with shape renderers
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        drawGrid((int)scale);

        //Reactivate the batch.
        batch.begin();
    }

    protected void drawGrid(int cellSize) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(gridColor);

        int leftX = (int)((originX) * scale);
        int rightX = (int)((originX + tilemap.width) * scale);
        int bottomY = (int)((originY) * scale);
        int topY = (int)((originY + tilemap.height) * scale);

        for(int x = leftX; x <= rightX; x += cellSize) {
            shapeRenderer.line(x, bottomY, x, topY);
        }

        for(int y = bottomY; y <= topY; y += cellSize) {
            shapeRenderer.line(leftX, y, rightX, y);
        }

        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.line(0, 0, getWidth(), getHeight());
        shapeRenderer.line(0, getHeight(), getWidth(), 0);

        shapeRenderer.end();
    }

    protected void drawTiles(Batch batch, int cellSize) {
        for(int yi = 0; yi < tilemap.height; yi++) {
            for(int xi = 0; xi < tilemap.width; xi++) {
                if(tilemap.map[xi][yi] != TestTilemap.NONE) {
                    float x = (originX + xi) * scale;
                    float y = (originY + yi) * scale;

                    batch.draw(tex, x, y, cellSize, cellSize);
                }
            }
        }
    }

    public void pan(float x, float y) {
        originX += x;
        originY += y;
    }
}

