package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;
import com.mygdx.nextlevel.screens.GameScreen2;

import java.util.ArrayList;
import java.util.Random;



public class Block2 extends Actor2 {
    protected boolean spawnItem;
    protected boolean spawned;
    protected boolean breakable;
    protected BoxCollider collider;
    protected int itemIndex = 0;

    ArrayList<Class> items = new ArrayList<>();

    public Block2(GameScreen2 screen, float x, float y, boolean spawnItem, int index, boolean breakable) {
        super(screen, x, y, 1, 1);

        this.spawnItem = spawnItem;
        this.spawned = false;
        this.breakable = breakable;
        if (this.spawnItem && !this.breakable) {
//            this.breakable = false;

            //Setup all items
            items.add(SlowItem2.class);
            items.add(SpeedItem2.class);
            items.add(LifeItem2.class);
            items.add(MushroomItem2.class);
            items.add(StarItem2.class);
            items.add(FireFlowerItem2.class);
            items.add(LifeStealItem2.class);

            itemIndex = index;
        }
//        else {
//            this.breakable = breakable;
//        }

        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false
        );

        if (spawnItem && this.breakable) {
            setRegion(new Texture("Block.png"));
        } else if (spawnItem) {
            setRegion(new Texture("item-block.png"));
        } else {
            setRegion(new Texture("Block.png"));
        }
    }

//    public Block2(GameScreen2 screen, float x, float y, boolean spawnItem) {
//        super(screen, x, y, 1, 1);
//
//        this.spawnItem = spawnItem;
//        this.spawned = false;
//        this.breakable = false;
//
//        collider = new BoxCollider(
//                this,
//                new Vector2(x, y),
//                new Vector2(1, 1),
//                false
//        );
//
//        if (spawnItem) {
//            setRegion(new Texture("item-block.png"));
//        } else {
//            setRegion(new Texture("Block.png"));
//        }
//    }

    public Block2(GameScreen2 screen, float x, float y, boolean spawnItem, boolean breakable) {
        super(screen, x, y, 1, 1);

        this.spawnItem = spawnItem;
        this.spawned = false;
        this.breakable = breakable;

        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false
        );

        if (spawnItem && breakable) {
            setRegion(new Texture("Block.png"));
        } else if (spawnItem) {
            setRegion(new Texture("item-block.png"));
        }
        else {
            setRegion(new Texture("Block.png"));
        }
    }

    public void reset() {
        this.spawned = false;

        if (spawnItem) {
            setRegion(new Texture("item-block.png"));
        } else {
            setRegion(new Texture("Block.png"));
        }
    }

    public void update(float delta) {

    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2 && side == Side.BOTTOM) {
            if(spawnItem && !spawned && !breakable) {
                Vector2 pos = collider.getPosition();
                Class itemClass;
                if (itemIndex == 7) {
                    Random rand = new Random();
                    itemClass = items.get(rand.nextInt(items.size()));
                }
//                else if (itemIndex == -1) {
//                    itemClass = Coin.class;
//                }
                else {
                    itemClass = items.get(itemIndex);
                }

                screen.queueActorSpawn(pos.x, pos.y + 1.0f, itemClass);
                screen.blockList.add(this);
                spawned = true;
                setRegion(new Texture("used-item-block.jpg"));
            }
            if (this.breakable && this.spawnItem) {
                System.out.println("here");
                Vector2 pos = collider.getPosition();
                Class itemClass;
                itemClass = Coin.class;
                screen.queueActorSpawn(pos.x, pos.y +1.0f, itemClass);
                System.out.println("item spanwed");
                screen.queueActorDespawn(this);
            }
        }
    }

    public void dispose() {
        collider.dispose();
    }

    public boolean isSpawnItem() { return this.spawned; }
}
