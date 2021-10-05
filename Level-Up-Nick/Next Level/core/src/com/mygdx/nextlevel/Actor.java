package com.mygdx.nextlevel;

import com.badlogic.gdx.math.Vector2;

public class Actor {
    Vector2 position;
    Vector2 size;

    public void onSpawn() {}    // Called after the Actor is placed in the level
    public void onDespawn() {}  // Called when the Actor is deactivated. Ex: killed

    public void onUpdate(float deltaTime) {}

    // Called when the actor collides with another actor.
    // The collision functions are called when the player collides with an
    // actor that is solid, such as a platform
    public void onCollisionEnter(Actor o) {}
    public void onCollisionStay(Actor o) {}
    public void onCollisionExit(Actor o) {}

    // Called when the actor collides with another actor.
    // The trigger functions are called when the player collides with an
    // actor that is not solid, such as a checkpoint
    public void onTriggerEnter(Actor o) {}
    public void onTriggerStay(Actor o) {}
    public void onTriggerExit(Actor o) {}

    // Getters and setters
    public Vector2 getPosition() { return this.position; }
    public Vector2 getSize()    { return this.size; }

    public void setPosition(Vector2 position) { this.position = position; }
    public void setSize(Vector2 size) { this.size = size; }
}
