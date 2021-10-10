package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Enemy extends Actor {
    boolean killable;

    public Enemy(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        sprite = new Sprite(texture);
        sprite.setSize(64.0F, 64.0F);
        this.killable = true;

        super.setPosition(position.x, position.y);
//        super.setBody(BodyDef.BodyType.StaticBody);
        super.setBody(BodyDef.BodyType.DynamicBody);
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
        this.shape.dispose();
    }

    public boolean isKillable() {
        return this.killable;
    }

    public void movement() {
        float timeSeconds = 0f;
        float period = 1f;
        timeSeconds += Gdx.graphics.getRawDeltaTime();
        if(timeSeconds > period){
            timeSeconds -= period;
            move();
        }
        getBody().setLinearVelocity(-1f, getBody().getLinearVelocity().y);
    }
    public void move() {

    }
}
