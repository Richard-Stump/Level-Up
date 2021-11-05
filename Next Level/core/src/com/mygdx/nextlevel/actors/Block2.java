package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.GameScreenBase;

import java.util.ArrayList;
import java.util.Random;

public class Block2 extends Actor2 {
    protected boolean spawnItem;
    protected boolean spawned;
    protected BoxCollider collider;
    ArrayList<Class> items;




    public Block2(GameScreenBase screen, float x, float y, boolean spawnItem, ArrayList<Class> item) {
        super(screen, x, y, 1, 1);

        this.spawnItem = spawnItem;
        this.spawned = false;

        items = item;

        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false
        );

        //Setup all items


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
            if(spawnItem) {
                Vector2 pos = collider.getPosition();
                Random rand = new Random();
                Class itemClass = items.get(rand.nextInt(items.size()));
                screen.queueActorSpawn(pos.x, pos.y + 1.0f, itemClass);

                spawnItem = false;
                setRegion(new Texture("used-item-block.jpg"));
            }
        }
    }

    public void dispose() {
        collider.dispose();
    }
}
