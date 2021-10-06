package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    Texture texture;
    Sprite sprite;
    BodyDef bodyDef;
    PolygonShape shape;
    FixtureDef fixtureDef;
    Body body;
    World world;
    final float PIXELS_TO_METERS = 100f;
    final short PHYSICS_ENTITY = 0x1; //0001
    final short WORLD_ENTITY = 0x1 << 1; //0010
    final short BLOCK_ENTITY = 0x1 << 2; //0100


    public Player(Texture texture, World world, float density, float restitution) {
        this.texture = texture;
        this.world = world;
        sprite = new Sprite(texture);
        sprite.setSize(64.0F, 64.0F);

        setPosition();
        setBody();
        setShape();
        setFixture(density, restitution);
    }

    private void setPosition() {
        this.sprite.setPosition(-this.sprite.getWidth()/2.0F - 300.0F, -this.sprite.getHeight()/2.0F);
    }

    private void setBody() {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
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
        this.fixtureDef.shape = this.shape;
        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.shape.dispose();
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public Body getBody() {
        return this.body;
    }
}
