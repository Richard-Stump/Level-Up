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

    public Player(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(64.0F, 64.0F);
        this.lives = 3;
        this.powerUp = false;
        this.invulnerable = false;
        this.takeDamage = false;
        super.setPosition(position.x, position.y);
        this.spawnpoint = this.worldSpawn;
        super.setBody(BodyDef.BodyType.DynamicBody);
        setShape();
        setFixture(density, restitution);
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
        return powerUp;
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

    public void death(Checkpoint checkpoint) {
        if (getLives() < 1) { //Player has no lives left
            addLife(3);

            setSpawnpoint(getWorldSpawn());
            Vector2 spawn = getSpawnpoint();
//            while (this.world.isLocked());
            this.body.setTransform(spawn.x/PIXELS_TO_METERS, spawn.y/PIXELS_TO_METERS, 0);
            checkpoint.setTexture(new Texture("checkpoint2.jpg"));
            checkpoint.setTriggered(false);
        } else { //Player has lives
            Vector2 spawn = getSpawnpoint();
//            while (this.world.isLocked());
            this.body.setTransform(spawn.x/PIXELS_TO_METERS, spawn.y/PIXELS_TO_METERS, 0);
        }
        this.body.setLinearVelocity(0, 0);
    }
}
