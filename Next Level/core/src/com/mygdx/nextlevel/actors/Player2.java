package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;
import com.sun.tools.javac.comp.Check;

import java.util.ArrayList;

@Placeable(
        group = "Stage",
        displayName = "Player",
        textures = {"hero.png" }
)
public class Player2 extends Actor2 {
    protected Vector2 worldSpawn;
    protected Vector2 respawnPosition;  //Position that the player respawns in
    protected BoxCollider boxCollider;
    protected ArrayList<Texture> textures;

    protected int lifeCount;
    protected Item2 heldItem;
    long score;

    private boolean canJump = false;
    private boolean respawn = false;
    protected boolean facingRight = true;
    protected boolean drawTexture = false;
    protected boolean checkpointTrigger = false;
    private boolean win = false;
    private boolean fail = false;
    private int coin = 0;
    private int enemiesKilled = 0;
    private boolean powerUp; //Check if player has powerup
    private boolean invulernable = false;
    private float time = 2f;
    private boolean launch = false;
    boolean coinConditionMet = false;
    boolean killEnemyConditionMet = false;
    boolean killNoEnemyConditionMet = false;
    boolean jewelConditionMet = false;
    boolean timeLimitConditionMet = false;

    //Item Booleans
    private boolean mushroomItem;
    private boolean slowItem;
    private boolean speedItem;
    private boolean lifeStealItem;
    private boolean starItem;
    private boolean fireFlowerItem;
    private boolean jewel;

    //Item Timers
    private float slowTime = 0f;
    private float speedTime = 0f;
    private float lifeStealTime = 0f;
    private float starTime = 0f;
    private float fireFlowerTime = 0f;
    private float invulerableTime = 0f;
    private float fireTimer = 0.5f;
    private ArrayList<Integer> conditions;

    private double record = 25.00;

    //0 = unconditional, 1 = coins, 2 = kill all enemies, 3 = kill no enemies, 4 = clear level while holding the jewel, 5 = clear within time limit
//    private int condition = 5;
//    private int condition2 = 2;



    //Fire Timer
    private boolean fireSpawn = false;

    public enum PlayerIndex {
        DEFAULT(0), POWERUP(1), STAR(2), FIRE(3), LIFESTEAL(4);
        private final int value;

        PlayerIndex(final int newValue) { value = newValue; }
    }

    public Player2() {
        lifeCount = 3;
        mushroomItem = false;
        powerUp = false;
        slowItem = false;
        speedItem = false;
        lifeStealItem = false;
        starItem = false;
        fireFlowerItem = false;
        jewel = false;
        coin = 0;
        enemiesKilled = 0;
    }

    public Player2(GameScreen2 screen, ArrayList<Texture> textures, float x, float y) {
        super(screen, x, y, 0.8f, 0.8f);
        this.textures = textures;

        boxCollider = new BoxCollider(this,
                new Vector2(x, y),
                new Vector2(0.8f, 0.8f),
                true,
                (short) (CollisionGroups.ACTOR | CollisionGroups.WORLD | CollisionGroups.BLOCK | CollisionGroups.ENEMY), CollisionGroups.ACTOR);
        worldSpawn = new Vector2(boxCollider.getPosition());
        respawnPosition = worldSpawn;
        lifeCount = 3;
        coin = 0;
        score = 0;

        //The texture region needs to be set for rendering.
        setRegion(this.textures.get(PlayerIndex.DEFAULT.value));
        powerUp = false;
        mushroomItem = false;
        slowItem = false;
        speedItem = false;
        lifeStealItem = false;
        starItem = false;
        fireFlowerItem = false;
        jewel = false;
        conditions = new ArrayList<>();
        conditions.add(1);
        conditions.add(2);
        conditions.add(5);
    }

    public void update(float delta) {
        if(respawn) {
            boxCollider.setPosition(respawnPosition);
            setRegion(this.textures.get(PlayerIndex.DEFAULT.value));
            if (!facingRight) {
                flip(true, false);
            }
            powerUp = false;
            mushroomItem = false;
            slowItem = false;
            speedItem = false;
            lifeStealItem = false;
            starItem = false;
            fireFlowerItem = false;
            heldItem = null;
            jewel = false;
            respawn = false;

            coin = 0;
            enemiesKilled = 0;
        }

        if (drawTexture) {
            if (starItem)
                setRegion(this.textures.get(PlayerIndex.STAR.value));
            else if (fireFlowerItem)
                setRegion(this.textures.get(PlayerIndex.FIRE.value));
            else if (lifeStealItem)
                setRegion(this.textures.get(PlayerIndex.LIFESTEAL.value));
            else if (powerUp)
                setRegion(this.textures.get(PlayerIndex.POWERUP.value));
            else
                setRegion(this.textures.get(PlayerIndex.DEFAULT.value));
            if (!facingRight) {
                flip(true, false);
            }

            drawTexture = false;
        }

//        if (launch) {
//            BoxCollider jewelCollider = new BoxCollider()
//        }

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
        if (invulernable) {
            invulerableTime += delta;
            if (invulerableTime >= 1) {
                time--;
                invulerableTime = 0;
                if (time == 0) {
                    invulernable = false;
                    time = 2f;
                }
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
                    fireTimer = 0;
                } else {
                    if (fireTimer >= 1.5f) {
                        fireSpawn = false;
                    } else {
                        fireTimer += 0.1f;
                    }
                }
            }
        }

