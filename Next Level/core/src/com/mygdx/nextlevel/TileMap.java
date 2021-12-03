package com.mygdx.nextlevel;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
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
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.enums.BackgroundColor;
import com.mygdx.nextlevel.jankFix.TmxMapLoader2;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;


public class TileMap extends ApplicationAdapter{
    TiledMap tiledMap;
    MapProperties tiledMapProperties;
    TiledMapRenderer tiledMapRenderer;
    public static ArrayList<Integer> conditionList = new ArrayList<>();

    //for test cases
    public static TiledMap tmTest;
    public static MapProperties tmProperties;
    public static ArrayList<Integer> conditionListTest = new ArrayList<>();
    public static boolean collectCoinTest;
    public static boolean beatTimeLimitTest;
    public static boolean killAllEnemiesTest;
    public static boolean killNoEnemiesTest;
    public static boolean keepJewelTest;
    public static boolean autoScrollTest;
    public static float timeLimitTest;
    public static float gravityTest;

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
    float gravity;
    String backgroundColor;

    //Camera Position
    float xAxis;
    float yAxis;
    float screenWidth;
    float screenHeight;

    //Hud Properties
    int coinCount = 0;
    int enemyCount = 0;

    public TileMap(){}

    public TileMap(String filename, FileHandleResolver resolver) {
        init(filename, new TmxMapLoader2(resolver));
    }

    public TileMap(String filename) {
        init(filename, new TmxMapLoader2());
    }

    protected void init(String filename, TmxMapLoader2 loader) {
        tiledMap = loader.load(filename);
        tiledMapProperties = tiledMap.getProperties();

        mapWidth = tiledMapProperties.get("width", Integer.class);
        mapHeight = tiledMapProperties.get("height", Integer.class);

        screenWidth = Gdx.graphics.getWidth()/32f;
        screenHeight = Gdx.graphics.getHeight()/32f;
        xAxis = screenWidth/2f;
        yAxis = screenWidth/2f/32f;

        collectCoin = tiledMapProperties.get("collectCoins", Boolean.class);
        beatTimeLimit = tiledMapProperties.get("beatTimeLimit", Boolean.class);
        killAllEnemies = tiledMapProperties.get("killAllEnemies", Boolean.class);
        killNoEnemies = tiledMapProperties.get("killNoEnemies", Boolean.class);
        keepJewel = tiledMapProperties.get("keepJewel", Boolean.class);

        timeLimit = tiledMapProperties.get("timeLimit", Integer.class);
        autoScroll = tiledMapProperties.get("autoScroll", Boolean.class);
        gravity = tiledMapProperties.get("gravity", Float.class);
        backgroundColor = tiledMapProperties.get("backgroundColor", String.class);

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

//        for (Integer integer : conditionList) {
//            System.out.println(integer);
//        }
//        if (conditionList.contains(5)) {
//            System.out.println(timeLimit);
//        } else  {
////            System.out.println("No time limit");
//        }

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

        MapLayer objectLayer;
        objectLayer = tiledMap.getLayers().get("Player Layer");
        for(MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject mapObject = (RectangleMapObject) object;
                if (mapObject.getName().equals("Player2")) {
                    screen.setPlayer(new Player2( screen,
                            screen.playerTextures,
                            mapObject.getProperties().get("x", Float.TYPE),
                            mapHeight - mapObject.getProperties().get("y", Float.TYPE)
                            ));
                }
            }
        }

