package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Actor {
    final float PIXELS_TO_METERS = 100.0f;
    final short PHYSICS_ENTITY = 0x1; //0001
    final short WORLD_ENTITY = 0x1 << 1; //0010
    final short BLOCK_ENTITY = 0x1 << 2; //0100

    Sprite sprite;
    BodyDef bodyDef;
    PolygonShape shape;
    FixtureDef fixtureDef;
    Body body;
    World world;

    void setPosition(float offsetx, float offsety) {
        this.sprite.setPosition(-this.sprite.getWidth()/2.0F + offsetx, -this.sprite.getHeight()/2.0F + offsety);
    }

    void setBody(BodyDef.BodyType type) {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = type;
        this.bodyDef.position.set((this.sprite.getX() + this.sprite.getWidth()/2.0F)/PIXELS_TO_METERS, (this.sprite.getY() + this.sprite.getHeight()/2.0F)/PIXELS_TO_METERS);
        this.body = world.createBody(bodyDef);
        this.body.setFixedRotation(true);
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public Body getBody() {
        return this.body;
    }
}
