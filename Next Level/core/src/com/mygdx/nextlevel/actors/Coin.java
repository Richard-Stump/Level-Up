package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Coin extends Actor2 {
    BoxCollider collider;
    boolean spawned = true;
    public Coin() {

    }

    public Coin(GameScreen2 screen, float x, float y, boolean spawned) {
        super(screen, x, y, 0.5f,0.5f);
        this.spawned = spawned;
        if (this.spawned) {
            System.out.println("dynamic");
            collider = new BoxCollider(this, new Vector2(x,y), new Vector2(0.5f, 0.5f), true);
        } else {
            System.out.println("static");
            collider = new BoxCollider(this, new Vector2(x, y), new Vector2(0.5f, 0.5f), false);
        }
        setRegion(new Texture("coin.png"));
    }

    public void update() {
//        collider.setVelocity(new Vector2(0.0f, collider.getVelocity().y));
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