        boxCollider.setVelocity(dir);
        setPosition(boxCollider.getPosition().x, boxCollider.getPosition().y);
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        //Check if player collides with enemy
        Vector2 pos = boxCollider.getPosition();
        if (other instanceof Enemy2 && (side == Side.LEFT || side == Side.RIGHT || side == Side.TOP) && invulernable) {
            //do nothing
        } else if((other instanceof Enemy2 && (side == Side.LEFT || side == Side.RIGHT || side == Side.TOP) && !starItem && !invulernable) || other instanceof BlueFire) {
            if (jewel) {
                jewel = false;
                screen.queueActorSpawn(pos.x, pos.y + 1.0f, Jewel.class);
                launch = true;
            }
            if (powerUp) {
                powerUp = false;
                invulernable = true;
//                invulernableFunc();
            } else {
                lifeCount--;
                score -= 200;
                if (score < 0) {
                    score = 0;
                }
                respawn = true;
            }

            drawTexture = true;

            if(lifeCount < 1) {
                lifeCount = 3;
                respawnPosition = worldSpawn;

                for(Actor2 actor : screen.checkpointList) {
                    ((CheckPoint2) actor).reset();
                }
            }

            if (!powerUp && !invulernable)
                screen.setShouldReset(true);
        }
        if (other instanceof Enemy2 && side == Side.BOTTOM || other instanceof Enemy2 && (side == Side.RIGHT || side == Side.LEFT) && starItem) {
            incEnemiesKilled();
            score += 50;
//            if (condition == 3 && enemiesKilled > 0) {
//                fail = true;
//            }
        }
        if (other instanceof Jewel) {
            this.jewel = true;
        }

        //Check if player falls off edge
        if (other instanceof DeathBlock) {
            lifeCount--;
            respawn = true;
            score -= 200;
            if (score < 0) {
                score = 0;
            }

            if(lifeCount < 1) {
                lifeCount = 3;
                respawnPosition = worldSpawn;

                for(Actor2 actor : screen.checkpointList) {
                    ((CheckPoint2) actor).reset();
                }
            }

            screen.setShouldReset(true);
        }

        if(side == Side.BOTTOM) {
            canJump = true;
        }

        if(other instanceof SlowItem2) {
            slowItem = true;
            speedItem = false;
//            score += 20;
        } else if (other instanceof SpeedItem2) {
            speedItem = true;
            slowItem = false;
//            score += 20;
        } else if (other instanceof LifeItem2) {
//            lifeCount++;
//            score += 20;
        } else if (other instanceof MushroomItem2) {
            powerUp = true;
            mushroomItem = true;
            drawTexture = true;
//            score += 20;
        } else if (other instanceof FireFlowerItem2) {
            if (heldItem == null) {
                heldItem = (Item2) other;
//                score += 20;
            } else {
                powerUp = true;
                mushroomItem = true;
                drawTexture = true;
//                score += 20;
            }
        } else if (other instanceof StarItem2) {
            if (heldItem == null) {
                heldItem = (Item2) other;
//                score += 20;
            } else {
                powerUp = true;
                mushroomItem = true;
                drawTexture = true;
//                score += 20;
            }
        } else if (other instanceof LifeStealItem2) {
            if (heldItem == null) {
                heldItem = (Item2) other;
//                score += 20;
            } else {
                powerUp = true;
                mushroomItem = true;
                drawTexture = true;
//                score += 20;
            }
        }
//        if ((other instanceof CoinStatic || other instanceof Coin)) {
//            coin++;
////            System.out.println(coin);
//        }
//        if ((other instanceof Coin && other.active)) {
//            coin++;
//        }
    }

    public void onTrigger(Actor2 other, Side side) {
//        if(other instanceof CheckPoint2 && !((CheckPoint2) other).activated) {
//            addLife();
//            System.out.println("Test checkpoint2");
//            respawnPosition = ((CheckPoint2)other).collider.getPosition();
//            ((CheckPoint2)other).setActivated(true);
//        }
//        if (other instanceof Coin) {
//            coin++;
////            System.out.println(coin);
//        }
//        if (other instanceof End) {
//            if (other instanceof Player2) {
////            player.setWin(true);
//                checkConditions(getConditions());
//                System.out.println("Kill condition: " + getKillCondition());
//                System.out.println("Coin condition: " + getCoinCondition());
//                System.out.println("NoKill condition: " + getNoKillCondition());
//                System.out.println("Jewel condition: " + getJewelCondition());
//                System.out.println("Kill condition: " + getTimeCondition());
//                if (getKillCondition() && getCoinCondition() && getNoKillCondition() && getJewelCondition() && getTimeCondition()) {
//                    setWin(true);
//                }
//            }
//                checkConditions(conditions);
//            //Todo: replace coin == ? to a preset number in the level
//            if (condition == 1 && coin == 5) {
//                win = true;
//                //Todo: replace enemiesKilled == ? to a preset number in the level
//            } else if (condition == 2 && enemiesKilled == 2) {
//                win = true;
//            } else if (condition == 3 && enemiesKilled == 0) {
//                win = true;
//            } else if (condition == 4 && jewel) {
//                win = true;
//            }
//            else if (condition == 0) {
//                win = true;
//            } else {
//                System.out.println("Not enough coins or not enough enemies killed.");
//            }

//            if (condition == 1 && condition2 == 2 && coin == 5 && enemiesKilled == 1) {
//                win = true;
//            }
//            System.out.println("Here");
//            if (condition == 5) {
////                System.out.println("Win");
//                win = true;
//            }

//            System.out.println("TEst");
//            if (condition == 1 && condition2 == 2 && coin == 5 && enemiesKilled == 1) {
//                win = true;
//            }


//        }
    }

