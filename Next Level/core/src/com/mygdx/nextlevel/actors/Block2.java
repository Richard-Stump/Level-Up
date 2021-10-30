package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Block2 extends Actor2 {
    protected boolean spawnItem;
    protected boolean spawned;
    protected BoxCollider collider;

    public Block2(GameScreen2 screen, float x, float y, boolean spawnItem) {
        super(screen, x, y, 1, 1);

        this.spawnItem = spawnItem;
        this.spawned = false;

        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false
        );

        setRegion(new Texture("Block.png"));
    }

    public void update(float delta) {

    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2 && side == Side.BOTTOM) {
            if(spawnItem) {
                Vector2 pos = collider.getPosition();
                screen.queueActorSpawn(pos.x, pos.y + 1.0f, Item2.class);
                spawnItem = false;
            }

        }
    }

    public void dispose() {
        collider.dispose();
    }
}
