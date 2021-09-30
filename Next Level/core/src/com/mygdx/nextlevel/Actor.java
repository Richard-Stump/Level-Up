package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;

public interface Actor {
    boolean active = false;
    Vector2 startPosition = new Vector2(0,0);
    Vector2 startSize = new Vector2(10,10);
    Vector2 position = new Vector2(0,0);
    Vector2 size = new Vector2(10,10);

    boolean getActive();
    void setActive(boolean active);
    Vector2 getStartPosition();
    Vector2 setStartPosition(Vector2 startPosition);
    Vector2 getStartSize();
    Vector2 setStartSize(Vector2 startSize);
    Vector2 getPosition();
    Vector2 setPosition(Vector2 position);
    Vector2 getSize();
    Vector2 setSize(Vector2 size);
    void reset();
    void onSpawn();
    void onDespawn();
    void onUpdate();
    void onCollisionEnter();
    void onCollisionStay();
    void onCollisionExit();
}

