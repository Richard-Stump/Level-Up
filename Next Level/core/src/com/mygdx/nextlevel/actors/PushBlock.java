package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.TileMap;
import com.mygdx.nextlevel.screens.GameScreen2;

public class PushBlock extends Actor2 {
    protected BoxCollider collider;
    protected TileMap tm;

    public PushBlock(GameScreen2 screen, TileMap tm) {
        super(screen, 0, tm.getMapHeight()/2f, 1, tm.getMapHeight());
        this.tm = tm;

        collider = new BoxCollider(
                this,
                new Vector2(0, tm.getMapHeight()/2f),
                new Vector2(1, tm.getMapHeight()),
                false,
                CollisionGroups.ALL, CollisionGroups.WORLD
        );
    }

    public void update(float delta) {
        if (tm.getAutoScroll()) {
            collider.setPosition(new Vector2(tm.getxAxis() - tm.getScreenWidth()/2f, tm.getMapHeight()/2f));
        }
    }
}
