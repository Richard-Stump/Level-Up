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
    public Item(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.destroy = false;
        this.sprite.setSize(64.0F, 64.0F);
        super.setPosition(position.x, position.y);
        super.setBody(BodyDef.BodyType.StaticBody);
        setShape();
        setFixture(density, restitution);
    }

    private void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
    }

    private void setFixture(float density, float restitution) {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = density;
        this.fixtureDef.restitution = restitution;
        this.fixtureDef.filter.categoryBits = BLOCK_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        this.fixtureDef.shape = this.shape;
        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.isSensor = true;
        this.shape.dispose();
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    private boolean getDestroy() {
        return this.destroy;
    }
}
