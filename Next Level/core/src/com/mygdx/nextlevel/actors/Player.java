package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.nextlevel.screens.GameScreen;

public class Player extends Actor {
    Vector2 spawnpoint;
    int lives;
    boolean powerUp;
    Vector2 position;

    public Player(Texture texture, World world, Vector2 position, float density, float restitution) {
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(64.0F, 64.0F);
        this.lives = 3;
        this.powerUp = false;
        super.setPosition(position.x, position.y);
        this.spawnpoint = this.worldSpawn;
        super.setBody(BodyDef.BodyType.DynamicBody);
        setShape();
        setFixture(density, restitution);
    }

    private void setShape() {
        this.shape = new PolygonShape();
        this.shape.setAsBox(this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);

        System.out.println("Player Vertices");
        Array<Vector2> verts = new Array<Vector2>();
        Vector2 tmp = new Vector2();
        for (int i = 0; i < this.shape.getVertexCount(); i++) {
            // fill tmp with the vertex
            this.shape.getVertex(i, tmp);
            verts.add(new Vector2(tmp));
            System.out.println(tmp.toString());
        }
    }

    private void setFixture(float density, float restitution) {
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.density = density;
        this.fixtureDef.restitution = restitution;
        this.fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        this.fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
//        EdgeShape feet = new EdgeShape();
//        feet.set(-this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, -this.sprite.getHeight()/2.0F/PIXELS_TO_METERS, this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, -this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
//        this.fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
//        this.fixtureDef.shape = feet;
//        this.body.createFixture(this.fixtureDef);
//        EdgeShape head = new EdgeShape();
//        head.set(-this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS, this.sprite.getWidth()/2.0F/PIXELS_TO_METERS, this.sprite.getHeight()/2.0F/PIXELS_TO_METERS);
//        this.fixtureDef.shape = head;
//        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.shape = this.shape;
        this.body.createFixture(this.fixtureDef);
        this.fixtureDef.shape.dispose();
    }

    public void setSpawnpoint(Vector2 position) {
        this.spawnpoint = position;
    }

    public Vector2 getSpawnpoint() {
        return this.spawnpoint;
    }

    public int getLives() { return this.lives; }

    public void addLife() {
        this.lives++;
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
}
