package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;

public class LifeStealItem2 extends Item2 {
    public LifeStealItem2(GameScreen2 screen, float x, float y) {
        super(screen, x, y);
        screen.itemToName.put(this, "lifesteal-mushroom.png");
        setRegion(new Texture("lifesteal-mushroom.png"));
    }
}