//    public void invulernableFunc() {
//        invulerableTime += Gdx.graphics.getDeltaTime();
//        if (invulerableTime >= 1) {
//            time--;
//            invulerableTime = 0;
//            if (time == 0) {
//                invulernable = false;
//                time = 5f;
//            }
//        }
//    }

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
    public void setWin(boolean win) { this.win = win; }
    public boolean getWin() {
        return this.win;
    }
    public int getCoins() {
        return this.coin;
    }

    public void setCoins(int num) {
        this.coin = num;
    }

    public void incCoins() {
        this.coin++;
    }

    public void setFail(boolean set) {
        fail = set;
    }
    public boolean getFail() {
        return fail;
    }
    public boolean getJewel() {
        return this.jewel;
    }
    public long getScore() {
        return this.score;
    }

    public void incScore(long score) {
        this.score += score;
    }

    public void setJewel(boolean set) {
        this.jewel = set;
    }

    public boolean getInvulernable() {
        return this.invulernable;
    }

    public void setInvuelnerable(boolean set) {
        this.invulernable = set;
    }

    public BoxCollider getBoxCollider() {
        return this.boxCollider;
    }

    //TODO: temporary check to check record time replacement
    public double getRecordTime() {
        return this.record;
    }

    public void setRecordTime(double time) {
        this.record = time;
    }

    public boolean getLaunch() {
        return this.launch;
    }

    public void setLaunch(boolean launch) {
        this.launch = launch;
    }

    public int getEnemiesKilled() {
        return this.enemiesKilled;
    }
    public void incEnemiesKilled() {
        this.enemiesKilled++;
    }
    public void setRespawnLocation(Vector2 pos) { respawnPosition = new Vector2(pos.x, pos.y); }
    public void dispose() { boxCollider.dispose(); }

    public ArrayList<Integer> getConditions() {
        return conditions;
    }

    public void checkConditions(ArrayList<Integer> conditions) {
        if (conditions.contains(1)) {
            coinConditionMet = false;
            if (coin == 5) {
                coinConditionMet = true;
            }
        } else {
            coinConditionMet = true;
        }
        if (conditions.contains(2)) {
            killEnemyConditionMet = false;
            if (enemiesKilled == 1) {
                killEnemyConditionMet = true;
            }
        } else  {
            killEnemyConditionMet = true;
        }
        if (conditions.contains(3)) {
            killNoEnemyConditionMet = false;
            if (enemiesKilled == 0) {
                killNoEnemyConditionMet = true;
            }
        } else {
            killNoEnemyConditionMet = true;
        }
        if (conditions.contains(4)) {
            jewelConditionMet = false;
            if (jewel) {
                jewelConditionMet = true;
            }
        } else {
            jewelConditionMet = true;
        }
        if (conditions.contains(5)) {
            timeLimitConditionMet = false;
            if (!getFail()) {
                timeLimitConditionMet = true;
            }
        } else {
            timeLimitConditionMet = true;
        }
    }

    public boolean getCoinCondition() {
        return coinConditionMet;
    }

    public boolean getKillCondition() {
        return killEnemyConditionMet;
    }

    public boolean getNoKillCondition() {
        return killNoEnemyConditionMet;
    }

    public boolean getTimeCondition() {
        return timeLimitConditionMet;
    }

    public boolean getJewelCondition() {
        return jewelConditionMet;
    }
}
