package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;

public class StarItem2 extends Item2 {
    public StarItem2(GameScreen2 screen, float x, float y) {
        super(screen, x, y);

        setRegion(new Texture("star.jpg"));
    }
}