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
    protected boolean drawTexture = false;
    protected boolean checkpointTrigger = false;
    private boolean powerUp; //Check if player has powerup

    //Item Booleans
    private boolean mushroomItem;
    private boolean slowItem;
    private boolean speedItem;
    private boolean lifeStealItem;
    private boolean starItem;
    private boolean fireFlowerItem;

    //Item Timers
    private float slowTime = 0f;
    private float speedTime = 0f;
    private float lifeStealTime = 0f;
    private float starTime = 0f;
    private float fireFlowerTime = 0f;

    //Fire Timer
    private float fireTime = 0f;
    private boolean fireSpawn = false;

    public Player2() {
        lifeCount = 3;
        mushroomItem = false;
        powerUp = false;
        slowItem = false;
        speedItem = false;
        lifeStealItem = false;
        starItem = false;
        fireFlowerItem = false;
    }

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
        powerUp = false;
        mushroomItem = false;
        slowItem = false;
        speedItem = false;
        lifeStealItem = false;
        starItem = false;
        fireFlowerItem = false;
    }

    public void update(float delta) {
        if(respawn) {
            boxCollider.setPosition(respawnPosition);
            setRegion(new Texture("goomba.png"));
            powerUp = false;
            mushroomItem = false;
            slowItem = false;
            speedItem = false;
            lifeStealItem = false;
            starItem = false;
            fireFlowerItem = false;
            heldItem = null;
            respawn = false;
        }

        if (drawTexture) {
            if (starItem)
                setRegion(new Texture("stargoomba.png"));
            else if (fireFlowerItem)
                setRegion(new Texture("firegoomba.png"));
            else if (lifeStealItem)
                setRegion(new Texture("lifesteal-goomba.png"));
            else if (powerUp)
                setRegion(new Texture("paragoomba.png"));
            else
                setRegion(new Texture("goomba.png"));

            if (!facingRight) {
                flip(true, false);
            }

            drawTexture = false;
        }

        if (slowItem) {
            slowTime+= delta;
            if (slowTime > 3f) {
                slowItem = false;
                slowTime = 0f;
            }
        }

        if (speedItem) {
            speedTime += delta;
            if (speedTime > 3f) {
                speedItem = false;
                speedTime = 0f;
            }
        }

        if (starItem) {
            starTime += delta;
            if (starTime > 3f) {
                drawTexture = true;
                starItem = false;
                starTime = 0f;
            }
        }

        if (lifeStealItem) {
            lifeStealTime += delta;
            if (lifeStealTime > 3f) {
                drawTexture = true;
                lifeStealItem = false;
                lifeStealTime = 0f;
            }
        }

        if (fireFlowerItem) {
            fireFlowerTime += delta;
            if (fireFlowerTime > 3f) {
                drawTexture = true;
                fireFlowerItem = false;
                fireFlowerTime = 0f;
            }
        }

        Vector2 dir = new Vector2();
        dir.y = boxCollider.getVelocity().y;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            float xSpeed;
            if (slowItem)
                xSpeed = -1f;
            else if (speedItem || starItem)
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
            else if (speedItem || starItem)
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

        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (heldItem != null) {
                drawTexture = true;
            }

            //Do action based off held item
            if (heldItem instanceof StarItem2) {
                starItem = true;
            } else if (heldItem instanceof FireFlowerItem2) {
                fireFlowerItem = true;
            } else if (heldItem instanceof LifeStealItem2) {
                lifeStealItem = true;
                powerUp = true;
            }
            heldItem = null;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (fireFlowerItem) {
                if (!fireSpawn) {
                    //If fireflower item active spawn fire
                    if (facingRight) { //Send fire to the right direction
                        screen.queueActorSpawn(getX() + 1, getY(), Fire2.class);
                    } else { //Send fire to the left side
                        screen.queueActorSpawn(getX() - 1, getY(), Fire2.class);
                    }
                    fireSpawn = true;
                }
                fireTime++;
            }
        }

        boxCollider.setVelocity(dir);
        setPosition(boxCollider.getPosition().x, boxCollider.getPosition().y);
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        //Check if player collides with enemy
        if(other instanceof Enemy2 && (side == Side.LEFT || side == Side.RIGHT) && !starItem) {
            if (!powerUp) {
                lifeCount--;
                respawn = true;
            }

            drawTexture = true;
            powerUp = false;

            if(lifeCount < 1) {
                screen.setShouldReset(true);
            }
        }

        //Check if player falls off edge
        if (other instanceof DeathBlock) {
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
        } else if (other instanceof MushroomItem2) {
            powerUp = true;
            mushroomItem = true;
            drawTexture = true;
        } else if (other instanceof FireFlowerItem2) {
            if (heldItem == null)
                heldItem = (Item2) other;
        } else if (other instanceof StarItem2) {
            if (heldItem == null)
                heldItem = (Item2) other;
        } else if (other instanceof LifeStealItem2) {
            if (heldItem == null)
                heldItem = (Item2) other;
        }
    }

    public void onTrigger(Actor2 other, Side side) {
        if(other instanceof CheckPoint2 && !checkpointTrigger) {
            addLife();
            respawnPosition = ((CheckPoint2)other).collider.getPosition();
            checkpointTrigger = true;
        }
    }

    public void setfireSpawn(boolean b) { fireSpawn = b; }
    public void addLife() { lifeCount++; }
    public int getLives() { return lifeCount; }
    public Item2 getHeldItem() { return heldItem; }
    public boolean hasPowerUp() {
        return powerUp;
    }
    public boolean getMushroom() {
        return mushroomItem;
    }
    public boolean getLifeStealItem() {
        return this.lifeStealItem;
    }
    public boolean getStar() {
        return starItem;
    }
    public boolean getSpeedItem() {
        return speedItem;
    }
    public boolean getSlowItem() {
        return this.slowItem;
    }
    public void setRespawnLocation(Vector2 pos) { respawnPosition = new Vector2(pos.x, pos.y); }
    public void dispose() { boxCollider.dispose(); }
}
