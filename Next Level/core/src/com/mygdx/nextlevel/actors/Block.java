package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Block extends Actor {
    boolean itemBlock; //Boolean for block an item block
    boolean itemSpawned; //Boolean for if the block has spawned items
    short collision; //If collisions are enabled on the block
    Item item;


    public Block(Texture texture, World world, Vector2 position, float density, float restitution, short breakableSides, boolean itemBlock) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(64.0F, 64.0F);
        this.itemBlock = itemBlock;
        this.collision = breakableSides;
        this.itemSpawned = false;
        super.setPosition(position.x, position.y);
        super.setBody(BodyDef.BodyType.StaticBody);
        setShape();
        setFixture(density, restitution);
        this.body.setUserData(this.body);
    }

    //Used for Unit Testing
    public Block(Vector2 position, short breakableSides, boolean spawnItem) {
        this.world = null;
        this.sprite = null;
        this.itemBlock = spawnItem;
        this.collision = breakableSides;
        this.itemSpawned = false;
        this.worldSpawn.x = position.x;
        this.worldSpawn.y = position.y;
        this.body = null;
        this.bodyDef = null;
        this.shape = null;
    }

    private void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);

        super.setEdgeShape();
    }

    private void setFixture(float density, float restitution) {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = density;
        this.fixtureDef.restitution = restitution;
        this.fixtureDef.filter.categoryBits = BLOCK_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        this.fixtureDef.friction = 1f;
        this.fixtureDef.shape = this.shape;
        this.body.createFixture(this.fixtureDef);
        this.shape.dispose();
    }

    public void setCollision(short collision) {
        this.collision = collision;
    }     //Set Collision Sides

    public short getCollision() {
        return this.collision;
    }

    public boolean isItemBlock() {
        return this.itemBlock;
    }

    public boolean isItemSpawned() {
        return this.itemSpawned;
    }

    public void setSpawned(boolean set) {
        this.itemSpawned = set;
    }

    public void setTexture(Texture texture) {
        this.sprite.setTexture(texture);
    }

    public void setItem(Item item) { this.item = item; }

    public Item getItem() {
        return this.item;
    }

    public Vector2 getPosition() { return this.worldSpawn; }
}
