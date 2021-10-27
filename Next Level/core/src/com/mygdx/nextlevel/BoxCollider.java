package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.nextlevel.actors.Actor;

import static com.mygdx.nextlevel.CollisionManager.getWorld;

public class BoxCollider {
    public static float PPM = 64.0f;

    protected Actor         owner;
    protected Body          body;
    protected Fixture       fixture;
    protected PolygonShape  shape;

    public BoxCollider(Actor owner, boolean dynamic) {
        this.owner = owner;

        //steps to create a box2d collider:
        // 1) setup the body
        // 2) setup the shape
        // 3) setup the fixture
        setupBody(dynamic, owner.getPosition());
        setupShape(owner.getPosition(), owner.getSize());
        setupFixture();
    }

    public BoxCollider(Vector2 pos, Vector2 size, boolean dynamic) {
        this.owner = null;

        setupBody(dynamic, pos);
        setupShape(pos, size);
        setupFixture();
    }

    protected void setupBody(boolean dynamic, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = dynamic ? BodyType.DynamicBody : BodyType.StaticBody;
        bodyDef.position.set(position.scl(PPM));
        body = getWorld().createBody(bodyDef);
        body.setFixedRotation(true);
    }

    protected void setupShape(Vector2 position, Vector2 size) {
        Vector2 distanceFromCenter = size.scl(0.5f, 0.5f);
        shape = new PolygonShape();
        shape.setAsBox(size.x * PPM, size.y * PPM);
    }

    protected void setupFixture() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
    }

    public void onCollisionEnter(BoxCollider collider) {
        System.out.print("Collision Started on ");

        Vector2 posA = body.getPosition();
        Vector2 posB = collider.body.getPosition();
        Vector2 dir = posB.sub(posA);

        float angle = dir.angleDeg();

        if(angle >= 45.0f && angle <= 135.0f) {
            System.out.println("top");
        }
        else if (angle > 135.0f && angle < 225.0f) {
            System.out.println("left");
        }
        else if (angle >= 225.0f && angle <= 315.0f ) {
            System.out.println("bottom");
        }
        else {
            System.out.println("right");
        }
    }

    public void setVelocity(Vector2 v) {
        body.setLinearVelocity(v.scl(PPM));
    }
}
