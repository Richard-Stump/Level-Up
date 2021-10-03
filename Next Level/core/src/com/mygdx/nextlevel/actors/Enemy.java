package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Enemy {
    Texture texture;
    Sprite sprite;
    BodyDef bodyDef;
    PolygonShape shape;
    FixtureDef fixtureDef;

    final float PIXELS_TO_METERS = 100f;

    final short PHYSICS_ENTITY = 0x1; //0001
    final short BLOCK_ENTITY = 0x1 << 2; //0100
    final short WORLD_ENTITY = 0x1 << 1; //0010

    public Enemy(Texture texture, float density, float restitution) {
        this.texture = texture;
        this.sprite = new Sprite(texture);

        this.sprite.setSize(64, 64);

        setPosition();
        setBody();
        setShape();
        setFixture(density, restitution);
    }

    private void setPosition() {
        this.sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2 - 100);
    }

    private void setBody() {
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
        this.bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/PIXELS_TO_METERS, (sprite.getY() + sprite.getHeight()/2)/PIXELS_TO_METERS);
    }

    private void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(sprite.getWidth()/2/PIXELS_TO_METERS, sprite.getHeight()/2/PIXELS_TO_METERS);
    }

    private void setFixture(float density, float restitution) {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = density;
        this.fixtureDef.restitution = restitution;
        this.fixtureDef.filter.categoryBits = BLOCK_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;

        this.fixtureDef.shape = this.shape;
    }

    public void disposeShape() {
        this.shape.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public PolygonShape getShape() {
        return shape;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }
}