        objectLayer = tiledMap.getLayers().get("Object Layer 1");
        for(MapObject object : objectLayer.getObjects()){
            if(object instanceof RectangleMapObject) {
                RectangleMapObject mapObject = (RectangleMapObject) object;
                switch (mapObject.getName()) {
                    case ("Block2"): //FIXME (Wait for other properties)
                        /*
                        if (mapObject.getProperties().get("Can Spawn Item", Boolean.TYPE) && !mapObject.getProperties().get("breakable", Boolean.TYPE)) { //Item Block
                            screen.actors.add(new Block2(screen,
                                    screen.itemBlockTextures,
                                    mapObject.getProperties().get("x", Float.TYPE),
                                    mapObject.getProperties().get("y", Float.TYPE),
                                    true,
                                    GameScreen2.ItemIndex.ALL.getValue(), //FIXME this will need to be updated
                                    false));
                        } else if (mapObject.getProperties().get("Can Spawn Item", Boolean.TYPE) && mapObject.getProperties().get("breakable", Boolean.TYPE)) { //Coin Block
                            screen.actors.add(new Block2(screen,
                                    screen.coinBlockTextures,
                                    mapObject.getProperties().get("x", Float.TYPE),
                                    mapObject.getProperties().get("y", Float.TYPE),
                                    true,
                                    GameScreen2.ItemIndex.COIN.getValue(),
                                    true));
                                    coinCount++;
                        } else { //Non Breakable Block
                            screen.actors.add(new Block2(screen,
                                    screen.blockTextures,
                                    mapObject.getProperties().get("x", Float.TYPE),
                                    mapObject.getProperties().get("y", Float.TYPE),
                                    false,
                                    GameScreen2.ItemIndex.NONE.getValue(),
                                    false));
                        }
                        */
                        screen.actors.add(new Block2(screen,
                                screen.itemBlockTextures,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE),
                                true,
                                GameScreen2.ItemIndex.ALL.getValue(),
                                mapObject.getProperties().get("breakable", Boolean.TYPE)));
                        break;
                    case ("Enemy2"): //FIXME (Wait for other properties)
                        screen.actors.add(new Enemy2(screen,
                                screen.enemyTextures,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE),
//                                mapObject.getProperties().get("Enemy Action", Enemy2.Action.class),
                                Enemy2.Action.DEFAULT, //FIXME need to get property
                                screen.getPlayer()
                                ));
                        enemyCount++;
                        break;
                    case ("End"):
                        screen.actors.add(new End(screen,
                                screen.endTexture,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE),
                                screen.getPlayer()
                                ));
                        break;
                    case ("CheckPoint2"):
                        screen.actors.add(new CheckPoint2(screen,
                                screen.checkpointTextures,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE),
                                screen.getPlayer()
                                ));
                        break;
                    case ("Jewel"):
                        screen.actors.add(new Jewel(screen,
                                screen.jewelTexture,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE)
                                ));
                        break;
                    case ("SpikeBlock"):
                        screen.actors.add(new SpikeBlock(screen,
                                screen.spikeBlockTexture,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE)
                                ));
                        break;
                    case ("CoinStatic"):
                        screen.actors.add(new CoinStatic(screen,
                                screen.coinTexture,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE)
                                ));
                        coinCount++;
                        break;
                    case ("basicBlock1"):
                        screen.actors.add(new basicBlock1(screen,
                                screen.basicBlock1Textures,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE)
                                ));
                        break;
                    case ("basicBlock2"):
                        screen.actors.add(new basicBlock2(screen,
                                screen.basicBlock2Textures,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE)
                        ));
                        break;
                    case ("basicBlock3"):
                        screen.actors.add(new basicBlock3(screen,
                                screen.basicBlock3Textures,
                                mapObject.getProperties().get("x", Float.TYPE),
                                mapHeight - mapObject.getProperties().get("y", Float.TYPE)
                        ));
                        break;
                }
            }
        }

        //Add player to the screen
        screen.actors.add(screen.getPlayer());
    }

    public void render (OrthographicCamera camera, Player2 player, boolean reset) {


        switch (backgroundColor){
            case "Blue" : Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
                break;
            case "Green": Gdx.gl.glClearColor(47/255f, 79/255f, 79/255f, 1);
                break;
            case "Brown": Gdx.gl.glClearColor(160/255f, 82/255f, 45/255f, 1);
                break;
            case "Grey": Gdx.gl.glClearColor(169/255f, 169/255f, 169/255f, 1);
                break;
        }

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

        if (player.getY() <= screenHeight/2f) {
            yAxis = screenHeight/2f;
        } else if (mapHeight - player.getY() <= screenHeight/2f) {
            yAxis = mapHeight - screenHeight/2f;
        } else {
            yAxis = player.getY();
        }

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
    public float getGravity() {
        return gravity;
    }



    static public void createTMX() {
        tmTest = new TmxMapLoader2().load("stump_tprkjymj.tmx");
        tmProperties = tmTest.getProperties();
        collectCoinTest = tmProperties.get("collectCoins", Boolean.class);
        beatTimeLimitTest = tmProperties.get("beatTimeLimit", Boolean.class);
        killAllEnemiesTest = tmProperties.get("killAllEnemies", Boolean.class);
        killNoEnemiesTest = tmProperties.get("killNoEnemies", Boolean.class);
        keepJewelTest = tmProperties.get("keepJewel", Boolean.class);
        timeLimitTest = tmProperties.get("timeLimit", Float.class);
        gravityTest = tmProperties.get("gravity", Float.class);
        if (collectCoinTest) {
            conditionListTest.add(1);
        }
        if (beatTimeLimitTest) {
            conditionListTest.add(5);
        }
        if (killAllEnemiesTest) {
            conditionListTest.add(2);
        }
        if (killNoEnemiesTest) {
            conditionListTest.add(3);
        }
        if (keepJewelTest) {
            conditionListTest.add(4);
        }
    }

    static public ArrayList<Integer> getConditionListTest() {
        return conditionListTest;
    }

    static public float getTimeLimitTest1() {
        return timeLimitTest;
    }

    static public float getGravityTest1() {
        return gravityTest;
    }

    public int getCoinCount() { return this.coinCount; }
    public int getEnemyCount() { return this.enemyCount; }
}