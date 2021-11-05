package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionManager;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.hud.Hud2;

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
public class ItemShowcaseScreen2 implements GameScreenBase {
    private NextLevel game;

    private Box2DDebugRenderer box2dRenderer;
    private OrthographicCamera camera;
    private Hud2 hud;
    //GameScreen2 screen = new GameScreen2();

    private BoxCollider floor;
    private Player2 player;
    private boolean shouldReset = false;    //Should the world be reset next frame?

    public HashMap<Item2, String> itemToName = new HashMap<>();
    ArrayList<Class> items;
    ArrayList<Class> mushroom;
    ArrayList<Class> slowItem;
    ArrayList<Class> speedItem;
    ArrayList<Class> lifeItem;
    ArrayList<Class> star;
    ArrayList<Class> fireflower;
    ArrayList<Class> lifeSteal;

    @Override
    public void queueActorSpawn(float x, float y, Class<? extends Actor2> type) {
        queueActorSpawn2(x, y, type);
    }

    @Override
    public void queueActorDespawn(Actor2 o) {
        queueActorDespawn2(o);
    }

    /**
     * Used to queue actor spawns because colliders cannot be created in the collision handlers.
     * Actors should start at the beginning of the frame anyways, not in the middle
     */
    private class ActorSpawnInfo2 {
        public float x, y;
        public Class<? extends Actor2> type; //The actor type to spawn

        public ActorSpawnInfo2(float x, float y, Class<? extends Actor2> type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    /**
     * Default constructor
     */
    public ItemShowcaseScreen2() {

    }

    ArrayList<Actor2> actors2;               //The list of actors currently in play
    LinkedList<ActorSpawnInfo2> spawnQueue2;  //List of actors to spawn in the next frame
    LinkedList<Actor2> despawnQueue2;        //List of actors to destroy in the next frame

    /**
     * Initialize the game screen
     * @param game The screen that created this screen
     */
    public ItemShowcaseScreen2(NextLevel game) {
        this.game = game;
        //screen = new GameScreen2();
        //screen = new ItemShowcaseScreen2();
        items = new ArrayList<>();
        items.add(SlowItem2.class);
        items.add(SpeedItem2.class);
        items.add(LifeItem2.class);
        items.add(MushroomItem2.class);
        items.add(StarItem2.class);
        items.add(FireFlowerItem2.class);
        items.add(LifeStealItem2.class);

        mushroom = new ArrayList<>();
        mushroom.add(MushroomItem2.class);

        slowItem = new ArrayList<>();
        slowItem.add(SlowItem2.class);

        speedItem = new ArrayList<>();
        speedItem.add(SpeedItem2.class);

        lifeItem = new ArrayList<>();
        lifeItem.add(LifeItem2.class);

        star = new ArrayList<>();
        star.add(StarItem2.class);

        fireflower = new ArrayList<>();
        fireflower.add(FireFlowerItem2.class);

        lifeSteal = new ArrayList<>();
        lifeSteal.add(LifeStealItem2.class);

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
        actors2 = new ArrayList<>();
        spawnQueue2 = new LinkedList<>();
        despawnQueue2 = new LinkedList<>();

        //setup the initial map
        reset2();
    }

    /**
     * Sets whether the game's world should be reset before the next frame.
     * @param shouldReset Whether the world should be reset
     */
    public void setShouldReset2(boolean shouldReset) {
        this.shouldReset = shouldReset;
    }

    /**
     * Resets the game world into it's initial state. This is used for when the player loses all their lives.
     * Todo: This causes a lag spike, improve reset time when game is more fleshed out.
     */
    private void reset2() {
        CollisionManager.init();
        //Initialize the collision manager and create the floor
        CollisionManager.init();
        floor = new BoxCollider(new Vector2(15, 0), new Vector2(30, 1), false);

        //Clear all the queues
        actors2.clear();
        spawnQueue2.clear();
        despawnQueue2.clear();

        //Create all the actors for the test scene. This should be replaced with tilemap/level loading code.
        player = new Player2(this, 7, 2);
        actors2.add(new Enemy2(this,5, 2));
        actors2.add(new Block2(this, 7, 4, true, mushroom));
        actors2.add(new Block2(this, 10, 4, true, slowItem));
        actors2.add(new Block2(this, 13, 4, true, speedItem));
        actors2.add(new Block2(this, 16, 4, true, star));
        actors2.add(new Block2(this, 22, 4, true, lifeItem));
        actors2.add(new Block2(this, 25, 4, true, lifeSteal));
        actors2.add(new Block2(this, 28, 4, true, fireflower));

        actors2.add(new CheckPoint2(this, 10, 1.0f));
        actors2.add(player);
        actors2.add(new DeathBlock(this, player, player.getPosition().x));

        hud = new Hud2(game.batch, player);

        shouldReset = false;
    }

    /**
     * Queues a new actor to be spawned in the next frame. This is partly to avoid the issue of colliders not being
     * able to be created in the collision handler, but also to ensure all new actors are spawned before the next frame
     * rather than in the middle of actor updated.
     * @param x The actor's x coordinate
     * @param y The actor's y coordinate
     * @param type The type of actor to spawn.
     */
    public void queueActorSpawn2(float x, float y, Class<? extends Actor2> type) {
        spawnQueue2.add(new ActorSpawnInfo2(x, y, type));
    }

    /**
     * Queues an actor to be despawned in the next frame. This avoids the issue of colliders being unable to be destroyed
     * in the collision handlers
     * @param o The object to be destroyed.
     */
    public void queueActorDespawn2(Actor2 o) {
        //Make sure that this object isn't in the list. If it were to be added twice,
        //then box2d would crash because it would try to destroy the object's body twice.
        if(!despawnQueue2.contains(o))
            despawnQueue2.add(o);
    }

    @Override
    public void show() {
        reset2();
    }

    /**
     * Called before rendering each frame to update the game's state.
     * @param delta How much time has passed since the last frame
     */
    private void update(float delta) {
        if(shouldReset)
            reset2();

        //New actors should be spawned before physics and update methods are called because we want the new
        //actors to be considered in this frame.
        despawnActorsInQueue2();
        spawnActorsInQueue2();

        //I think higher iteration constants decreases the chances for side detection failure.
        CollisionManager.getWorld().step(delta, 27, 27);

        for(Actor2 a : actors2) {
            a.update(delta);
        }

        //make the camera track the player
        camera.position.set(player.getPosition().x, camera.position.y, 0.0f);
        camera.update();

        hud.update(delta, player, itemToName);
    }

    /**
     * Called each frame by LibGDX
     * @param delta The amount of time that has passed since the last frame
     */
    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.WHITE);

