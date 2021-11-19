package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;

public class DeathBlock extends Actor2 {
    protected BoxCollider collider;
    protected static float y = -3f; //level for which player will die at

    public DeathBlock(GameScreen2 screen, int width) {
        super(screen, width/2f, y, width, 1);

        collider = new BoxCollider(
                this,
                new Vector2(width/2f, y),
                new Vector2(width, 0),
                false,
                CollisionGroups.ALL, CollisionGroups.WORLD
        );
    }
}
