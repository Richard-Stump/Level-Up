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

    //Item Booleans
    private boolean mushroom = false;
    private boolean powerUp = false;
    private boolean slowItem = false;
    private boolean speedItem = false;
    private boolean lifeStealItem = false;
    private boolean starItem = false;
    private boolean fireFlowerItem = false;

    //Item Timers
    private float slowTime = 0f;
    private float speedTime = 0f;
    private float lifeStealTime = 0f;
    private float starTime = 0f;
    private float fireFlowerTime = 0f;

    public Player2() {
        lifeCount = 3;
        mushroom = false;
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
        mushroom = false;
        powerUp = false;
        slowItem = false;
        speedItem = false;
        lifeStealItem = false;
        starItem = false;
        fireFlowerItem = false;
    }

    public void update(float delta) {
        if(respawn) {
            boxCollider.setPosition(respawnPosition);
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
            //TODO Add Star Functionality
            if (starTime > 3f) {
                drawTexture = true;
                starItem = false;
                starTime = 0f;
            }
        }

        if (lifeStealItem) {
            lifeStealTime += delta;
            //TODO Add LifeSteal Functionality
            if (lifeStealTime > 3f) {
                drawTexture = true;
                lifeStealItem = false;
                lifeStealTime = 0f;
            }
        }

        if (fireFlowerItem) {
            fireFlowerTime += delta;
            //TODO Add FireFlower Functionality
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

        boxCollider.setVelocity(dir);
        setPosition(boxCollider.getPosition().x, boxCollider.getPosition().y);
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Enemy2 && (side == Side.LEFT || side == Side.RIGHT) || other instanceof DeathBlock) {
            if (!powerUp || (other instanceof DeathBlock)) {
                lifeCount--;
                respawn = true;
            }
            drawTexture = true;
            powerUp = false;

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
//            System.out.println(lifeCount);
            lifeCount++;
//            System.out.println(lifeCount);
        } else if (other instanceof MushroomItem2) {
            powerUp = true;
            mushroom = true;
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

    public boolean hasPowerUp() {
        return powerUp;
    }


    public boolean getSlowItem() {
        return this.slowItem;
    }


    public boolean getStar() {
        return starItem;
    }

    public boolean getSpeedItem() {
        return speedItem;
    }


    public boolean getLifeStealItem() {
        return this.lifeStealItem;
    }


    public boolean getMushroom() {
        return mushroom;
    }

    public void onTrigger(Actor2 other, Side side) {
        if(other instanceof CheckPoint2) {
            respawnPosition = ((CheckPoint2)other).collider.getPosition();
        }
    }

    public int getLives() { return lifeCount; }
    public Item2 getHeldItem() { return heldItem; }
    public void setRespawnLocation(Vector2 pos) { respawnPosition = new Vector2(pos.x, pos.y); }
    public void dispose() { boxCollider.dispose(); }
}
