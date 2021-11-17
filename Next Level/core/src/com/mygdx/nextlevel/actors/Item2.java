package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Item2 extends Actor2 {
    BoxCollider collider;
    private boolean inital;

    public Item2() {
//        collider = new BoxCollider(this, new Vector2(1f, 1f), new Vector2(0.5f, 0.5f), true);
    }

//    public Item2(GameScreen2 screen, float x, float y, boolean isSpawned, boolean isStatic) {
//        super(screen, x, y, 0.f, 0.5f);
//    }

    public Item2(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 0.5f, 0.5f);

        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), true);
        inital = true;
    }

    public void update(float delta) {
        if (inital) {
            if (screen.getPlayer().facingRight)
                collider.setVelocity(new Vector2(2.0f, collider.getVelocity().y));
            else
                collider.setVelocity(new Vector2(-2.0f, collider.getVelocity().y));
            inital = false;
        }

        setPosition(collider.getPosition());
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2) {
            screen.queueActorDespawn(this);
        }
    }

    public void dispose() {
        collider.dispose();
    }
}
