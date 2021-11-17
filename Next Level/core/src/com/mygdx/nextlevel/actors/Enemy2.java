package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Enemy2 extends Actor2 {

    protected BoxCollider boxCollider;

    protected float turnTimer;
    protected boolean right = true;
    boolean jump = false;
    protected final static float timeTillTurn = 2.0f;
    Enemy2.Action action;

    public enum Action {
        DEFAULT, JUMP, SHOOT;
    }

    public Enemy2(GameScreen2 screen, float x, float y, Enemy2.Action action) {
        super(screen, x, y, 0.8f, 0.8f);
        boxCollider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(0.8f, 0.8f),
                true
        );
        this.action = action;
        turnTimer = timeTillTurn;
        setRegion(new Texture("enemy.jpg"));
    }

    public void update(float delta) {

        boxCollider.setVelocity(new Vector2(0.0f, boxCollider.getVelocity().y));
        Vector2 dir = new Vector2();
        dir.y = boxCollider.getVelocity().y;
        if (this.action == Action.DEFAULT) {
            if (right) {
                boxCollider.setVelocity(new Vector2(2.0f, boxCollider.getVelocity().y));
            } else {
                boxCollider.setVelocity(new Vector2(-2.0f, boxCollider.getVelocity().y));
            }

            turnTimer -= delta;
            if (turnTimer <= 0.0f) {
                right = !right;
                turnTimer = timeTillTurn;
            }

            setPosition(boxCollider.getPosition());
        } else if (this.action == Action.JUMP) {
            if (right) {
                boxCollider.setVelocity(new Vector2(2.0f, boxCollider.getVelocity().y));
            } else {
                boxCollider.setVelocity(new Vector2(-2.0f, boxCollider.getVelocity().y));
            }
            if (jump) {
                dir.add(0.0f,13.0f);
//                boxCollider.setImpulse();
                jump = false;
            }
            turnTimer -= delta;
            if (turnTimer <= 0.0f) {
                right = !right;
//                boxCollider.setImpulse();
                turnTimer = timeTillTurn;
            }
            boxCollider.setVelocity(dir);



            setPosition(boxCollider.getPosition());
        } else if (this.action == Action.SHOOT) {

        }
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
        if (side == BoxCollider.Side.BOTTOM) {
            jump = true;
        }
    }

    public void dispose() {
        boxCollider.dispose();
    }
}
