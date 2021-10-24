package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.mygdx.nextlevel.screens.EditLevelScreen;

import java.util.ArrayList;

public class TilemapView extends Widget {
    protected ShapeRenderer shapeRenderer;
    protected Texture tex;
    protected float scale;              //The number of pixels per tile
    protected float originX, originY;   //Where the origin of the tilemap is in screen space
    protected Vector2 panStart;
    protected TestTilemap tilemap;
    private InputListener inputListener;
    private ArrayList<Texture> tiles;
    private int dragButton;

    private EditLevelScreen screen;

    protected Color gridColor = Color.WHITE;

    public TilemapView(EditLevelScreen screen, TestTilemap map, int screenWidth, int screenHeight) {
        this(screenWidth, screenHeight);

        this.screen = screen;
        this.tiles = screen.getTiles();

        tilemap = map;

        tilemap.map[0][0] = 0;
        tilemap.map[tilemap.width - 1][tilemap.height - 1] = 0;

        originX = 20;
        originY = 8;

        panStart = new Vector2(0.0f,0.0f);
    }

    public TilemapView(int screenWidth, int screenHeight) {
        super();

        setWidth(screenWidth);
        setHeight(screenHeight);

        shapeRenderer = new ShapeRenderer(1000);
        tex = new Texture("block.png");

        setPosition(0,0);
        setBounds(0, 0, screenWidth, screenHeight);

        scale = 32.0f;
        originX = 0.0f;
        originY = 0.0f;

        inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragButton = button;

                if(button == Input.Buttons.RIGHT) {
                    // We need to keep track of where the mouse started so we can keep track of how far
                    // the user has panned
                    panStart.set(x, y);
                    return true;
                }
                else if (button == Input.Buttons.LEFT) {
                    Vector2 tilePos = screenToWorld(x, y);
                    placeCurrentTileSelection((int) tilePos.x, (int) tilePos.y);

                    return true;
                }

                return false;
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.RIGHT) {
                    //move the origin by the amount that mouse has moved since the last update
                    originX += x - panStart.x;
                    originY += y - panStart.y;

                    //update the start point
                    panStart.set(x, y);
                }
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(dragButton == Input.Buttons.RIGHT) {
                    //move the origin by the amount that mouse has moved since the last update
                    originX += x - panStart.x;
                    originY += y - panStart.y;

                    //update the start point
                    panStart.set(x, y);
                }
                else if(dragButton == Input.Buttons.LEFT) {
                    Vector2 tilePos = screenToWorld(x, y);
                    placeCurrentTileSelection((int) tilePos.x, (int) tilePos.y);
                }
            }

            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                Vector2 start = screenToWorld(x, y);

                scale -= (amountY / 10.0f * scale);

                // clamp the scale to reasonable bounds.
                // If the scale goes to 1 pixel per tile, the user will just see the grid
                scale = Math.max(scale, 16.0f);
                scale = Math.min(scale, 100.0f);

                //update the origin point so that we zoom into the mouse position
                Vector2 end = screenToWorld(x, y);

                Vector2 worldDelta = start.sub(end); // I miss c++ operator overloading ;_;

                // Can't use the worldToScreen() method here because delta is a vector, not a point.
                // The method adds the origin coordinates to it, which we don't want.
                originX -= worldDelta.x * scale;
                originY -= worldDelta.y * scale;

                return true;
            }
        };

        addListener(inputListener);
    }

    // These methods are used to perform coordinate conversions. Screen coordinates originate from the bottom
    // left corner of the screen, up is +y, right is +x. World coordinates originate from the bottom left
    // corner of the the tilemap, up is +y, right is +x.
    // world coordinates must be converted to screen for rendering, and then mouse input must be converted
    // from screen coordinates to world coordinates.
    public Vector2 screenToWorld(Vector2 screenCoord) {
        return screenToWorld(screenCoord.x, screenCoord.y);
    }

    public Vector2 screenToWorld(float x, float y) {
        return new Vector2((x - originX) / scale, (y - originY) / scale);
    }

    public Vector2 worldToScreen(Vector2 worldCoord) {
        return worldToScreen(worldCoord.x, worldCoord.y);
    }

    public Vector2 worldToScreen(float x, float y) {
        return new Vector2(x * scale + originX, y * scale + originY);
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch,parentAlpha);

        drawTiles(batch);

        //The batch must be shut off before rendering the grid because batches don't
        //play well with shape renderers
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        drawGrid();

        //Reactivate the batch.
        batch.begin();
    }

    protected void drawGrid() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(gridColor);

        //Convert the edges of the tilemap into screen space coordinates
        float leftX   = originX;
        float rightX  = (originX + tilemap.width * scale + 1);
        float bottomY = originY;
        float topY    = (originY + tilemap.height * scale + 1);

        for(float x = leftX; x <= rightX; x += scale) {
            shapeRenderer.line(x, bottomY, x, topY);
        }

        for(float y = bottomY; y <= topY; y += scale) {
            shapeRenderer.line(leftX, y, rightX, y);
        }

        shapeRenderer.end();
    }

    protected void drawTiles(Batch batch) {
        for(int yi = 0; yi < tilemap.height; yi++) {
            for(int xi = 0; xi < tilemap.width; xi++) {
                int tileNumber = tilemap.map[xi][yi];

                if(tileNumber != TestTilemap.NONE) {
                    Vector2 tileScreenPos = worldToScreen(xi, yi);

                    Texture tex = tiles.get(tileNumber);

                    batch.draw(tex, tileScreenPos.x, tileScreenPos.y, scale, scale);
                }
            }
        }
    }

    protected void placeCurrentTileSelection(int x, int y) {
        AssetSelectorWindow selWin = screen.getSelectorWindow();

        if(selWin.getCurrentTabTitle().equals("Tiles")) {
            int index = selWin.getSelectionIndex();

            tilemap.map[x][y] = index;
        }
    }
}

