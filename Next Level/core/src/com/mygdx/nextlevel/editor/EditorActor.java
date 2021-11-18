package com.mygdx.nextlevel.editor;

/**
 * Class to represent an actor placed in an editor
 *
 * This class exists because there is a large difference in need between
 * an actor in game and in the editor
 */
public class EditorActor {
    public float x, y;
    public int actorId;

    public EditorActor(float x, float y, int actorId) {
        this.x = x;
        this.y = y;
        this.actorId = actorId;
    }
}
