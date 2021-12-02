package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Item2 extends Actor2 {
    BoxCollider collider;
    private boolean inital;
    Player2 player;

    public Item2() {
//        collider = new BoxCollider(this, new Vector2(1f, 1f), new Vector2(0.5f, 0.5f), true);
    }

//    public Item2(GameScreen2 screen, float x, float y, boolean isSpawned, boolean isStatic) {
//        super(screen, x, y, 0.f, 0.5f);
//    }

    public Item2(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 0.5f, 0.5f);
        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), true, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.ITEM);
        inital = true;
    }

    public Item2(GameScreen2 screen, float x, float y, Player2 player) {
        super(screen, x, y, 0.5f, 0.5f);
        this.player = player;
        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), true, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.ITEM);
        inital = true;
    }

    public void update(float delta) {
        if (!this.active) {
            collider = null;
        } else {
            if (inital) {
                if (screen.getPlayer().facingRight)
                    collider.setVelocity(new Vector2(2.0f, collider.getVelocity().y));
                else
                    collider.setVelocity(new Vector2(-2.0f, collider.getVelocity().y));
                inital = false;
            }
            setPosition(collider.getPosition());
        }
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2) {
            screen.queueActorDespawn(this);
//            if (this instanceof Coin) {
//                int coins = player.getCoins();
//                coins++;
//                player.setCoins(coins);
//            }
//            player.incScore(20);
//            screen.getPlayer().incScore(20);
        } else if (other instanceof DeathBlock || other instanceof PushBlock) {
            screen.queueActorDespawn(this);
        }
    }

    public void dispose() {
        if (collider == null) {
            collider = new BoxCollider(this, new Vector2(0, 0), new Vector2(0.5f, 0.5f), true, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.ITEM);
        }
        collider.dispose();
    }
}
