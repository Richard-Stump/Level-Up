package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.actors.Player2;
import com.mygdx.nextlevel.enums.BackgroundColor;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;
import com.mygdx.nextlevel.jankFix.TmxMapLoader2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

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

    protected HashMap<String, PlaceableObject> placeableObjects;
    protected PlacedObject objects[][];

    protected int lastPlayerX = -1;
    protected int lastPlayerY = -1;

    public String           saveName;
    public ArrayList<Tag>   tags;

    private int oldWidth, oldHeight;

    public LevelInfo info;

    @Property(displayName="Level Width") public int width;
    @Property(displayName="Level Height") public int height;
    @Property(displayName="Difficulty") public Difficulty difficulty = Difficulty.MEDIUM;
    @Property(displayName="Gravity") public float gravity = -9.81f;
    @Property(displayName="Time Limit") public int timeLimit = 120;
    @Property(displayName="Auto Scroll") public boolean autoScroll = false;
    @Property(displayName="Background Color") public BackgroundColor backgroundColor = BackgroundColor.BLUE;

    @Property(group="Completion Flags", displayName="Collect All Coins") public boolean collectCoins = false;
    @Property(group="Completion Flags", displayName="Beat The Time Limit") public boolean beatTimeLimit = true;
    @Property(group="Completion Flags", displayName="Kill All Enemies") public boolean killAllEnemies = false;
    @Property(group="Completion Flags", displayName="Kill No Enemies") public boolean killNoEnemies = false;
    @Property(group="Completion Flags", displayName="Keep The Jewel Safe") public boolean keepJewel = false;

    //creates an empty tile map
    public EditorLevel(int width, int height) {
        this.width = width;
        this.height = height;
        this.oldWidth = width;
        this.oldHeight = height;
        this.difficulty = null;
        this.tags = null;

        objects = new PlacedObject[width][height];
    }

    public EditorLevel(String name, int width, int height) {
        this(width, height);
    }

    public void setPlaceableObjects(ArrayList<PlaceableObject> placeableObjects) {
        this.placeableObjects = new HashMap<>();

        for(PlaceableObject po : placeableObjects) {
            this.placeableObjects.put(po.clazz.getSimpleName(), po);
        }
    }

    public void placeObject(int x, int y, PlaceableObject po, Object o) {
        //Ensure that there is only one player in the level
        if(o instanceof Player2) {
            if(lastPlayerX != -1 && lastPlayerY != -1) {
                objects[lastPlayerX][lastPlayerY] = null;
            }

            lastPlayerX = x;
            lastPlayerY = y;
        }

        objects[x][y] = new PlacedObject(po, po.clazz.cast(o));
    }

    public void clearObject(int x, int y) {
        //Ensure that there is only one player in the level
        if(x == lastPlayerX && y == lastPlayerY) {
            lastPlayerX = -1;
            lastPlayerY = -1;
        }

        objects[x][y] = null;
    }

    /**
     * This updates the level data based on how the properties in the class are set
     */
    public void updateFromProperties() {
        resize(width, height);

        info.setDifficulty(difficulty.ordinal());
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

    /**
     * Export the map to a given file in the TMX file format
     * @param filename Name of the file to write to
     * @return         The file object used to write the file
     * @throws FileNotFoundException
     */
    public File exportTo(String filename) throws FileNotFoundException {
        File file = new File(filename);
        System.out.println("Exporting to \"" + filename + "\"");


        PrintWriter fileWriter = new PrintWriter(file);

        fileWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        fileWriter.print("<map version=\"1.2\" tiledversion=\"1.3.4\" orientation=\"orthogonal\"");
        fileWriter.print(" renderorder=\"right-up\" ");
        fileWriter.print("width=\"" + Integer.toString(width) + "\" ");
        fileWriter.print("height=\"" + Integer.toString(height) + "\" ");
        fileWriter.println("tilewidth=\"1\" tileheight=\"1\" infinite=\"0\">");
        fileWriter.println(" <tileset firstgid=\"1\" source=\"test2.tsx\"/>");

        //Write all of the properties that belong to the actual map itself. Not every property needs to be written,
        //So this has to be done manually.
        fileWriter.println(" <properties>");
        fileWriter.println("  <property name=\"collectCoins\" type=\"bool\" value=\"" + collectCoins + "\"/>");
        fileWriter.println("  <property name=\"beatTimeLimit\" type=\"bool\" value=\"" + beatTimeLimit + "\"/>");
        fileWriter.println("  <property name=\"killAllEnemies\" type=\"bool\" value=\"" + killAllEnemies + "\"/>");
        fileWriter.println("  <property name=\"killNoEnemies\" type=\"bool\" value=\"" + killNoEnemies + "\"/>");
        fileWriter.println("  <property name=\"keepJewel\" type=\"bool\" value=\"" + keepJewel + "\"/>");
        fileWriter.println("  <property name=\"gravity\" type=\"float\" value=\"" + gravity + "\"/>");
        fileWriter.println("  <property name=\"timeLimit\" type=\"int\" value=\"" + timeLimit + "\"/>");
        fileWriter.println("  <property name=\"autoScroll\" type=\"bool\" value=\"" + autoScroll + "\"/>");
        fileWriter.println("  <property name=\"backgroundColor\" type=\"string\" value=\"" + backgroundColor.toString() + "\"/>");
        fileWriter.println(" </properties>");

        //Player must be written first
        writePlayer(fileWriter);
        writeObjects(fileWriter);

        fileWriter.println("</map>");

        fileWriter.flush();

        return file;
    }

    private void writePlayer(PrintWriter fileWriter) {
        fileWriter.println(" <objectgroup id=\"2\" name=\"Player Layer\">");

        //Ensure that the player exists in the level
        if(lastPlayerX != -1 && lastPlayerY != -1)
            writeObject(lastPlayerX, lastPlayerY, 1, fileWriter, objects[lastPlayerX][lastPlayerY]);

        fileWriter.println(" </objectgroup>");
    }

    /**
     * Write all the objects in the tilemap to the TMX file, except the player
     * @param fileWriter The PrintWriter to use to write the data
     */
    private void writeObjects(PrintWriter fileWriter) {
        fileWriter.println(" <objectgroup id=\"3\" name=\"Object Layer 1\">");

        //Loop through each of the placed objects in the map, and if they're not null, write them to the file
        //Give each object a unique id.
        int id = 1;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                PlacedObject po = objects[x][y];
                if(po != null && po.object != null && !(po.object instanceof Player2))
                    writeObject(x, y, id, fileWriter, po);
            }
        }

        fileWriter.println(" </objectgroup>");
    }

    /**
     * Writes a given object to the TMX file.
     * @param x             The X coordinate, in tiles, where the object is located
     * @param y             The Y coordinate, in tiles, where the object is located
     * @param id            The id of the object. Each object should get a unique id
     * @param fileWriter    The PrintWriter to use for writing
     * @param po            The Placed object to write to the file
     */
    private void writeObject(int x, int y, int id, PrintWriter fileWriter, PlacedObject po) {
        fileWriter.println("  <object id=\"" + id + "\" " +
                "name=\"" + po.object.getClass().getSimpleName() + "\" " +
                "x=\"" + x + "\" " +
                "y=\"" + y + "\" " +
                ">"
        );

        writeObjectProperties(fileWriter, po);

        fileWriter.println("  </object>");
    }

    /**
     * Writes all the properties for a placed object to the TMX file
     * @param fileWriter    FileWriter to use
     * @param po            The PlacedObject to write
     */
    private void writeObjectProperties(PrintWriter fileWriter, PlacedObject po) {
        fileWriter.println("   <properties>");

        Object obj = po.object;

        //loop through all the fields in the class and write the ones that are marked with the
        //@Property annotation
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields) {
            Property property = (Property)field.getDeclaredAnnotation(Property.class);

            if(property != null) {
                //extract the value of the field from the passed object
                Object value;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
                writeProperty(fileWriter, property, field, value);
            }
        }

        fileWriter.println("   </properties>");
    }

    /**
     * Writes a single property of an object to the TMX file.
     * @param fileWriter The FileWriter to use for writing
     * @param property   The Property annotation of the property to write
     * @param field      The actual field containing the data to write
     * @param value      The value of the property
     */
    private void writeProperty(PrintWriter fileWriter, Property property, Field field, Object value) {
        Class fieldType = field.getType();
        fileWriter.print("    <property name=\"" + field.getName() + "\" ");

        //The actual text written to the file depends on the file type of the property
        if(fieldType.equals(Integer.TYPE)) {
            fileWriter.println("type=\"int\" value=\"" + value.toString() + "\"/>");
        }
        else if (fieldType.equals(Boolean.TYPE)) {
            fileWriter.println("type=\"bool\" value=\"" + value.toString() + "\"/>");
        }
        else if (fieldType.equals(Float.TYPE)) {
            fileWriter.println("type=\"float\" value=\"" + value.toString() + "\"/>");
        }
        else if (fieldType.equals(String.class)) {
            fileWriter.println("type=\"string\" value=\"" + value + "\"/>");
        }
        else if (fieldType.isEnum()) {
            fileWriter.println("type=\"string\" value=\"" + value.toString() + "\"/>");
        }

    }

    public void importFrom(File file) {
        importFrom(file.getName());
    }

    ///TODO: ReWrite
    public void importFrom(String filename) {
        TiledMap tiledMap = new TmxMapLoader2().load(filename);
        System.out.println("Importing from \"" + filename + "\"");

        //Load the map's properties
        MapProperties tiledMapProperties = tiledMap.getProperties();
        importLevelProperties(tiledMapProperties);

        objects = new PlacedObject[width][height];

        MapLayers layers = tiledMap.getLayers();
        MapLayer objectLayer = layers.get("Object Layer 1");

        importPlayer(layers.get("Player Layer"));

        MapObjects mapObjects = objectLayer.getObjects();
        importObjects(mapObjects);

        this.oldWidth = width;
        this.oldHeight = height;
        return;
    }

    private void importLevelProperties(MapProperties mapProperties) {
        for(Field field : this.getClass().getFields()) {
            Property property = field.getDeclaredAnnotation(Property.class);

            if(property != null) {
                try {
                    if(!field.getType().isEnum()){
                        field.set(this, mapProperties.get(field.getName(), field.getType()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        //Cheap fix
        backgroundColor = BackgroundColor.fromString(mapProperties.get("backgroundColor", String.class));
    }

    private void importPlayer(MapLayer layer) {
        MapObject mapObject = layer.getObjects().get(0);

        MapProperties objectProperties = mapObject.getProperties();

        //I have no idea why, but for some reason the y axis is flipped when read.
        lastPlayerX = objectProperties.get("x", Float.TYPE).intValue();
        lastPlayerY = height - objectProperties.get("y", Float.TYPE).intValue();

        importObject(mapObject);
    }

    private void importObjects(MapObjects objects) {
        for(MapObject mapObject : objects) {
            importObject(mapObject);
        }
    }

    private void importObject(MapObject mapObject) {
        MapProperties objectProperties = mapObject.getProperties();

        int x = objectProperties.get("x", Float.TYPE).intValue();

        //I have no idea why, but for some reason the y axis is flipped when read.
        int y = height - objectProperties.get("y", Float.TYPE).intValue();

        PlaceableObject po = placeableObjects.get(mapObject.getName());
        if(po == null)
            return;

        try {
            Class objectClass = po.clazz;
            Constructor constructor = objectClass.getDeclaredConstructor();

            Object object = constructor.newInstance();

            objects[x][y] = new PlacedObject(po, object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
