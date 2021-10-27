package com.mygdx.nextlevel.screens.editor;

import java.util.ArrayList;

public class EditorLevel {
    public int map[][];
    public int width, height;
    public ArrayList<EditorActor> actors;

    public static int NONE = -1;

    //creates an empty tile map
    public EditorLevel(int width, int height) {
        this.width = width;
        this.height = height;

        map = new int[width][height];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                map[x][y] = NONE;
            }
        }

        this.actors = new ArrayList<EditorActor>();
        actors.add(new EditorActor(0, 0, 0));
    }

    public void setTile(int x, int y, int tileId) {
        map[x][y] = tileId;
    }

    public void placeActor(float x, float y, int actorId) {
        actors.add(new EditorActor(x, y, actorId));
     }
}
