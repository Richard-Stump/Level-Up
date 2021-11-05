package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Fire2 extends Actor2 {
    BoxCollider collider;
    Boolean inital;

    public Fire2(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 1, 1);
        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), true);
        setRegion(new Texture("fireball.png"));
        inital = true;
    }

    public void update(float delta) {
        if (inital) {
            if (screen.getPlayer().facingRight)
                collider.setVelocity(new Vector2(5.0f, collider.getVelocity().y));
            else {
                collider.setVelocity(new Vector2(-5.0f, collider.getVelocity().y));
                flip(true, false);
            }
            inital = false;
        }

        setPosition(collider.getPosition());
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Enemy2) {
            screen.queueActorDespawn(this);
            screen.getPlayer().setfireSpawn(false);
        }
    }

    public void dispose() {
        collider.dispose();
    }
}
