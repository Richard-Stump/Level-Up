package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Fire extends Actor {
    public Vector2 position;
    public float v;
    public boolean remove = false;

    public Fire(Vector2 playerLocation, Texture texture, World world, float v) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.position = playerLocation;
        this.sprite.setSize(20.0F, 20.0F);
        this.v = v;
//        super.setPosition(position.x, position.y);
//        super.setBody(BodyDef.BodyType.DynamicBody);
//        setShape();
//        setFixture();
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
        this.fixtureDef.isSensor = true;
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }

    public void update() {
        position.x += v;
        if (position.x > Gdx.graphics.getWidth()) {
            remove = true;
        }

    }

    public void render(SpriteBatch batch) {
        batch.draw(this.getSprite(),position.x, position.y);
    }
}
