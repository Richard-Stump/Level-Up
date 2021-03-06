package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

/**
 * Base class for all the actors in GameScreen2. All actors should derive from this class, and then implement
 * their functionality by overriding the various methods provided:
 *
 *  - update(...):      Called each fame after new actors are spawned, after physics, and before rendering.
 *                      Useful for updating actor velocities and properties.
 *  - onCollision(...): Called when the actor collides with a solid collider.
 *                      Useful for handling interactions between actors colliding, such as the player jumping on the
 *                      enemy's head
 *  - onTrigger(...):   Called when the actor collides with a non-solid actor.
 *                      Used similarly to onCollision(), but with actors that have non-solid boxes like the checkpoint
 *
 * One important thing to note for actors, they need to set the texture region of the sprite super class
 * with setRegion(Texture tex) so they can be displayed. If they don't, the game crashes. Using setRegion() also
 * displays the entire image in the space of the actor. If setTexture() is used, then only a single color shows up
 * for the texture.
 * Todo: Fix this so that there can be invisible actors
 */
public class Actor2 extends Sprite implements Disposable {
    protected GameScreen2 screen;
    boolean active;

    public Actor2() {

    }

    /**
     * Construct the actor with a given position.
     * @param screen    The screen that this object belongs to
     * @param x         The X coordinate to spawn at, specified in world coordinates
     * @param y         The Y Coordinate to spawn at, specified in world coordinates
     */
    public Actor2(GameScreen2 screen, float x, float y) {
        this(screen, x, y, 1.0f, 1.0f);
    }

    /**
     * Construct the actor with a given position and size. Most subclasses override the the other constructor, then
     * use this one internally as they should specify their own size, not the game screen.
     * Subclasses should call this using super(...).
     * @param screen    The screen that this object belongs to
     * @param x         The X coordinate to spawn at, specified in world coordinates
     * @param y         The Y Coordinate to spawn at, specified in world coordinates
     * @param width     The width of the actor, specified in world coordinates
     * @param height    The height of the actor, specified in world coordinates.
     */
    public Actor2(GameScreen2 screen, float x, float y, float width, float height) {
        if(screen == null)
            throw new NullPointerException("Argument \'screen\' cannot be null");

        this.screen = screen;
        this.active = true;
        setSize(width, height);
        setPosition(x, y);

        //One important thing to note for actors, they need to set the texture region of the sprite super class
        // with setRegion(Texture tex) so they can be displayed. If they don't, the game crashes.
        //Todo: Fix this so that there can be invisible actors
    }

    /**
     * This is called every frame after new actors are spawned and after physics update, but before rendering.
     * Use this method to change the actor's velocity/position/etc...
     *
     * @param deltaTime How much time has passed since the previous frame
     */
    public void update(float deltaTime) {
    }

    /**
     * Call this to set the position of the actor's sprite in the world. The units is the number of tiles, so
     * moving 1 unit right moves exactly 1 tile right. This should be called every frame in the actor's update()
     * method. The center of the cell gets the integer coordinate.
     *
     * @param x The sprite's x coordinate in world coordinates
     * @param y The sprite's y coordinate in world coordinates
     */
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(
                x - 0.5f * getWidth(),
                y - 0.5f * getHeight()
        );
    }

    /**
     * Call this to set the position of the actor's sprite in the world. The units is the number of tiles, so
     * moving 1 unit right moves exactly 1 tile right. This should be called every frame in the actor's update()
     * method. The center of the cell gets the integer coordinate.
     *
     * @param pos The sprite's new position in world coordinates
     */
    public void setPosition(Vector2 pos) {
        setPosition(pos.x, pos.y);
    }

    /**
     * Returns the world coordinates of the actors sprite.
     * @return The actor's coordinates
     */
    public Vector2 getPosition() { return new Vector2(getX(), getY()); }

    /**
     * If this actor has a collider, then this method is called when a collision occurs between this and a solid
     * collider (not necessarily attached to an object).
     * @param other The actor that is collided with
     * @param side Which side did the collision occur
     */
    public void onCollision(Actor2 other, BoxCollider.Side side) {

    }

    /**
     * If this actor has a collider, then this method is called when a collision occurs between this and a non-solid
     * collider (not necessarily attached to an object). This method is used for objects that the user can pass through,
     * but still trigger
     * @param other The actor that is collided with
     * @param side Which side did the trigger occur
     */
    public void onTrigger(Actor2 other, BoxCollider.Side side) {

    }

    /**
     * Called when the actor is despawned. It is crucial that colliders are disposed of in this method,
     * otherwise they remain in the world when the actor is despawned.
     */
    public void dispose() {

    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
