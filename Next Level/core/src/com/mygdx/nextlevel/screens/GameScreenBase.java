package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.nextlevel.actors.Actor2;
import com.mygdx.nextlevel.actors.Item2;
import com.mygdx.nextlevel.actors.Player2;

import java.util.HashMap;

public interface GameScreenBase extends Screen {


    /**
     * Queues a new actor to be spawned in the next frame. This is partly to avoid the issue of colliders not being
     * able to be created in the collision handler, but also to ensure all new actors are spawned before the next frame
     * rather than in the middle of actor updated.
     * @param x The actor's x coordinate
     * @param y The actor's y coordinate
     * @param type The type of actor to spawn.
     */
    public void queueActorSpawn(float x, float y, Class<? extends Actor2> type);


    /**
     * Queues an actor to be despawned in the next frame. This avoids the issue of colliders being unable to be destroyed
     * in the collision handlers
     * @param o The object to be destroyed.
     */
    public void queueActorDespawn(Actor2 o);

    public Player2 getPlayer();

    public HashMap<Item2, String> itemToName = new HashMap<>();

    public void setShouldReset(boolean shouldReset);

}
