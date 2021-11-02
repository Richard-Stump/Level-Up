package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;

public class SpeedItem2 extends Item2 {
    public SpeedItem2(GameScreen2 screen, float x, float y) {
        super(screen, x, y);

        setRegion(new Texture("speeditem.png"));
    }
}