package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.GameScreenBase;

public class LifeItem2 extends Item2 {
    public LifeItem2() {}
    public LifeItem2(GameScreenBase screen, float x, float y) {
        super(screen, x, y);
        screen.itemToName.put(this, "1up-mushroom.jpeg");
        setRegion(new Texture("1up-mushroom.jpeg"));
    }
}