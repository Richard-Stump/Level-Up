package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Coin extends Actor2 {
    BoxCollider collider;
    Boolean initial;
    public Coin() {

    }

    public Coin(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 0.5f,0.5f);
        collider = new BoxCollider(this, new Vector2(x,y), new Vector2(0.5f, 0.5f), false, true);
        initial = true;
        setRegion(new Texture("coin.png"));
    }

    public void update() {
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