        Batch batch = game.batch;
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for(Actor2 a : actors2) {
            a.draw(batch);
        }

        hud.render(batch);

        batch.end();

        box2dRenderer.render(CollisionManager.getWorld(), camera.combined);
    }

    /**
     * Spawns all of the actors waiting in the spawn queue.
     */
    private void spawnActorsInQueue2() {
        while (!spawnQueue2.isEmpty()) {
            //Use fancy reflection stuff to fetch the constructor and spawn the actor type specified.
            try {
                ActorSpawnInfo2 i =  spawnQueue2.remove();
                Constructor<?> c = i.type.getDeclaredConstructor(GameScreenBase.class, float.class, float.class);
                actors2.add((Actor2) c.newInstance(this, i.x, i.y));
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
    private void despawnActorsInQueue2() {
        //ensure the world is not locked. Despawning actor while the world is locked will cause a crash.
        if(!CollisionManager.getWorld().isLocked()) {
            while (!despawnQueue2.isEmpty()) {
                Actor2 a = despawnQueue2.remove();
                a.dispose();
                actors2.remove(a);
            }
        }

        despawnQueue2.clear(); //I don't know why, but this is necessary to avoid bugs. :/
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

    @Override
    public void setShouldReset(boolean shouldReset) {
        setShouldReset2(shouldReset);
    }

    public GameScreenBase getScreen() {
        return this;
    }
}