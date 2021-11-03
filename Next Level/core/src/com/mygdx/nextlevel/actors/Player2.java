package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Player2 extends Actor2 {
    protected Vector2 respawnPosition;  //Position that the player respawns in
    protected BoxCollider boxCollider;

    protected int lifeCount;
    protected Item2 heldItem;

    private boolean canJump = false;
    private boolean respawn = false;
    protected boolean facingRight = true;
    private boolean slowItem = false;
    private boolean speedItem = false;

    private float slowTime = 0;
    private float speedTime = 0;

    public Player2(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 1.0f, 1.0f);

        boxCollider = new BoxCollider(this,
                new Vector2(x, x),
                new Vector2(1, 1),
                true);

        respawnPosition = new Vector2(boxCollider.getPosition());

        lifeCount = 3;

        //The texture region needs to be set for rendering.
        setRegion(new Texture("goomba.png"));
    }

    public void update(float delta) {
        if(respawn) {
            boxCollider.setPosition(respawnPosition);
            respawn = false;
        }

        if (slowItem) {
            slowTime+= delta;
            if (slowTime > 3f) {
                slowItem = false;
                slowTime = 0;
            }
        }

        if (speedItem) {
            speedTime += delta;
            if (speedTime > 3f) {
                speedItem = false;
                speedTime = 0;
            }
        }

        Vector2 dir = new Vector2();
        dir.y = boxCollider.getVelocity().y;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float xSpeed;
            if (slowItem)
                xSpeed = -1f;
            else if (speedItem)
                xSpeed = -9f;
            else
                xSpeed = -5f;
            dir.add(xSpeed, 0);
            if (facingRight) {
                facingRight = false;
                flip(true, false);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            float xSpeed;
            if (slowItem)
                xSpeed = 1f;
            else if (speedItem)
                xSpeed = 9f;
            else
                xSpeed = 5f;
            dir.add(xSpeed, 0);
            if (!facingRight) {
                facingRight = true;
                flip(true, false);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(canJump) {
                dir.add(0.0f, 13.0f);
                canJump = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

        }

        boxCollider.setVelocity(dir);
        setPosition(boxCollider.getPosition().x, boxCollider.getPosition().y);
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Enemy2 && (side == Side.LEFT || side == Side.RIGHT) || other instanceof DeathBlock) {
            lifeCount--;
            respawn = true;

            if(lifeCount < 1)
                screen.setShouldReset(true);
        }

        if(side == Side.BOTTOM) {
            canJump = true;
        }

        if(other instanceof SlowItem2) {
            slowItem = true;
            speedItem = false;
        } else if (other instanceof SpeedItem2) {
            speedItem = true;
            slowItem = false;
        } else if (other instanceof LifeItem2) {
            lifeCount++;
            heldItem = (Item2) other;
        } else if (other instanceof MushroomItem2) {
            heldItem = (Item2) other;
        } else if (other instanceof FireFlowerItem2) {
            heldItem = (Item2) other;
        } else if (other instanceof StarItem2) {
            heldItem = (Item2) other;
        }
    }

    public void onTrigger(Actor2 other, Side side) {
        if(other instanceof CheckPoint2) {
            respawnPosition = ((CheckPoint2)other).collider.getPosition();
        }
    }

    public int getLives() { return lifeCount; }
    public Item2 getHeldItem() { return heldItem; }
    public void setRespawnLocation(Vector2 pos) {
        respawnPosition = new Vector2(pos.x, pos.y);
    }
    public void dispose() {
        boxCollider.dispose();
    }
}
