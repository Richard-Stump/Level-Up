package com.mygdx.nextlevel.screens.editor;

public class TestTilemap {
    public int map[][];
    public int width, height;

    public static int NONE = -1;

    //creates an empty tile map
    public TestTilemap(int width, int height) {
        this.width = width;
        this.height = height;

        map = new int[width][height];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                map[x][y] = NONE;
            }
        }
    }
}
