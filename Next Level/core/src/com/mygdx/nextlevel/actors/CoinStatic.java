package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;

@Placeable(
        group = "Items",
        displayName = "Coin",
        textures = { "coin.png" }
)
public class CoinStatic extends Actor2 {
    BoxCollider collider;
    boolean spawned = true;
    boolean reset = false;
//    Player2 player;
    public CoinStatic() {}

    public CoinStatic(GameScreen2 screen, Texture texture, float x, float y) {
        super(screen, x, y, 0.5f, 0.5f);
        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), false, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.ITEM);
        setRegion(texture);
    }

    public void update(float delta) {
//        collider.setVelocity(new Vector2(0.0f, collider.getVelocity().y));
        setPosition(collider.getPosition());
    }

    public void onTrigger(Actor2 other, BoxCollider.Side side) {
//        if(other instanceof Player2) {
//            screen.queueActorDespawn(this);
//        }
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2 || other instanceof PushBlock) {
            screen.queueActorDespawn(this);
//            player.incScore(20);
//            screen.getPlayer().incScore(20);
        }
    }

    public void dispose() {
        collider.dispose();
    }
}
