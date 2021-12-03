package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;
import com.mygdx.nextlevel.screens.editor.Property;

import java.util.ArrayList;

@Placeable(
        group = "Enemies",
        displayName = "Enemy",
        textures = { "enemy.png" }
)
public class Enemy2 extends Actor2 {
    protected BoxCollider boxCollider;
    BoxCollider playerCollider;

    protected float turnTimer;
    //protected boolean right = true;
    boolean jump = false;
    boolean contactFloor = false;
    Enemy2.Action action;
    boolean fireSpawn = false;
    float fireTimer = 0;
    Player2 player;

    @Property(displayName = "Time Before Turning") public float timeTillTurn = 2.0f;
    @Property(displayName = "Face Right") public boolean right = true;


//    public enum Action {
//        DEFAULT, JUMP, SHOOT;
//    }

    public enum Action {
        DEFAULT(0), JUMP(1), SHOOT(2);
        private final int value;

        Action(final int newValue) { value = newValue; }
    }


    //for use by the level editor
    public Enemy2() {}

    public Enemy2(GameScreen2 screen, ArrayList<Texture> textures, float x, float y, Enemy2.Action action, Player2 player) {
        super(screen, x, y, 0.8f, 0.8f);
        boxCollider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(0.8f, 0.8f),
                true,
                (short) (CollisionGroups.ACTOR | CollisionGroups.WORLD | CollisionGroups.BLOCK),
                CollisionGroups.ENEMY
        );
        playerCollider = player.getBoxCollider();
        this.player = player;
        this.action = action;
        turnTimer = timeTillTurn;
        setRegion(textures.get(action.value));
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
                if (jump) {
                    dir.add(0.0f,8.0f);
//                boxCollider.setImpulse();
                    jump = false;
                }
                boxCollider.setVelocity(new Vector2(2.0f, boxCollider.getVelocity().y));
            } else {
                if (jump) {
                    dir.add(0.0f,8.0f);
                    jump = false;
                }
                boxCollider.setVelocity(new Vector2(-2.0f, boxCollider.getVelocity().y));
            }
            turnTimer -= delta;
            if (turnTimer <= 0.0f) {
                right = !right;
//                if (contactFloor) {
//                    jump = true;
//                }
                turnTimer = timeTillTurn;
            }
            boxCollider.setVelocity(dir);
            setPosition(boxCollider.getPosition());
        } else if (this.action == Action.SHOOT) {
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

//            if  (screen.getBlueFireDespawn()) {
//                System.out.println("Respawn fire");
//                fireSpawn = true;
//            }

            if (playerCollider.getPosition().x > boxCollider.getPosition().x) {
                if (!fireSpawn) {
                    screen.queueActorSpawn(getX() + 1, getY(), BlueFire.class);
                    fireSpawn = true;
                    fireTimer = 0;
                } else {
                    if (fireTimer >= 16f) {
                        fireSpawn = false;
                    } else {
                        fireTimer += 0.1f;
                    }
                }
            } else {
                if (!fireSpawn) {
                    screen.queueActorSpawn(getX() - 1, getY(), BlueFire.class);
                    fireSpawn = true;
                    fireTimer = 0;
                } else {
                    if (fireTimer >= 16f) {
                        fireSpawn = false;
                    } else {
                        fireTimer += 0.1f;
                    }
                }
            }
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
//            contactFloor = true;
            jump = true;
        }
    }

    public Enemy2.Action getAction() { return this.action; }

    public void dispose() {
        boxCollider.dispose();
    }
}
