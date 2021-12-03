package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.TileMap;
import com.mygdx.nextlevel.screens.GameScreen2;

public class EndBlock extends Actor2 {
    protected BoxCollider collider;
    protected TileMap tm;

    public EndBlock(GameScreen2 screen, TileMap tm) {
        super(screen, tm.getMapWidth(), tm.getMapHeight() / 2f, 1, tm.getMapHeight());
        this.tm = tm;

        collider = new BoxCollider(
                this,
                new Vector2(tm.getMapWidth(), tm.getMapHeight() / 2f),
                new Vector2(1, tm.getMapHeight()),
                tm.getAutoScroll(),
                (CollisionGroups.ALL),
                CollisionGroups.WORLD
        );
    }
}
