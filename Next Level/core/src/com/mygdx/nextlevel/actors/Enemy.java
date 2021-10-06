package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Enemy {
    Texture texture;
    Sprite sprite;
    BodyDef bodyDef;
    PolygonShape shape;
    FixtureDef fixtureDef;
    Body body;
    World world;
    final float PIXELS_TO_METERS = 100.0F;
    final short PHYSICS_ENTITY = 1;
    final short BLOCK_ENTITY = 4;
    final short WORLD_ENTITY = 2;
    final short BROKEN_ENTITY = 8;
    boolean killable;

    public Enemy(Texture texture, World world, float density, float restitution) {
        this.texture = texture;
        this.world = world;
        sprite = new Sprite(texture);
        sprite.setSize(64.0F, 64.0F);
        this.killable = true;

        setPosition();
        setBody();
        setShape();
        setFixture(density, restitution);
    }

    private void setPosition() {
        sprite.setPosition(-sprite.getWidth()/2.0F, -sprite.getHeight()/2.0F - 100.0F);
    }

    private void setBody() {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
        this.bodyDef.position.set((this.sprite.getX() + this.sprite.getWidth()/2.0F)/PIXELS_TO_METERS, (this.sprite.getY() + this.sprite.getHeight()/2.0F)/PIXELS_TO_METERS);

        this.body = this.world.createBody(this.bodyDef);
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
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY | BROKEN_ENTITY;
        this.fixtureDef.shape = this.shape;
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public Body getBody() {
        return this.body;
    }

    public boolean isKillable() {
        return this.killable;
    }
}
