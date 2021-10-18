package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Actor {
    final float PIXELS_TO_METERS = 100.0f;
    final short PHYSICS_ENTITY = 0x1; //0001
    final short WORLD_ENTITY = 0x1 << 1; //0010
    final short BLOCK_ENTITY = 0x1 << 2; //0100

    Vector2 worldSpawn = new Vector2();
    Sprite sprite;
    BodyDef bodyDef;
    PolygonShape shape;
    FixtureDef fixtureDef;
    Body body;
    World world;
    boolean deleteSprite;

    //Added For Contact Direction
    FixtureDef bottom, head, leftSide, rightSide;
    EdgeShape edgeShape;
    float w = 0F;
    float h = 0F;
    float tolerance = 0.02F;

    void setPosition(float x, float y) {
        this.deleteSprite = false;
        this.sprite.setPosition(-this.sprite.getWidth()/2.0F + x, -this.sprite.getHeight()/2.0F + y);
        this.worldSpawn.x = x;
        this.worldSpawn.y = y;
    }

    void setBody(BodyDef.BodyType type) {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = type;
        this.bodyDef.position.set((this.sprite.getX() + this.sprite.getWidth()/2.0F)/PIXELS_TO_METERS, (this.sprite.getY() + this.sprite.getHeight()/2.0F)/PIXELS_TO_METERS);
        this.body = world.createBody(bodyDef);
        this.body.setFixedRotation(true);
    }

    void setEdgeShape() {
        this.edgeShape = new EdgeShape();
        this.w = this.sprite.getWidth()/PIXELS_TO_METERS;
        this.h = this.sprite.getHeight()/PIXELS_TO_METERS;
    }

    void setContactSide(FixtureDef def) {
        def = new FixtureDef();
        def.density = 0;
        def.restitution = 0;
        def.filter.categoryBits = PHYSICS_ENTITY;
        def.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        def.shape = this.edgeShape;
        def.isSensor = true;
        this.body.createFixture(def);
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public boolean getDeleteSprite() {
        return this.deleteSprite;
    }
    public void setDeleteSprite(boolean set) {
        this.deleteSprite = set;
    }

    public Body getBody() {
        return this.body;
    }


}
