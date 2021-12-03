package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.nextlevel.*;
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.hud.Hud2;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Temporary game screen to use while refactoring code.
 *
 * One big change to note with this part of the code: Box2D calculations now use 1 pixel per meter, and the camera
 * is instead zoomed so that each unit is a tile. This was done to fix Box2D's velocity cap, which kept bodies
 * from moving too fast. This makes the math for placing enemies and everything easier though because a change of
 * 1.0f in position moves exactly one time. No more PixelsPerMeter calculations.
 *
 * No modifications can be made to the Box2d world in the collision handler functions. Deleting, adding, etc.
 * actors/colliders in the collision handling methods will cause crashes.
 */
public class GameScreen2 extends Timer implements Screen {

    /**
     * Enums to the screen in which specify what item goes into the block
     */
    public enum ItemIndex {
        SLOW(0), SPEED(1), LIFE(2), MUSHROOM(3), STAR(4), FIREFLOWER(5), LIFESTEAL(6), COIN(7), ALL(8);
        private final int value;

        ItemIndex(final int newValue) {
            value = newValue;
        }
    }
    public enum PlayerIndex {
        DEFAULT(0), POWERUP(1), STAR(2), FIRE(3), LIFESTEAL(4);
        private final int value;

        PlayerIndex(final int newValue) { value = newValue; }
    }
    public enum EnemyIndex {
        DEFAULT(0), JUMP(1), SHOOT(2);
        private final int value;

        EnemyIndex(final int newValue) { value = newValue; }
    }
    public enum BlockIndex {
        DEFAULT(0), EMPTY(1);
        private final int value;

        BlockIndex(final int newValue) { value = newValue; }
    }
    public enum CheckpointIndex {
        DEFAULT(0), TRIGGERED(1);
        private final int value;

        CheckpointIndex(final int newValue) { value = newValue; }
    }

    private NextLevel game;
    private Box2DDebugRenderer box2dRenderer;
    private OrthographicCamera camera;
    private Hud2 hud;
    TileMap tm;
//    ServerDBHandler db = new ServerDBHandler();

    private BoxCollider floor;
    private Player2 player;
    private boolean shouldReset = false;    //Should the world be reset next frame?

    long start;
    long end;
    public ArrayList<Integer> conditionList;

    public HashMap<Item2, String> itemToName = new HashMap<>();

    /**
     * Used to queue actor spawns because colliders cannot be created in the collision handlers.
     * Actors should start at the beginning of the frame anyways, not in the middle
     */
    private class ActorSpawnInfo {
        public float x, y;
        public Class<? extends Actor2> type; //The actor type to spawn

