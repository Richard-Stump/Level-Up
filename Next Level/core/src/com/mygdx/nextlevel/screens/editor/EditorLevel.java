package com.mygdx.nextlevel.screens.editor;

import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;

import java.util.ArrayList;

public class EditorLevel {
    public int map[][];
    public int width, height;
    public ArrayList<EditorActor> actors;

    public String           name;
    public Difficulty       difficulty = Difficulty.NONE;
    public ArrayList<Tag>   tags;

    public static int NONE = -1;

    //creates an empty tile map
    public EditorLevel(int width, int height) {
        this.width = width;
        this.height = height;
        this.name = null;
        this.difficulty = null;
        this.tags = null;

        map = new int[width][height];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                map[x][y] = NONE;
            }
        }

        this.actors = new ArrayList<EditorActor>();
    }

    public EditorLevel(String name, int width, int height) {
        this(width, height);

        this.name = new String(name);
    }

    public void setTile(int x, int y, int tileId) {
        map[x][y] = tileId;
    }

    public void placeActor(float x, float y, int actorId) {
        actors.add(new EditorActor(x, y, actorId));
     }

    public void resize(int width, int height) {
        int[][] newMap = new int[width][height];

        //clear the new map
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(x < this.width && y < this.height)
                    newMap[x][y] = map[x][y];
                else
                    newMap[x][y] = NONE;
            }
        }

        this.width = width;
        this.height = height;
        this.map = newMap;
    }
}
