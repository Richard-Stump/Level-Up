package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class TestActor extends Actor {
    Vector2 spawnpoint;
    FixtureDef bottom, head, leftSide, rightSide;
    float w = 0F;
    float h = 0F;
    EdgeShape edgeShape;

    public TestActor(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(64.0F, 64.0F);
        super.setPosition(position.x, position.y);
        this.spawnpoint = this.worldSpawn;
        super.setBody(BodyDef.BodyType.StaticBody);
        setShape();
        setFixture(density, restitution);
    }

    private void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);

        this.edgeShape = new EdgeShape();
        this.w = this.sprite.getWidth()/PIXELS_TO_METERS;
        this.h = this.sprite.getHeight()/PIXELS_TO_METERS;
        this.edgeShape.set( -w / 2.0F , -h / 2.0F, w / 2.0F, -h / 2.0F); //Set correctly
    }

    private void setFixture(float density, float restitution) {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = 0;
        this.fixtureDef.restitution = 0;
        this.fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        this.fixtureDef.shape = this.shape;
        this.fixtureDef.isSensor = true;
        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.shape.dispose();

        this.edgeShape.set( -w / 2.0F , -h / 2.0F, w / 2.0F, -h / 2.0F); //Bottom
        setSide(density, restitution, this.bottom);

        this.edgeShape.set(-w/2.0F, -h/2.0F, -w/2.0F,h/2.0F); //Left
        setSide(density, restitution, this.leftSide);

        this.edgeShape.set( -w/2.0F, h/2.0F, w/2.0F, h/2.0F); //Head
        setSide(density, restitution, this.head);

        this.edgeShape.set(w/2.0F, -h/2.0F, w/2.0F, h/2.0F); //Right Side
        setSide(density, restitution, this.rightSide);
        edgeShape.dispose();
    }

    private void setSide(float density, float restitution, FixtureDef def) {
        def = new FixtureDef();
        def.density = density;
        def.restitution = restitution;
        def.filter.categoryBits = PHYSICS_ENTITY;
        def.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        def.shape = this.edgeShape;
        this.body.createFixture(def);
    }
}
