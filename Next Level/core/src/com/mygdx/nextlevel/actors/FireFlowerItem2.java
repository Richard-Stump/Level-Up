package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;

public class FireFlowerItem2 extends Item2 {
    public FireFlowerItem2(GameScreen2 screen, float x, float y, String texture) {
        super(screen, x, y);
        screen.itemToName.put(this, texture);
        setRegion(new Texture(texture));
    }
}