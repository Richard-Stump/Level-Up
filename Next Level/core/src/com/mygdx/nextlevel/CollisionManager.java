package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * This class is a singleton which manages the collision detection system. It handles the Box2D world.
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
        world = new World(new Vector2(0.0f, 0.0f), true);
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

    public static CollisionManager getInstance() {
        checkForInstance();
        return instance;
    }

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

        BoxCollider colliderA = (BoxCollider)fixtureA.getUserData();
        BoxCollider colliderB = (BoxCollider)fixtureB.getUserData();

        if(fixtureA.isSensor() || fixtureB.isSensor()) {
            colliderA.edgeTrigger(fixtureA, fixtureB);
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