        public ActorSpawnInfo(float x, float y, Class<? extends Actor2> type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    /**
     * Default constructor
     */
    public GameScreen2() {

    }

    public String tileMapName;              //The name of the tilemap
    PushBlock pb;

    public ArrayList<Actor2> actors;               //The list of actors currently in play
    LinkedList<ActorSpawnInfo> spawnQueue;  //List of actors to spawn in the next frame
    LinkedList<Actor2> despawnQueue;        //List of actors to destroy in the next frame

    ArrayList<Actor2> despawnedActors;      //The list of actors that have been despawned from the game
    public ArrayList<Actor2> itemsList;     //The list of all items that are currently in the game screen
    public ArrayList<Actor2> blockList;     //The list of all item blocks that need to be reset
    public ArrayList<Actor2> checkpointList;//The list of all checkpoints in the game screen
    public ArrayList<Actor2> fireList;      //The list of all fire that is in the game
    public ArrayList<Enemy2.Action> enemyList; //The list of all enemies actions that die in the game

    //Textures for all actors within the game
    public ArrayList<Texture> playerTextures;
    public ArrayList<Texture> enemyTextures;
    public ArrayList<Texture> itemBlockTextures;
    public ArrayList<Texture> coinBlockTextures;
    public ArrayList<String> itemTextures;
    public ArrayList<Texture> checkpointTextures;
    public Texture endTexture;
    public Texture coinTexture;
    public Texture blockTexture;
    public Texture jewelTexture;
    public Texture playerFireTexture;
    public Texture enemyFireTexture;

    //skins for screens
    private TextureAtlas atlas;
    protected Skin skin;

    /**
     * Initialize the game screen
     * @param game The screen that created this screen
     */
//    public GameScreen2(NextLevel game, String levelInfo) {
     public GameScreen2(NextLevel game, String levelInfo) {
         this.game = game;

         atlas = new TextureAtlas("skin/uiskin.atlas");
         skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

         //Used to display where the colliders are on the screen
         box2dRenderer = new Box2DDebugRenderer();

         //Initialize a camera to view the world. Specify how many tiles are viewable vertically, and then
         //use the screen's aspect ratio to calculate how many tiles to view along the x access to keep tiles square.
         //This camera converts the world coordinates into screen coordinates when rendering, so actors don't need to
         //worry about the screen's size.
         float numTilesVisibleY = 15.0f;
         float aspect = (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
         camera = new OrthographicCamera(numTilesVisibleY * aspect, numTilesVisibleY);
         camera.translate(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f);
         camera.update();

         //Lists to keep track of actors and their states
         actors = new ArrayList<>();
         spawnQueue = new LinkedList<>();
         despawnQueue = new LinkedList<>();

         despawnedActors = new ArrayList<>();
         itemsList = new ArrayList<>();
         blockList = new ArrayList<>();
         checkpointList = new ArrayList<>();
         fireList = new ArrayList<>();
         enemyList = new ArrayList<>();

         playerTextures = new ArrayList<>();
         enemyTextures = new ArrayList<>();
         itemTextures = new ArrayList<>();
         itemBlockTextures = new ArrayList<>();
         coinBlockTextures = new ArrayList<>();
         checkpointTextures = new ArrayList<>();

         //Create and load tilemap
         tileMapName = levelInfo + ".tmx";
         System.out.println(tileMapName);
         tm = new TileMap(tileMapName);
         conditionList = tm.getConditionList();

         //setup the initial map
         init();

         start = getStartTime();
    }

    /**
     * Sets whether the game's world should be reset before the next frame.
     * @param shouldReset Whether the world should be reset
     */
    public void setShouldReset(boolean shouldReset) {
        this.shouldReset = shouldReset;
    }

    /**
     * Initial state of the game world. This is used when the world is being set up.
     */
    private void init() {
        //Initialize the collision manager
        CollisionManager.init();

        //Clear all the queues
        actors.clear();
        spawnQueue.clear();
        despawnQueue.clear();

        //Create floor and world ends
        floor = new BoxCollider(new Vector2(15, 0), new Vector2(30, 1), false, CollisionGroups.ALL, CollisionGroups.WORLD);
        new DeathBlock(this, tm.getMapWidth());

        pb = new PushBlock(this, tm);
        if (tm.getAutoScroll()) {
            actors.add(pb);
        }

        //Player Textures
        playerTextures.add(PlayerIndex.DEFAULT.value, new Texture("hero.png"));
        playerTextures.add(PlayerIndex.POWERUP.value, new Texture("powerup.png"));
        playerTextures.add(PlayerIndex.STAR.value, new Texture("star-hero.png"));
        playerTextures.add(PlayerIndex.FIRE.value, new Texture("fire-hero.png"));
        playerTextures.add(PlayerIndex.LIFESTEAL.value, new Texture("life-steal-hero.png"));

        //Item Textures
        itemTextures.add(ItemIndex.SLOW.value, "slow-mushroom.png");
        itemTextures.add(ItemIndex.SPEED.value, "speeditem.png");
        itemTextures.add(ItemIndex.LIFE.value, "1up-mushroom.jpeg");
        itemTextures.add(ItemIndex.MUSHROOM.value, "mushroom.jpeg");
        itemTextures.add(ItemIndex.STAR.value, "star.jpg");
        itemTextures.add(ItemIndex.FIREFLOWER.value, "fireflower.png");
        itemTextures.add(ItemIndex.LIFESTEAL.value,"lifesteal-mushroom.png");
        itemTextures.add(ItemIndex.COIN.value,"coin.png");

        //Coin Texture
        coinTexture = new Texture("coin.png");

        //Block Texture
        itemBlockTextures.add(BlockIndex.DEFAULT.value, new Texture("item-block.png"));
        itemBlockTextures.add(BlockIndex.EMPTY.value, new Texture("used-item-block.jpg"));
        coinBlockTextures.add(BlockIndex.DEFAULT.value, new Texture("Block.png"));
        coinBlockTextures.add(BlockIndex.EMPTY.value, new Texture("used-item-block.jpg"));
        blockTexture = new Texture("Block.png");

        //Enemy Texture
        enemyTextures.add(EnemyIndex.DEFAULT.value, new Texture("enemy.jpg"));
        enemyTextures.add(EnemyIndex.JUMP.value, new Texture("enemy_jump.png"));
        enemyTextures.add(EnemyIndex.SHOOT.value, new Texture("enemy_shoot.png"));

        //Checkpoint Textures
        checkpointTextures.add(CheckpointIndex.DEFAULT.value, new Texture("checkpoint.png"));
        checkpointTextures.add(CheckpointIndex.TRIGGERED.value, new Texture("checkpoint2.png"));

        //End Texture
        endTexture = new Texture("end.jpeg");

        //Jewel Texture
        jewelTexture = new Texture("jewel.png");

        //Fire Textures
        enemyFireTexture = new Texture("blue-fire.png");
        playerFireTexture = new Texture("fireball.png");

//        player = new Player2(this, playerTextures, 1.0f, 1.0f);
//        actors.add(new Enemy2(this, enemyTextures, 16, 2, Enemy2.Action.JUMP, player));
//        actors.add(new CheckPoint2(this, checkpointTextures, 10.0f, 1.0f, player));
//        actors.add(new End(this, endTexture, 30, 1, player));
//        actors.add(new Block2(this,itemBlockTextures, 7, 4, true, ItemIndex.ALL.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 10, 4, true, ItemIndex.SLOW.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 13, 4, true, ItemIndex.SPEED.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 16, 4, true, ItemIndex.LIFE.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 19, 4, true, ItemIndex.MUSHROOM.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 22, 4, true, ItemIndex.STAR.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 25, 4, true, ItemIndex.FIREFLOWER.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 28, 4, true, ItemIndex.LIFESTEAL.value, false));
//        actors.add(new Block2(this, coinBlockTextures, 29, 4, true, ItemIndex.COIN.value, true));
//        actors.add(new Block2(this, coinBlockTextures, 30, 4, false,false));
//        actors.add(new CoinStatic(this, coinTexture, 10, 5));
//        actors.add(new CoinStatic(this, coinTexture, 13, 5));
//        actors.add(new CoinStatic(this, coinTexture, 16, 5));
//        actors.add(new CoinStatic(this, coinTexture, 19, 5));
//        actors.add(new Enemy2(this, enemyTextures, 16, 2, Enemy2.Action.JUMP, player));
//        actors.add(new CheckPoint2(this, checkpointTextures, 10.0f, 1.0f, player));
//        actors.add(new End(this, endTexture, 30, 1, player));
//        actors.add(new Block2(this,itemBlockTextures, 7, 4, true, ItemIndex.ALL.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 10, 4, true, ItemIndex.SLOW.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 13, 4, true, ItemIndex.SPEED.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 16, 4, true, ItemIndex.LIFE.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 19, 4, true, ItemIndex.MUSHROOM.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 22, 4, true, ItemIndex.STAR.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 25, 4, true, ItemIndex.FIREFLOWER.value, false));
//        actors.add(new Block2(this, itemBlockTextures, 28, 4, true, ItemIndex.LIFESTEAL.value, false));
//        actors.add(new Block2(this, coinBlockTextures, 29, 4, true, ItemIndex.COIN.value, true));
//        actors.add(new Block2(this, coinBlockTextures, 30, 4, false,false));
//        actors.add(new CoinStatic(this, coinTexture, 10, 5));
//        actors.add(new CoinStatic(this, coinTexture, 13, 5));
//        actors.add(new CoinStatic(this, coinTexture, 16, 5));
//        actors.add(new CoinStatic(this, coinTexture, 19, 5));
//        actors.add(new CoinStatic(this, coinTexture, 10, 5));
//        actors.add(new CoinStatic(this, coinTexture, 13, 5));
//        actors.add(new CoinStatic(this, coinTexture, 16, 5));
//        actors.add(new CoinStatic(this, coinTexture, 19, 5));
//        actors.add(new Jewel(this, jewelTexture, 2, 1));
//        actors.add(player);
//        actors.add(player);

        tm.loadObjects(this, actors);

        hud = new Hud2(game.batch, player, tileMapName.substring(0, tileMapName.length()-4));

        //Add all checkpoints into checkpointlist
        for (Actor2 actor : actors) {
            if (actor.getClass().equals(CheckPoint2.class)) {
                checkpointList.add(actor);
            }
        }

        shouldReset = false;
    }

    /**
     * Resets the game world into it's initial state. This is used for when the player loses all their lives.
     */
    private void reset() {
        //Clear all the queues
        spawnQueue.clear();
        despawnQueue.clear();

        //Add all despawnedActors into the spawnQueue (Blocks and Enemies)
        for(Actor2 actor : despawnedActors) {
//            if (actor.getClass() == Jewel.class) {
//                System.out.println("Respawning jewel");
//            }
            queueActorSpawn(actor.getX(), actor.getY(), actor.getClass());
        }

        //Reset all modified Blocks into the spawnQueue
        for (Actor2 actor : blockList) {
//            if (((Block2) actor).isSpawnItem() && ((Block2) actor).isBreakable()) {
//
//            }
            if (((Block2) actor).isSpawnItem()) {
                ((Block2) actor).reset();
            }
        }

        //Despawn all items that are currently on the game screen
        despawnQueue.addAll(itemsList);
        despawnQueue.addAll(fireList);

        //Clear Queues used to reset
        itemsList.clear();
        blockList.clear();
        fireList.clear();
        despawnedActors.clear();

        hud = new Hud2(game.batch, player, tileMapName.substring(0, tileMapName.length()-4));

        shouldReset = false;

        tm.render(camera, player, true);

        if (tm.getAutoScroll()) {
            pb.getCollider().dispose();
            pb.createBoxCollider(tm.getxAxis() - tm.getScreenWidth()/2f);
        }
    }

    /**
     * Queues a new actor to be spawned in the next frame. This is partly to avoid the issue of colliders not being
     * able to be created in the collision handler, but also to ensure all new actors are spawned before the next frame
     * rather than in the middle of actor updated.
     * @param x The actor's x coordinate
     * @param y The actor's y coordinate
     * @param type The type of actor to spawn.
     */
    public void queueActorSpawn(float x, float y, Class<? extends Actor2> type) {
        spawnQueue.add(new ActorSpawnInfo(x, y, type));
    }

    /**
     * Queues an actor to be despawned in the next frame. This avoids the issue of colliders being unable to be destroyed
     * in the collision handlers
     * @param o The object to be destroyed.
     */
    public void queueActorDespawn(Actor2 o) {
        //Make sure that this object isn't in the list. If it were to be added twice,
        //then box2d would crash because it would try to destroy the object's body twice.
        if(!despawnQueue.contains(o)) {
            despawnQueue.add(o);

            if (o instanceof Item2) { //If this is an item
                if (o instanceof LifeItem2) {
                    player.addLife();
                }
                itemsList.remove(o);
            } else if (o instanceof Fire2 || o instanceof BlueFire) {
                fireList.remove(o);
            } else if (o instanceof Block2 && ((Block2) o ).isSpawnItem() && ((Block2) o).isBreakable()) {
                blockList.add(o);
            } else if (o instanceof Block2 && ((Block2) o).isSpawnItem()) {
                blockList.add(o);
            } else if (o instanceof Enemy2) {
                enemyList.add(((Enemy2) o).getAction());
                despawnedActors.add(o);
            }
//            else if (o instanceof Jewel) {
//                System.out.println("Spawn jewel");
//            }
            else {
                if (o instanceof BlueFire) {

                } else {
                    despawnedActors.add(o);
                }
//                if (o instanceof Jewel) {
////                    System.out.println("Jewel added in despawned actors");
//                }
            }
        }
    }

    @Override
    public void show() {
//        init();
    }

    /**
     * Called before rendering each frame to update the game's state.
     * @param delta How much time has passed since the last frame
     */
    private void update(float delta) {
        despawnActorsInQueue();
        if(shouldReset)
            reset();

        //New actors should be spawned before physics and update methods are called because we want the new
        //actors to be considered in this frame.
        spawnActorsInQueue();

        //I think higher iteration constants decreases the chances for side detection failure.
        CollisionManager.getWorld().step(delta, 27, 27);

        for(Actor2 a : actors) {
            a.update(delta);
        }

        //make the camera track the player
        camera.position.set(player.getPosition().x, camera.position.y, 0.0f);
        camera.update();

        hud.update(delta, player, itemToName);
        if (player.getWin()) {
            end = getEndTime();
//            System.out.println(start);
//            System.out.println(end);
//            System.out.println(end-start);
            long elapsed = end-start;
            double elapsedTime = (double) elapsed / 1000000000;
//            System.out.println(String.format("Current Record Time: %f", player.getRecordTime()));
            if (player.getRecordTime() > elapsedTime) {
                player.setRecordTime(elapsedTime);
            }
            player.incScore(elapsed / 100000);
//            System.out.println(String.format("New Record Time: %f", player.getRecordTime()));
//            System.out.println(elapsedTime);
         // ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "VICTORY", "MainMenuScreen"));
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(game, hud, "VICTORY", player));
        }
        if (player.getFail()) {
            //System.out.println("Im here");
//            ((Game) Gdx.app.getApplicationListener()).setScreen(new ErrorMessageScreen(game, "FAIL", "MainMenuScreen"));
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(game, hud, "Game Over...", player));
        }
    }

