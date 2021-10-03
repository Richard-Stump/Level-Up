package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Enemy extends Image {
    private Body body;
    private World world;

    public Enemy(World aWorld, float posX, float posY) {
        super(new Texture("test.png"));
        this.setPosition(posX, posY);
        world = aWorld;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX, posY);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.getWidth()/2, this.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
        this.setOrigin(this.getWidth()/2, this.getHeight()/2);
    }

}
