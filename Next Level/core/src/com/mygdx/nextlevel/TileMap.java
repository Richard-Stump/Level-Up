package com.mygdx.nextlevel;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.nextlevel.actors.Actor2;
import com.mygdx.nextlevel.actors.Block;
import com.mygdx.nextlevel.actors.Block2;
import com.mygdx.nextlevel.actors.Player2;
import com.mygdx.nextlevel.screens.GameScreen2;

import java.util.ArrayList;


public class TileMap extends ApplicationAdapter{
    TiledMap tiledMap;
    MapProperties tiledMapProperties;
    TiledMapRenderer tiledMapRenderer;
    TiledMapTileLayer layer;
    public static ArrayList<Integer> conditionList = new ArrayList<>();

    //Tile Map Properties
    int mapWidth;
    int mapHeight;
    boolean collectCoin;
    boolean beatTimeLimit;
    boolean killAllEnemies;
    boolean killNoEnemies;
    boolean keepJewel;
    boolean autoScroll;
    float timeLimit;

    //Camera Position
    float xAxis;
    float yAxis;
    float screenWidth;

    public TileMap(String filename) {
        tiledMap = new TmxMapLoader().load(filename);
        tiledMapProperties = tiledMap.getProperties();
        layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);

        mapWidth = tiledMapProperties.get("width", Integer.class);
        mapHeight = tiledMapProperties.get("height", Integer.class);

        screenWidth = Gdx.graphics.getWidth()/32f;
        xAxis = screenWidth/2f;
        yAxis = screenWidth/2f/32f;

//        collectCoin = tiledMapProperties.get("collectCoins", Boolean.class);
//        beatTimeLimit = tiledMapProperties.get("beatTimeLimit", Boolean.class);
//        killAllEnemies = tiledMapProperties.get("killAllEnemies", Boolean.class);
//        killNoEnemies = tiledMapProperties.get("killNoEnemies", Boolean.class);
//        keepJewel = tiledMapProperties.get("keepJewel", Boolean.class);
//        timeLimit = tiledMapProperties.get("timeLimit", Float.class);
//        autoScroll = tiledMapProperties.get("autoScroll", Boolean.class);
        autoScroll = true;

        if (collectCoin) {
            conditionList.add(1);
        }
        if (beatTimeLimit) {
            conditionList.add(5);
        }
        if (killAllEnemies) {
            conditionList.add(2);
        }
        if (killNoEnemies) {
            conditionList.add(3);
        }
        if (keepJewel) {
            conditionList.add(4);
        }

        for (Integer integer : conditionList) {
            System.out.println(integer);
        }
        if (conditionList.contains(5)) {
            System.out.println(timeLimit);
        } else  {
            System.out.println("No time limit");
        }

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1.0f/32.0f);
    }

    public void loadObjects(GameScreen2 screen, ArrayList<Actor2> actors){
        //TODO are we going to have object layer for certain actor groups (PLayer | Enemy | block | items)
        //TODO have tile maps show different layers
        //TODO Item Texture (add all to item texture list)
        //TODO Fire Textures (add to player/enemy fire)
        //TODO Player Textures (add all to player texture list)
        //TODO Coin texture (add to coin texutre)
        //TODO Jewel texture (add to Jewel texutre)
        //TODO Enemy Textures (add all to enemy texture list)
        //TODO Block Textures (add to itemblocktextures & coin block texture list along with block texture)
        //TODO Checkpoint Textures (add all to Checkpoint texture list)
        //TODO End texture (add to End texutre)



        MapLayer objectLayer = tiledMap.getLayers().get("Object Layer 1");
        int i = 0;
        //FIXME work texture
        for(MapObject object : objectLayer.getObjects()){
//            if(object instanceof TextureMapObject && i != 0) {
            if(object instanceof TextureMapObject) {
                TextureMapObject mapObject = (TextureMapObject) object;
                ArrayList<Texture> test = new ArrayList<>();
                float x = (float) object.getProperties().get("x");
                float y = (float) object.getProperties().get("y");
                float width = (float) object.getProperties().get("width");
                float height = (float) object.getProperties().get("height");
                mapObject.getTextureRegion().setRegion(x, y, width, height);
                Texture texture = mapObject.getTextureRegion().getTexture();
                mapObject.getTextureRegion().setRegion(mapObject.getTextureRegion().getRegionX() + x, mapObject.getTextureRegion().getRegionY() + y, width, height);
                test.add(texture);
                actors.add(new Block2(screen, test, mapObject.getX()/32.0f, mapObject.getY()/32.0f, false, false));
            }
            i++;
        }
    }

    public void render (OrthographicCamera camera, Player2 player, boolean reset) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (autoScroll) {
            if (reset) {
                if (player.getX() <= screenWidth/2f) { //Incase player moves back
                    xAxis = screenWidth/2f;
                } else if (mapWidth - player.getX() <= screenWidth/2f) {
                    xAxis = mapWidth - screenWidth/2f;
                } else {
                    xAxis = player.getX();
                }
            } else if (mapWidth - xAxis <= screenWidth/2f) {
                xAxis = mapWidth - screenWidth/2f;
            } else {
                xAxis += 0.0162f;
            }
        } else {
            if (player.getX() <= screenWidth/2f) { //Incase player moves back
                xAxis = screenWidth/2f;
            } else if (mapWidth - player.getX() <= screenWidth/2f) {
                xAxis = mapWidth - screenWidth/2f;
            } else {
                xAxis = player.getX();
            }
        }
        yAxis = Gdx.graphics.getHeight()/2f/32f;

        camera.position.x = xAxis;
        camera.position.y = yAxis;

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }
    public float getxAxis() { return xAxis; }
    public boolean getAutoScroll() { return autoScroll; }
    public float getScreenWidth() { return screenWidth; }

    public ArrayList<Integer> getConditionList() {
        return conditionList;
    }
    public float getTimeLimit() {
        return timeLimit;
    }
}