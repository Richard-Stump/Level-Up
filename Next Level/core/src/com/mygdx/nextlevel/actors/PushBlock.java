package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.TileMap;
import com.mygdx.nextlevel.screens.GameScreen2;

public class PushBlock extends Actor2 {
    protected BoxCollider collider;
    protected TileMap tm;
    protected boolean inital;

    public PushBlock(GameScreen2 screen, TileMap tm) {
        super(screen, 0f, tm.getMapHeight()/2f, 1, tm.getMapHeight());
        this.tm = tm;

        collider = new BoxCollider(
                this,
                new Vector2(0f, tm.getMapHeight()/2f),
                new Vector2(1, tm.getMapHeight()),
                true,
                (short) (CollisionGroups.ACTOR | CollisionGroups.WORLD | CollisionGroups.FIRE | CollisionGroups.ITEM),
                CollisionGroups.WORLD
        );

        if (!tm.getAutoScroll()) {
            collider.setStatic();
        }

        inital = true;
    }

    public void update(float delta) {
        if (tm.getAutoScroll()) {
            collider.setVelocity(new Vector2(1.0f, 0f));
        }

        setPosition(collider.getPosition());
        if (tm.getMapWidth() - collider.getPosition().x <= tm.getScreenWidth()) {
            collider.setVelocity(new Vector2(0,0));
            collider.setStatic();
        }
    }

    public void updatePosition(Vector2 vec) {
//        Vector2 v = new Vector2();
//        v.x = vec.x-1f;
        collider.setPosition(vec);
//        collider.setPosition(v);
        setPosition(collider.getPosition());
    }
}
