package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;

public class Enemy implements Actor {
    private float moveSpeed;


    public Enemy(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSpeed(Enemy e) {
        return e.moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public boolean getActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public Vector2 getStartPosition() {
        return null;
    }

    @Override
    public Vector2 setStartPosition(Vector2 startPosition) {
        return null;
    }

    @Override
    public Vector2 getStartSize() {
        return null;
    }

    @Override
    public Vector2 setStartSize(Vector2 startSize) {
        return null;
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public Vector2 setPosition(Vector2 position) {
        return null;
    }

    @Override
    public Vector2 getSize() {
        return null;
    }

    @Override
    public Vector2 setSize(Vector2 size) {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onCollisionEnter() {

    }

    @Override
    public void onCollisionStay() {

    }

    @Override
    public void onCollisionExit() {

    }
}
