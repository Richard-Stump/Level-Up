package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.screens.GameScreen2;

public class Coin extends Item2 {
    public Coin() {

    }

    public Coin(GameScreen2 screen, float x, float y, String texture) {
        super(screen, x, y);
        screen.itemToName.put(this, texture);
        setRegion(new Texture(texture));
    }
}
