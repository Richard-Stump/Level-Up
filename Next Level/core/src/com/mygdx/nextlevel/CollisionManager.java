package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CollisionManager implements ContactListener {
    public static CollisionManager instance;

    public World world;

    private CollisionManager() {
        world = new World(new Vector2(0.0f, -9.81f), true);
        world.setContactListener(this);
    }

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

    @Override
    public void beginContact(Contact contact) { //called when two fixtures begin contact
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        BoxCollider colliderA = (BoxCollider)fixtureA.getUserData();
        BoxCollider colliderB = (BoxCollider)fixtureB.getUserData();

        colliderA.onCollisionEnter(colliderB);
        colliderB.onCollisionEnter(colliderA);
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
