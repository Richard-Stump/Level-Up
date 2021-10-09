package com.mygdx.nextlevel;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class Tilemap {
    private ArrayList<Tile> tiles;
    private int[][]         map;
    private int             width;
    private int             height;

    public Tilemap() {
        tiles = new ArrayList<Tile>();

        tiles.add(new Tile("Tile1.png", true));
        tiles.add(new Tile("Tile2.png", true));

        map = new int[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 2, 2, 0, 0, 0 },
                { 0, 0, 0, 2, 2, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
        };
    }

    public void render(Batch batch) {
        for(int y = 0; y < height; y++) {
            for(int x =0;)
        }
    }
}
