package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;
import com.mygdx.nextlevel.screens.editor.Property;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;

@Placeable(
        group = "Blocks",
        displayName = "Item Block",
        textures = { "item-block.png" }
)
public class Block2 extends Actor2 {
    protected boolean spawnItem;
    protected boolean spawned;
   // protected boolean breakable;
    protected BoxCollider collider;
    protected int itemIndex = 0;
    protected ArrayList<Texture> blockTextures;

    @Property(displayName = "Is Breakable") public boolean breakable;

    ArrayList<Class> items = new ArrayList<>();

    //This is here for use by the level editor
    public Block2() {}

    public Block2(GameScreen2 screen, ArrayList<Texture> blockTextures, float x, float y, boolean spawnItem, int index, boolean breakable) {
        super(screen, x, y, 1, 1);
        this.blockTextures = blockTextures;

        this.spawnItem = spawnItem;
        this.spawned = false;
        this.breakable = breakable;
        if (this.spawnItem && index != GameScreen2.ItemIndex.COIN.getValue() && index != -1) {
            //Setup all items
            items.add(SlowItem2.class);
            items.add(SpeedItem2.class);
            items.add(LifeItem2.class);
            items.add(MushroomItem2.class);
            items.add(StarItem2.class);
            items.add(FireFlowerItem2.class);
            items.add(LifeStealItem2.class);

            itemIndex = index;
        } else if (this.spawnItem && index == GameScreen2.ItemIndex.COIN.getValue()) {
            items.add(Coin.class);
        }
//        else {
//            this.breakable = breakable;
//        }

        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false,
                (short) (CollisionGroups.ACTOR | CollisionGroups.ITEM | CollisionGroups.WORLD | CollisionGroups.ENEMY), CollisionGroups.BLOCK
        );
        setRegion(blockTextures.get(0));
    }

    public void reset() {
        this.spawned = false;
        if (items.contains(Coin.class)) {
            this.breakable = true;
        }
//        if (spawnItem && !breakable) {
//            setRegion(regularTexture);
////        } else if (spawnItem && breakable) {
////            setRegion(new Texture("Block.png"));
//        } else {
//            setRegion(new Texture(regularTexture));
//        }
        setRegion(blockTextures.get(0));
    }

    public void update(float delta) {

    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2 && side == Side.BOTTOM) {
            if(spawnItem && !spawned && !breakable) {
                Vector2 pos = collider.getPosition();
                Class itemClass;
                if (itemIndex == 8) {
                    Random rand = new Random();
                    itemClass = items.get(rand.nextInt(items.size()));
                } else {
                    itemClass = items.get(itemIndex);
                }

                screen.queueActorSpawn(pos.x, pos.y + 1.0f, itemClass);
                screen.blockList.add(this);
                spawned = true;
                setRegion(blockTextures.get(1));
            }
            if (breakable && spawnItem) {
                Vector2 pos = collider.getPosition();
                Class itemClass;
                itemClass = Coin.class;
                screen.queueActorSpawn(pos.x, pos.y +1.0f, itemClass);
                screen.queueActorDespawn(this);
            }
        }
    }
    public void dispose() {
        collider.dispose();
    }

    public boolean isSpawnItem() { return this.spawned; }

    public boolean isBreakable() {
        return this.breakable;
    }
}
