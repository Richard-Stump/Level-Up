package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;

@Placeable(
        group = "Items",
        textures = { "jewel.png" },
        displayName = "Jewel"
)
public class Jewel extends Actor2 {
    BoxCollider collider;
    boolean spawned = true;

    public Jewel() {
//        collider = new BoxCollider(this, new Vector2(1,1), new Vector2(0.5f, 0.5f), true, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.ACTOR);
    }

    public Jewel(GameScreen2 screen, Texture texture, float x, float y) {
        super(screen, x, y, 0.5f,0.5f);
        collider = new BoxCollider(this, new Vector2(x,y), new Vector2(0.5f, 0.5f), true, (short) (CollisionGroups.WORLD | CollisionGroups.BLOCK), CollisionGroups.ACTOR);
        setRegion(texture);
    }

    public void update(float delta) {
        setPosition(collider.getPosition());
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2) {
            screen.queueActorDespawn(this);
        }
    }

    public void dispose() {
        collider.dispose();
    }
}
