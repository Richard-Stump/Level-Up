package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.GameScreenBase;

public class Enemy2 extends Actor2 {

    protected BoxCollider boxCollider;

    protected float turnTimer;
    protected boolean right = true;
    protected final static float timeTillTurn = 2.0f;

    public Enemy2(GameScreenBase screen, float x, float y) {
        super(screen, x, y, 1, 1);

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
        if(other instanceof Player2 && side == BoxCollider.Side.TOP) {
            screen.queueActorDespawn(this);
        }
        if (other instanceof Player2 && ((Player2) other).getStar()) {
            screen.queueActorDespawn(this);
        }
        if (other instanceof Player2 && ((Player2) other).getLifeStealItem()) {
            ((Player2) other).addLife();
            screen.queueActorDespawn(this);
        }
        if (other instanceof Fire2) {
            screen.queueActorDespawn(this);
        }
    }

    public void dispose() {
        boxCollider.dispose();
    }
}
