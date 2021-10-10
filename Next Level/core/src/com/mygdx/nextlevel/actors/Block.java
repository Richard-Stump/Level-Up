package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Block extends Actor {
    boolean breakable;
    boolean spawnItem;
    boolean spawned;

    public Block(Texture texture, World world, Vector2 position, float density, float restitution, boolean breakable, boolean spawnItem) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(64.0F, 64.0F);
        this.breakable = breakable;
        this.spawnItem = spawnItem;
        this.spawned = false;
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
        this.shape.dispose();
    }

    public boolean isBreakable() {
        return this.breakable;
    }

    public boolean canSpawnItem() {
        return this.spawnItem;
    }

    public boolean isSpawned() {
        return this.spawned;
    }

    public void setSpawned(boolean set) {
        this.spawned = set;
    }


}
