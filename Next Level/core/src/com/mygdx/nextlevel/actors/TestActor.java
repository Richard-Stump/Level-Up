package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class TestActor extends Actor {
    Vector2 spawnpoint;


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

        super.setEdgeShape(); //Only needed if want collisions
    }

    private void setFixture(float density, float restitution) {
        //This Fixture is so that the Actor Interacts with the Worlds
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = density;
        this.fixtureDef.restitution = restitution;
        this.fixtureDef.filter.categoryBits = BLOCK_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        this.fixtureDef.shape = this.shape;
        this.fixtureDef.isSensor = false;
        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.shape.dispose();


        //Only need if wanting to set Contact Sides
        this.edgeShape.set( -w / 2.0F + tolerance, -h / 2.0F -  4*tolerance, w / 2.0F - tolerance, -h / 2.0F - 4*tolerance); //Bottom
        super.setContactSide(this.bottom);

        this.edgeShape.set(-w / 2.0F - 2*tolerance, -h / 2.0F + tolerance, -w / 2.0F - 2*tolerance,h / 2.0F - tolerance); //Left
        super.setContactSide(this.leftSide);

        this.edgeShape.set( -w / 2.0F + tolerance, h / 2.0F + 2*tolerance, w / 2.0F - tolerance, h / 2.0F + 2*tolerance); //Head
        super.setContactSide(this.head);

        this.edgeShape.set(w / 2.0F + 2*tolerance, -h / 2.0F + tolerance, w / 2.0F + 2*tolerance, h / 2.0F - tolerance); //Right Side
        super.setContactSide(this.rightSide);
        this.edgeShape.dispose();
    }
}
