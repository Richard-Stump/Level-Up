package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Fire extends Actor{
    public Vector2 position;
    public float v;

    public Fire(Vector2 playerLocation, Texture texture, World world, float velocity) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.position = playerLocation;
        this.sprite.setSize(20.0F, 20.0F);
        super.setPosition(position.x, position.y);
        super.setBody(BodyDef.BodyType.StaticBody);
        setShape();
//        setFixture();
        v = velocity;
    }

    public void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
    }

    public void setFixture() {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = 0f;
        this.fixtureDef.restitution = 0f;
        this.fixtureDef.filter.categoryBits = BLOCK_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        this.fixtureDef.shape = this.shape;
        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.isSensor = true;
        this.shape.dispose();
    }

    public void update() {
        this.position.x += v;
    }
}