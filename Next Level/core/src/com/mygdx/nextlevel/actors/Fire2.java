package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Fire2 extends Actor2 {
    BoxCollider collider;
    Boolean inital;
    float ogX;

    public Fire2(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 1, 1);
        ogX = x;
        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), true, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.FIRE);
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

        if (Math.abs(ogX - collider.getPosition().x) > 10f) {
            screen.queueActorDespawn(this);
        }
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
