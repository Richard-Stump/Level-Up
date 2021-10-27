package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.nextlevel.actors.Actor;

/**
 * A rectangle shapped physics collider to be used by an actor
 */
public class BoxCollider {
    /**
     * The way the box collider works is as follows:
     *  - There one rectangular collider that actually collides with surfaces. This is what prevents the object from
     *    moving into other objects.
     *  - In addition, there are 4 edge colliders set as sensors which detect which side the collision happened.
     *    these do not detect other sensors, just the solid part of the box colider.
     */

    public static float PPM = 64.0f;    // How many pixels is a meter in the game world.

    protected Actor         owner;
    protected Body          body;
    protected Fixture       fixture;
    protected PolygonShape  mainShape;

    protected Fixture[]     edgeFixtures;
    protected EdgeShape[]   edgeShapes;

    public boolean debugPrint = false;

    public BoxCollider(Actor owner, boolean dynamic) {
        this.owner = owner;

        //steps to create a box2d collider:
        // 1) setup the body
        // 2) setup the shape
        // 3) setup the fixture
        setupBodies(dynamic, owner.getPosition());
        setupShapes(owner.getPosition(), owner.getSize());
        setupFixtures();
    }

    public BoxCollider(Vector2 pos, Vector2 size, boolean dynamic) {
        this.owner = null;

        setupBodies(dynamic, pos);
        setupShapes(pos, size);
        setupFixtures();
    }

    protected void setupBodies(boolean dynamic, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = dynamic ? BodyType.DynamicBody : BodyType.StaticBody;
        bodyDef.position.set(position.scl(PPM));
        body = CollisionManager.getWorld().createBody(bodyDef);
        body.setFixedRotation(true);
    }

    protected void setupShapes(Vector2 position, Vector2 size) {
        Vector2 distanceFromCenter = size.scl(0.5f, 0.5f);
        mainShape = new PolygonShape();
        mainShape.setAsBox(size.x * PPM, size.y * PPM);

        //Calculate where the edges for the box collider will be in the world.
        Vector2 halfSize = size.scl(1.0f);
        float top = (position.y + halfSize.y) * PPM + 0.01f;
        float bottom = (position.y - halfSize.y) * PPM - 0.01f;
        float left = (position.x - halfSize.x) * PPM - 0.01f;
        float right = (position.x + halfSize.x) * PPM + 0.01f;

        // An epsilon value is used to move the vertices of the edges slightly away from the sides
        // they run perpendicular to. This prevents multiple sides from triggering.
        final float epsilon = 0.001f * PPM;

        edgeShapes = new EdgeShape[4];
        for(int i = 0; i < 4; i++)
            edgeShapes[i] = new EdgeShape();

        // I'm not sure if it makes a difference, but here I have wound the vertices
        // in a counter-clockwise order so that the shapes' normals face outward
        edgeShapes[0].set(right - epsilon, top, left + epsilon, top);
        edgeShapes[1].set(left, top - epsilon, left, bottom + epsilon);
        edgeShapes[2].set(left + epsilon, bottom,right - epsilon, bottom);
        edgeShapes[3].set(right, bottom + epsilon, right, top - epsilon);
    }

    /**
     * Sets up the fixtures for the collider. The main one is used for actual collision, and the edges are
     * sensors that detect which side the collision occured
     */
    protected void setupFixtures() {
        //setup the main fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = mainShape;
        fixtureDef.isSensor = false;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        // Set up the edge fixtures for sensing. They should not provide any mass, density, or restitution.
        edgeFixtures = new Fixture[4];
        for(int i = 0; i < 4; i++) {
            FixtureDef edgeFixDef = new FixtureDef();
            edgeFixDef.density = 0;
            edgeFixDef.restitution = 0;
            edgeFixDef.filter.groupIndex = CollisionGroups.BOX_HELPER; //Mark the sensors so they don't trigger each other
            edgeFixDef.isSensor = true;
            edgeFixDef.shape = edgeShapes[i];

            edgeFixtures[i] = body.createFixture(edgeFixDef);
            edgeFixtures[i].setUserData(this);
        }
    }

    /**
     * Called when one of the edges for the collider senses another collider
     * @param thisFixture The fixture which sense the collision
     * @param otherFixture The fixure that the sensor collided with.
     */
    public void edgeTrigger(Fixture thisFixture, Fixture otherFixture) {
        if(thisFixture == edgeFixtures[0]) {
            System.out.println("Top");
        }
        else if(thisFixture == edgeFixtures[1]) {
            System.out.println("Left");
        }
        else if(thisFixture == edgeFixtures[2]) {
            System.out.println("Bottom");
        }
        else if(thisFixture == edgeFixtures[3]) {
            System.out.println("Right");
        }
    }

    /**
     * Sets the velocity of the collider. This should get updated to play nicer with the actor class.
     * @param v The new velocity.
     */
    public void setVelocity(Vector2 v) {
        body.setLinearVelocity(v.scl(PPM));
    }
}
