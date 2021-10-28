package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;

public class Enemy2 extends Actor2 {

    protected BoxCollider boxCollider;

    protected float turnTimer;
    protected boolean right = true;
    protected final static float timeTillTurn = 2.0f;

    public Enemy2(float x, float y) {
        super(x, y, 1, 1);

        boxCollider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                true
        );

        turnTimer = timeTillTurn;
        setRegion(new Texture("enemy.jpg"));
    }

    public void update(float delta) {

        boxCollider.setVelocity(new Vector2(0.0f, boxCollider.getVelocity().y));

        if(right) {
            boxCollider.setVelocity(new Vector2(2.0f, boxCollider.getVelocity().y));
        }
        else {
            boxCollider.setVelocity(new Vector2(-2.0f, boxCollider.getVelocity().y));
        }

        turnTimer -= delta;
        if(turnTimer <= 0.0f) {
            right = !right;
            turnTimer = timeTillTurn;
        }

        setPosition(boxCollider.getPosition());
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        System.out.println("On Enemy Collision, side = " + side.toString());

        if(other instanceof Player2 && side == BoxCollider.Side.TOP) {
            System.out.println("Enemy die");
        }
    }

}
