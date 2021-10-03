package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Hero {
    private Body body;

    public Hero(World world, float posX, float posY) {
        createBody(world, posX, posY);

    }

    private void createBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / 32, y / 32);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void hit() {
        System.out.println("There is contact.");
    }

}
