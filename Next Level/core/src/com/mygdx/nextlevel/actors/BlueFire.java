package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;

public class BlueFire extends Actor2 {
    BoxCollider collider;
    float ogX;
    Player2 player;
    BoxCollider playerCollider;
    protected boolean initial;

    public BlueFire(GameScreen2 screen, float x, float y, Player2 player) {
        super(screen, x, y, 1, 1);
        ogX = x;
        this.player = player;
        playerCollider = player.getBoxCollider();
        initial = true;
//        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), true);

        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), true, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.FIRE);

        setRegion(new Texture("blue-fire.png"));
    }

//    public void update(float delta) {
//        if (playerCollider.getPosition().x > collider.getPosition().x) {
//            collider.setVelocity(new Vector2(5.0f, collider.getVelocity().y));
//        } else {
//            collider.setVelocity(new Vector2(-3.0f, collider.getVelocity().y));
//        }
//        setRegion(new Texture("blue-fire.png"));
//    }

    public void update(float delta) {
//        setPosition(collider.getPosition());
        if (initial) {
            if (playerCollider.getPosition().x > collider.getPosition().x) {
                collider.setVelocity(new Vector2(4.0f, collider.getVelocity().y));
            } else {
                collider.setVelocity(new Vector2( - 4.0f, collider.getVelocity().y));
            }
            initial = false;
        }
        setPosition(collider.getPosition());

        if (Math.abs(ogX - collider.getPosition().x) > 10f) {
            screen.queueActorDespawn(this);
        }
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2) {
            screen.queueActorDespawn(this);
//            screen.getPlayer().setfireSpawn(false);
        }
    }

    public void dispose() {
        collider.dispose();
    }
}