package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

public class End extends Actor2 {
    boolean activated = false;

    protected BoxCollider collider;

    public End(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 1, 1);

        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(1.0f, 1.0f), false, true);

        setRegion(new Texture("end.jpeg"));
    }

    @Override
    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2 && !activated) {
            activated = true;
            setRegion(new Texture("end.jpeg"));
        }
    }
}
