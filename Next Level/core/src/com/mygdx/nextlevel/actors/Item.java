package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Item extends Actor {
    boolean destroy;
    Vector2 position;
    public Item(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.destroy = false;
        this.position = position;
        this.sprite.setSize(32.0F, 32.0F);
//        super.setPosition(position.x, position.y);
//        super.setBody(BodyDef.BodyType.StaticBody);
//        super.setBody(BodyDef.BodyType.DynamicBody);

//        setShape();
//        setFixture(density, restitution);
    }
    public Vector2 getSpawn() {
        return this.position;
    }
    public void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
    }

    public void setBody(BodyDef.BodyType type) {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = type;
        this.bodyDef.position.set((this.sprite.getX() + this.sprite.getWidth()/2.0F)/PIXELS_TO_METERS, (this.sprite.getY() + this.sprite.getHeight()/2.0F)/PIXELS_TO_METERS);
        this.body = world.createBody(bodyDef);
        this.body.setFixedRotation(true);
    }

    public void setPosition(float x, float y) {
        this.deleteSprite = false;
        this.sprite.setPosition(-this.sprite.getWidth()/2.0F + x, -this.sprite.getHeight()/2.0F + y);
        this.worldSpawn.x = x;
        this.worldSpawn.y = y;
    }

    public void setFixture(float density, float restitution) {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = density;
        this.fixtureDef.restitution = restitution;
        this.fixtureDef.filter.categoryBits = BLOCK_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        this.fixtureDef.shape = this.shape;
        this.fixtureDef.isSensor = true;
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    private boolean getDestroy() {
        return this.destroy;
    }
}
