package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.LinkedList;

/**
 * This class is a singleton which manages the collision detection system. It handles the Box2D world.
 * This was initially done so that actors wouldn't need to be passed the Box2d world all the time, but this could
 * possibly become a field in the game screen.
 * Todo: Refactor?
 *
 * The singleton needs to be instantiated with CollisionManager.init() before colliders can be used.
 */
public class CollisionManager implements ContactListener {
    public static CollisionManager instance;

    public World world;

    /**
     * Private constructor which constructs the Box2D world and sets up other collision handling stuff.
     */
    private CollisionManager() {
        world = new World(new Vector2(0.0f, -9.81f * 2.0f), true);
        world.setContactListener(this);
    }

    /**
     * Checks if there is a valid instance. If there is not, an exception is thrown
     */
    private static void checkForInstance() {
        if(instance == null)
            throw new RuntimeException("CollisionManager not initialized");
    }

    public static void init() {
        instance = new CollisionManager();
    }

    /**
     * Gets the singleton instance
     * @return The CollisionManager instance
     */
    public static CollisionManager getInstance() {
        checkForInstance();
        return instance;
    }

    /**
     * Gets the Box2d world in the instance
     * @return The Box2d world
     */
    public static World getWorld() {
        checkForInstance();
        return instance.world;
    }

    /**
     * Handles the actual collision between the objects. This is called by Box2D.
     * @param contact The Box2D contact for the collision.
     */
    @Override
    public void beginContact(Contact contact) { //called when two fixtures begin contact
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //The fixtures are checked to see if they are sensors. If the fixture is solid, it gets ignored because
        //we only care about the edge fixtures. If the main fixture of the collider is a sensor, it still gets
        //through, however.
        if(fixtureA.isSensor()) { //If A is a sensor, we want to notify it's collider that we collided with something else.
            BoxCollider colliderA = (BoxCollider)fixtureA.getUserData();
            colliderA.edgeTrigger(fixtureA, fixtureB);
        }

        if(fixtureB.isSensor()) {
            BoxCollider colliderB = (BoxCollider)fixtureB.getUserData();
            colliderB.edgeTrigger(fixtureB, fixtureA);
        }
    }

    @Override
    public void endContact(Contact contact) { //called when two fixtures stop contact

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
