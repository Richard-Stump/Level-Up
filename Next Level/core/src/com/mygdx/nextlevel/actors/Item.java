package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.nextlevel.screens.GameScreen;

public class Item extends Actor {
    boolean destroy;
    Vector2 position;
    boolean destroyed;
    public Item(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.destroy = false;
        this.position = position;
        this.sprite.setSize(32.0F, 32.0F);
        this.destroyed = false;
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

    public boolean getDestroy() {
        return this.destroy;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean getDestroyed() {
        return this.destroyed;
    }

    public void update(float delta) {
        if (!destroyed && destroy) {
            world.destroyBody(this.body);
            destroyed = true;
        }
    }
}

//public abstract class Item extends Sprite {
//    GameScreen gameScreen;
//    World world;
//    boolean destroy;
//    boolean destroyed;
//    Body body;
//    public Item(GameScreen gameScreen, float x, float y) {
//        this.gameScreen = gameScreen;
//        this.world = gameScreen.getWorld();
//        setPosition(x, y);
//        setBounds(getX(), getY(), 32.0F, 32.0F);
//        defineItem();
//        destroy = false;
//        destroyed = false;
//    }
//
//    public abstract void defineItem();
//    public abstract void use();
//
//    public void update(float delta) {
//        if (destroy && !destroyed) {
//            world.destroyBody(body);
//            destroyed = true;
//        }
//    }
//
//    public void draw(Batch batch) {
//        if (!destroyed) {
//            super.draw(batch);
//        }
//    }
//
//    public void destroy() {
//        destroy = true;
//    }
//}