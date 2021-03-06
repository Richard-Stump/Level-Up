package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Actor {
    Vector2 spawnpoint;
    int lives;
    boolean powerUp;
    boolean invulnerable = false;
    boolean takeDamage = false;
    boolean slowItem = false;
    boolean star = false;
    boolean fire = false;
    boolean fireflower = false;
    boolean speedItem = false;
    boolean oneUpItem = false;
    boolean lifeStealItem = false;
    boolean mushroom = false;
    Item heldItem = null;

    public Player(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(64.0F, 64.0F);
        this.lives = 3;
        this.powerUp = false;
        this.invulnerable = false;
        this.takeDamage = false;
        this.slowItem = false;
        this.star = false;
        this.mushroom = false;
        this.fire = false;
        this.fireflower = false;
        this.speedItem = false;
        this.oneUpItem = false;
        this.lifeStealItem = false;
        super.setPosition(position.x, position.y);
        this.spawnpoint = this.worldSpawn;
        super.setBody(BodyDef.BodyType.DynamicBody);
        setShape();
        setFixture(density, restitution);
    }


    //Constructor Used for Test cases
    public Player(Vector2 position, float density, float resistution) {
        this.world = null;
        this.sprite = null;
        this.lives = 3;
        this.powerUp = false;
        this.invulnerable = false;
        this.takeDamage = false;
        this.worldSpawn.x = position.x;
        this.worldSpawn.y = position.y;
        this.spawnpoint = this.worldSpawn;
        setSpawnpoint(spawnpoint);
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
        this.fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
        this.fixtureDef.shape = this.shape;
        this.fixtureDef.isSensor = false;
        this.body.createFixture(this.fixtureDef);

        this.fixtureDef.shape.dispose();

        //Bottom Side of Player Fixture
        this.edgeShape.set( (-w / 2.0F + tolerance/2)+0.1f, -h / 2.0F -  2*tolerance, (w / 2.0F - tolerance/2)-0.1f, -h / 2.0F - 2*tolerance); //Bottom
        super.setContactSide(this.bottom);

        //Left Side of Player Fixture
        this.edgeShape.set(-w / 2.0F - tolerance, (-h / 2.0F + tolerance/2)+0.1f, -w / 2.0F - tolerance,(h / 2.0F - tolerance/2)-0.1f); //Left
        super.setContactSide(this.leftSide);

        //Top Side of Player fixture
        this.edgeShape.set( (-w / 2.0F + tolerance/2)+0.1f, (h / 2.0F + 2*tolerance), (w / 2.0F - tolerance/2)-0.1f, (h / 2.0F + 2*tolerance)); //Head
        super.setContactSide(this.head);

        //Right Side of Player Fixture
        this.edgeShape.set(w / 2.0F + tolerance, (-h / 2.0F + tolerance/2)+0.1f, w / 2.0F + tolerance, (h / 2.0F - tolerance/2)-0.1f); //Right Side
        super.setContactSide(this.rightSide);
        this.edgeShape.dispose();
    }

    public void setSpawnpoint(Vector2 position) {
        this.spawnpoint = position;
    }

    public Vector2 getSpawnpoint() {
        return this.spawnpoint;
    }

    public int getLives() { return this.lives; }

    public void addLife(int numLives) {
        this.lives += numLives;
    }

    public void subLife() { this.lives--; }

    public Vector2 getWorldSpawn() { return this.worldSpawn; }

    public void setTexture(Texture texture) {
        this.sprite.setTexture(texture);
    }

    public boolean hasPowerUp() {
        return this.powerUp;
    }

    public void setPowerUp(boolean set) {
        this.powerUp = set;
    }

    public boolean getsInvulnerable() {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public void setTakeDamage(boolean set) {
        this.takeDamage = set;
    }

    public boolean getSlowItem() {
        return this.slowItem;
    }

    public void setSlowItem(boolean slowItem) {
        this.slowItem = slowItem;
    }

    public boolean getStar() {
        return this.star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public boolean getFire() {
        return this.fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public boolean getFireFlower() { return this.fireflower;}

    public void setFireflower(boolean fireflower) { this.fireflower = fireflower;}

    public boolean getSpeedItem() {
        return this.speedItem;
    }

    public void setSpeedItem(boolean speedItem) {
        this.speedItem = speedItem;
    }

    public void setLifeStealItem(boolean lifeStealItem) {
        this.lifeStealItem = lifeStealItem;
    }

    public boolean getLifeStealItem() {
        return this.lifeStealItem;
    }

    public boolean getOneUpItem() {
        return this.oneUpItem;
    }

    public void setOneUpItem(boolean oneUpItem) {
        this.oneUpItem = oneUpItem;
    }

    public Item getHeldItem() {
        return this.heldItem;
    }

    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }

    public boolean getMushroom() {
        return this.mushroom;
    }

    public void setMushroom(boolean set) {
        this.mushroom = set;
    }

    public void death(Checkpoint checkpoint) {
        if (getLives() < 1) { //Player has no lives left
            addLife(3);

            setSpawnpoint(getWorldSpawn());
            Vector2 spawn = getSpawnpoint();
            this.body.setTransform(spawn.x/PIXELS_TO_METERS, spawn.y/PIXELS_TO_METERS, 0);
            checkpoint.setTexture(new Texture("checkpoint2.jpg"));
            checkpoint.setTriggered(false);
        } else { //Player has lives
            Vector2 spawn = getSpawnpoint();
            this.body.setTransform(spawn.x/PIXELS_TO_METERS, spawn.y/PIXELS_TO_METERS, 0);
        }
        this.body.setLinearVelocity(0, 0);
    }
}