    /**
     * Called each frame by LibGDX
     * @param delta The amount of time that has passed since the last frame
     */
    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(Color.WHITE);
        tm.render(camera, player, false);

        SpriteBatch batch = game.batch;
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for(Actor2 a : actors) {
            if (!a.getClass().equals(PushBlock.class))
                a.draw(batch);
        }

        hud.render(batch);

        batch.end();

        box2dRenderer.render(CollisionManager.getWorld(), camera.combined);
    }

    /**
     * Spawns all of the actors waiting in the spawn queue.
     */
    private void spawnActorsInQueue() {
        while (!spawnQueue.isEmpty()) {
            //Use fancy reflection stuff to fetch the constructor and spawn the actor type specified.
            try {
                ActorSpawnInfo i =  spawnQueue.remove();
                Constructor<?> c;
                if (i.type.equals(Block2.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, ArrayList.class, float.class, float.class, boolean.class, int.class, boolean.class);
                    actors.add((Block2) c.newInstance(this, coinBlockTextures, i.x +0.5f, i.y + 0.5f, true, ItemIndex.COIN.value, true));
                } else if (i.type.equals(CoinStatic.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, Texture.class, float.class, float.class);
                    actors.add((CoinStatic) c.newInstance(this, coinTexture, i.x +0.25f, i.y+0.25f));
                } else if (i.type.equals(Coin.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, float.class, float.class, String.class);
                    actors.add((Coin) c.newInstance(this, i.x +0.25f, i.y+0.25f, itemTextures.get(ItemIndex.COIN.value)));
                } else if (i.type.equals(Jewel.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, Texture.class, float.class, float.class);
                    actors.add((Jewel) c.newInstance(this, jewelTexture, i.x, i.y));
                } else if (i.type.equals(Enemy2.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, ArrayList.class,float.class, float.class, Enemy2.Action.class, Player2.class);
                    actors.add((Enemy2) c.newInstance(this, enemyTextures, i.x, i.y, enemyList.get(0), player));
                    enemyList.remove(0);
                } else if (i.type.equals(BlueFire.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, float.class, float.class, Texture.class);
                    actors.add((BlueFire) c.newInstance(this, i.x, i.y, enemyFireTexture));
                } else if (i.type.equals(Fire2.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, float.class, float.class, Texture.class);
                    actors.add((Fire2) c.newInstance(this, i.x, i.y, playerFireTexture));
                } else if (i.type.getSuperclass().equals(Item2.class)) {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, float.class, float.class, String.class);
                    if (i.type.equals(SlowItem2.class)) {
                        actors.add((SlowItem2) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.SLOW.value)));
                    } else if (i.type.equals(SpeedItem2.class)) {
                        actors.add((SpeedItem2) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.SPEED.value)));
                    } else if (i.type.equals(StarItem2.class)) {
                        actors.add((StarItem2) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.STAR.value)));
                    } else if (i.type.equals(MushroomItem2.class)) {
                        actors.add((MushroomItem2) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.MUSHROOM.value)));
                    } else if (i.type.equals(LifeItem2.class)) {
                        actors.add((LifeItem2) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.LIFE.value)));
                    } else if (i.type.equals(FireFlowerItem2.class)) {
                        actors.add((FireFlowerItem2) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.FIREFLOWER.value)));
                    } else if (i.type.equals(LifeStealItem2.class)) {
                        actors.add((LifeStealItem2) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.LIFESTEAL.value)));
                    } else if (i.type.equals(Coin.class)) {
                        actors.add((Coin) c.newInstance(this, i.x, i.y, itemTextures.get(ItemIndex.COIN.value)));
                    }
                }
                else {
                    c = i.type.getDeclaredConstructor(GameScreen2.class, float.class, float.class);
                    actors.add((Actor2) c.newInstance(this, i.x, i.y));
                }

                //If statement to check to see if item/fire is in the game
                if (i.type.getSuperclass().equals(Item2.class)) {
                    itemsList.add(actors.get(actors.size() - 1));
                } else if (i.type.equals(Fire2.class) || i.type.equals(BlueFire.class)) {
                    fireList.add(actors.get(actors.size() - 1));
                }
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Despawns all of the actors in the despawn queue.
     */
    private void despawnActorsInQueue() {
        //ensure the world is not locked. Despawning actor while the world is locked will cause a crash.
        if(!CollisionManager.getWorld().isLocked()) {
            while (!despawnQueue.isEmpty()) {
                Actor2 a = despawnQueue.remove();
                if (a.getClass().equals(Item2.class)) {
                    player.incScore(20);
                    itemsList.remove(a);
                }
                if (a.getClass().equals(Coin.class) || a.getClass().equals(CoinStatic.class)) {
//                    int coins = player.getCoins();
//                    coins++;
//                    player.setCoins(coins);
                    player.incCoins();
                    player.incScore(20);
                }
                a.dispose();
                actors.remove(a);
            }
        }

        despawnQueue.clear(); //I don't know why, but this is necessary to avoid bugs. :/
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Player2 getPlayer() {return this.player;}
    public void setPlayer(Player2 player) { this.player = player; }
}
