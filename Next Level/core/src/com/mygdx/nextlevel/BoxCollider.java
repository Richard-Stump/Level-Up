package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.nextlevel.actors.Actor;
import com.mygdx.nextlevel.actors.Actor2;

import java.util.ArrayList;

/**
 * A rectangle shapped physics collider to be used by an actor
 */
public class BoxCollider {
    /**
     * The way the box collider works is as follows:
     *  - There one rectangular collider that actually collides with surfaces. This is what prevents the object from
     *    moving into other objects.
     *  - In addition, there are 4 colliders set as sensors which detect which side the collision happened.
     *    these do not detect other sensors, just the solid part of the box colider.
     *
     *    One thing to note,
     */

    public enum Side {
        NONE, TOP, LEFT, RIGHT, BOTTOM;

        public static Side fromInt(int val) {
            switch(val) {
                case -1: return NONE;
                case 0: return TOP;
                case 1: return LEFT;
                case 2: return BOTTOM;
                case 3: return RIGHT;
            }

            throw new IllegalArgumentException("Side for a BoxCollider must be 0-4, or -1 for none");
        }
    }

    protected Actor2        owner;
    protected Body          body;
    protected Fixture       fixture;
    protected PolygonShape  mainShape;

    protected Fixture[]         sensorFixtures;
    protected PolygonShape[]    sensorShapes;

    public boolean isTrigger;
    public boolean debugPrint = false;

    public BoxCollider(Vector2 pos, Vector2 size, boolean dynamic, boolean isTrigger) {
        this.owner = null;
        this.isTrigger = isTrigger;

        setupBodies(dynamic, pos);
        setupShapes(pos, size);
        setupFixtures(isTrigger);
    }

    public BoxCollider(Vector2 pos, Vector2 size, boolean dynamic) {
        this(pos, size, dynamic, false);
    }

    public BoxCollider(Actor2 owner, Vector2 pos, Vector2 size, boolean dynamic, boolean isTrigger) {
        this(pos, size, dynamic,  isTrigger);
        this.owner = owner;
    }

    public BoxCollider(Actor2 owner, Vector2 pos, Vector2 size, boolean dynamic) {
        this(owner, pos, size, dynamic, false);
    }

    protected void setupBodies(boolean dynamic, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = dynamic ? BodyType.DynamicBody : BodyType.StaticBody;
        bodyDef.position.set(position);
        body = CollisionManager.getWorld().createBody(bodyDef);
        body.setFixedRotation(true);
    }

    protected void setupShapes(Vector2 position, Vector2 size) {
        Vector2 halfSize = size.scl(0.5f);
        mainShape = new PolygonShape();
        mainShape.setAsBox(halfSize.x, halfSize.y);

        float width = size.x;
        float height = size.y;

        // An epsilon value is used to move the vertices of the edges slightly away from the sides
        // they run perpendicular to. This prevents multiple sides from triggering.
        final float epsilon = 0.12f;
        final float thickness = 0.05f;                  //How thick to make the sensors. Thicker decreases the chances
                                                        //for the side detection to fail
        final float xOffset = width - thickness + 0.01f;
        final float yOffset = height - thickness + 0.01f;

        // Here the sensor shapes are created in this order: right, top, left, bottom
        sensorShapes = new PolygonShape[4];
        sensorShapes[0] = new PolygonShape();
        sensorShapes[0].setAsBox(
                thickness, height - epsilon,
                new Vector2(xOffset, 0),
                0.0f
        );
        sensorShapes[1] = new PolygonShape();
        sensorShapes[1].setAsBox(
                width - epsilon, thickness,
                new Vector2(0, yOffset),
                0.0f
        );
        sensorShapes[2] = new PolygonShape();
        sensorShapes[2].setAsBox(
                thickness, height - epsilon,
                new Vector2(-xOffset, 0),
                0.0f
        );
        sensorShapes[3] = new PolygonShape();
        sensorShapes[3].setAsBox(
                width - epsilon, thickness,
                new Vector2(0, -yOffset),
                0.0f
        );
    }

    /**
     * Sets up the fixtures for the collider. The main one is used for actual collision, and the edges are
     * sensors that detect which side the collision occured
     */
    protected void setupFixtures(boolean isTrigger) {
        //setup the main fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = mainShape;
        fixtureDef.isSensor = isTrigger;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        mainShape.dispose();

        // Set up the edge fixtures for sensing. They should not provide any mass, density, or restitution.
        sensorFixtures = new Fixture[4];
        for(int i = 0; i < 4; i++) {
            FixtureDef edgeFixDef = new FixtureDef();
            edgeFixDef.density = 0;
            edgeFixDef.restitution = 0;
            edgeFixDef.filter.groupIndex = CollisionGroups.BOX_HELPER; //Mark the sensors so they don't trigger each other
            edgeFixDef.isSensor = true;
            edgeFixDef.shape = sensorShapes[i];

            sensorFixtures[i] = body.createFixture(edgeFixDef);
            sensorFixtures[i].setUserData(this);
            sensorShapes[i].dispose();
        }
    }

    /**
     * Called when one of the edges for the collider senses another collider
     * @param thisFixture The fixture which sense the collision
     * @param otherFixture The fixure that the sensor collided with.
     */
    public void edgeTrigger(Fixture thisFixture, Fixture otherFixture) {
        if(owner == null)
            return;

        Side side = Side.NONE;

        BoxCollider otherCollider = (BoxCollider)otherFixture.getUserData();
        Actor2 otherActor = otherCollider.owner;

        if(thisFixture == sensorFixtures[0])
            side = Side.RIGHT;
        else if(thisFixture == sensorFixtures[1])
            side = Side.TOP;
        else if(thisFixture == sensorFixtures[2])
            side = Side.LEFT;
        else if(thisFixture == sensorFixtures[3])
            side = Side.BOTTOM;

        if(otherCollider.isTrigger)
            owner.onTrigger(otherActor, side);
        else
            owner.onCollision(otherActor, side);
    }

    /**
     * Sets the velocity of the collider. This should get updated to play nicer with the actor class.
     * @param v The new velocity.
     */
    public void setVelocity(Vector2 v) {
        body.setLinearVelocity(v);
    }

    public Vector2 getVelocity() { return body.getLinearVelocity(); }

    public Vector2 getPosition() {
        return body.getPosition().scl(1.0f);
    }

    public void setPosition(Vector2 vec) {
        body.setTransform(vec, 0.0f);
    }

    public void dispose() {
        body.getWorld().destroyBody(body);
        body.setUserData(null);
    }
}
