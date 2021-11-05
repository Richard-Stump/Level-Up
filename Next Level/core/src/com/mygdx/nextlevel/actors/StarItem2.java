package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.GameScreenBase;

public class StarItem2 extends Item2 {
    public StarItem2(){}
    public StarItem2(GameScreenBase screen, float x, float y) {
        super(screen, x, y);
        screen.itemToName.put(this, "star.jpg");
        setRegion(new Texture("star.jpg"));
    }
}