package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.GameScreenBase;

public class MushroomItem2 extends Item2 {
    public MushroomItem2() {}

    public MushroomItem2(GameScreenBase screen, float x, float y) {
        super(screen, x, y);
        screen.itemToName.put(this, "mushroom.jpeg");
        setRegion(new Texture("mushroom.jpeg"));
    }
}