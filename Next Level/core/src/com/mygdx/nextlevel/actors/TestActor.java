package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class TestActor extends Actor {
    Vector2 spawnpoint;
    EdgeShape feet, head, leftSide, rightSide;

    public TestActor(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(64.0F, 64.0F);
        super.setPosition(position.x, position.y);
        this.spawnpoint = this.worldSpawn;
        super.setBody(BodyDef.BodyType.StaticBody);
        setShape();
        setFixture(density, restitution);
    }

    /*
    void setBody(BodyDef.BodyType type) {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = type;
    }
     */
    @Override
    void setBody(BodyDef.BodyType type) {
        super.setBody(type);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = type;
        this.bodyDef.position.set((this.sprite.getX() + this.sprite.getWidth()/2.0F)/PIXELS_TO_METERS, (this.sprite.getY() + this.sprite.getHeight()/2.0F)/PIXELS_TO_METERS);
        this.body = world.createBody(bodyDef);
        this.body.setFixedRotation(true);
    }

    private void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
    }

    private void setFixture(float density, float restitution) {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = density;
        this.fixtureDef.restitution = restitution;
        this.fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;

        feet = new EdgeShape();
        feet.set(-this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, -this.sprite.getHeight()/2.0F/PIXELS_TO_METERS, this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, -this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
        this.fixtureDef.shape = feet;
        this.body = this.world.createBody(this.bodyDef);
        this.body.createFixture(this.fixtureDef);
        this.body.setUserData(feet);

//        this.body.createFixture(this.fixtureDef);
//        EdgeShape head = new EdgeShape();
//        head.set(-this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS, this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
//        this.fixtureDef.shape = head;

//        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.shape = this.shape;
//        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.shape.dispose();
    }

    public EdgeShape getFeet() { return this.feet; }
}
