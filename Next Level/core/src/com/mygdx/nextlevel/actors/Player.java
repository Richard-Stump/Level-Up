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
    final short BLOCK_ENTITY = 0x1 << 2; //0100
    final short WORLD_ENTITY = 0x1 << 1; //0010

    public Player(Texture texture, World world, float density, float restitution) {
        this.texture = texture;
        this.world = world;
        sprite = new Sprite(texture);
        sprite.setSize(64, 64);

        setPosition();
        setBody();
        setShape();
        setFixture(density, restitution);
    }

    private void setPosition() {
        this.sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2 + 100);
    }

    private void setBody() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/PIXELS_TO_METERS, (sprite.getY() + sprite.getHeight()/2)/PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

    }

    private void setShape() {
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2/PIXELS_TO_METERS, sprite.getHeight()/2/PIXELS_TO_METERS);
    }

    private void setFixture(float density, float restitution) {
        fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public Body getBody() {
        return body;
    }
}
