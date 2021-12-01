package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EditorLevel {
    //This class represents and object that has been placed in the level's map
    private class PlacedObject<T> {
        PlaceableObject placeableObject;
        T               object;

        public PlacedObject(PlaceableObject po, T o) {
            this.placeableObject = po;
            this.object = o;
        }
    }

    protected PlacedObject objects[][];

    public String           saveName;
    public ArrayList<Tag>   tags;

    private int oldWidth, oldHeight;

    public LevelInfo info;

    @Property(displayName="Level Width") public int width;
    @Property(displayName="Level Height") public int height;
    @Property(displayName="Level Name") public String name;
    @Property(displayName="Difficulty") public Difficulty difficulty = Difficulty.NONE;
    @Property(displayName="Gravity") public float gravity;
    @Property(displayName="Time Limit") public int timeLimit;
    @Property(displayName="Auto Scroll") public boolean autoScroll;

    @Property(group="Completion Flags", displayName="Collect All Coins") public boolean collectCoins;
    @Property(group="Completion Flags", displayName="Beat The Time Limit") public boolean beatTimeLimit;
    @Property(group="Completion Flags", displayName="Kill All Enemies") public boolean killAllEnemies;
    @Property(group="Completion Flags", displayName="Kill No Enemies") public boolean killNoEnemies;
    @Property(group="Completion Flags", displayName="Keep The Jewel Safe") public boolean keepJewel;



    public static int NONE = -1;

    //creates an empty tile map
    public EditorLevel(int width, int height) {
        this.width = width;
        this.height = height;
        this.oldWidth = width;
        this.oldHeight = height;
        this.name = null;
        this.difficulty = null;
        this.tags = null;

        objects = new PlacedObject[width][height];
    }

    public EditorLevel(String name, int width, int height) {
        this(width, height);

        this.name = name;
    }

    public void placeObject(int x, int y, PlaceableObject po, Object o) {
        objects[x][y] = new PlacedObject(po, po.clazz.cast(o));
    }

    public void updateFromProperties() {
        resize(width, height);
    }

    public Texture getTexture(int x, int y) {
        if(objects[x][y] != null)
            return objects[x][y].placeableObject.texture;
        else
            return null;
    }

    public void resize(int width, int height) {
        PlacedObject[][] newObjects = new PlacedObject[width][height];

        //clear the new map
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(x < this.oldWidth && y < this.oldHeight) {
                    newObjects[x][y] = objects[x][y];
                }
            }
        }

        this.oldWidth = width;
        this.oldHeight = height;
        this.width = width;
        this.height = height;
        this.objects = newObjects;
    }

    /*
     * Tiledmap format:
     *  - "Tile Layer #":   stores the tiles
     *  - "Object Layer #"  Stores the object
     */

    public File exportTo(String filename) throws FileNotFoundException {
        File file = new File(filename);

        PrintWriter fileWriter = new PrintWriter(file);

        fileWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        fileWriter.print("<map version=\"1.2\" tiledversion=\"1.3.4\" orientation=\"orthogonal\"");
        fileWriter.print(" renderorder=\"right-down\" ");
        fileWriter.print("width=\"" + Integer.toString(width) + "\" ");
        fileWriter.print("height=\"" + Integer.toString(height) + "\" ");
        fileWriter.println("tilewidth=\"32\" tileheight=\"32\" infinite=\"0\">");
        fileWriter.println("<tileset firstgid=\"1\" source=\"test2.tsx\"/>");
        fileWriter.println("<properties>");

        fileWriter.println("<property name=\"collectCoins\" type=\"bool\" value=\"" + collectCoins + "\"/>");
        fileWriter.println("<property name=\"beatTimeLimit\" type=\"bool\" value=\"" + beatTimeLimit + "\"/>");
        fileWriter.println("<property name=\"killAllEnemies\" type=\"bool\" value=\"" + killAllEnemies + "\"/>");
        fileWriter.println("<property name=\"killNoEnemies\" type=\"bool\" value=\"" + killNoEnemies + "\"/>");
        fileWriter.println("<property name=\"keepJewel\" type=\"bool\" value=\"" + keepJewel + "\"/>");
        fileWriter.println("<property name=\"gravity\" type=\"float\" value=\"" + gravity + "\"/>");
        fileWriter.println("<property name=\"timeLimit\" type=\"float\" value=\"" + timeLimit + "\"/>");
        fileWriter.println("<property name=\"autoScroll\" type=\"float\" value=\"" + autoScroll + "\"/>");

        fileWriter.println("</properties>");
        fileWriter.print("<layer id=\"1\" name=\"Tile Layer 1\" ");
        fileWriter.print("width=\"" + Integer.toString(width) + "\" ");
        fileWriter.println("height=\"" + Integer.toString(height) + "\">");
        fileWriter.println("<data encoding=\"csv\">");

        for(int y = height - 1; y >= 0; y--) {
            for(int x = 0; x < width; x++) {
            }

            fileWriter.println();
        }

        fileWriter.println("</data>");
        fileWriter.println("</layer>");
        fileWriter.println("</map>");

        fileWriter.flush();
        return file;
    }

    public void importFrom(File file) {
        importFrom(file.getName());
    }

    public void importFrom(String filename) {
        TiledMap tiledMap = new TmxMapLoader().load(filename);
        MapProperties tiledMapProperties = tiledMap.getProperties();
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);

        width = tiledMapProperties.get("width", Integer.class);
        height = tiledMapProperties.get("height", Integer.class);
        name = filename.substring(0, filename.lastIndexOf(".tmx"));

        this.width = width;
        this.height = height;
        this.oldWidth = width;
        this.oldHeight = height;

        for(int y = height - 1; y >= 0; y--) {
            for(int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
            }
        }

        return;
    }
}
