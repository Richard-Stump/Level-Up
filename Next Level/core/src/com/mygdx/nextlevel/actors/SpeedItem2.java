package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.GameScreenBase;

public class SpeedItem2 extends Item2 {
    public SpeedItem2(){}
    public SpeedItem2(GameScreenBase screen, float x, float y) {
        super(screen, x, y);
        screen.itemToName.put(this, "speeditem.png");
        setRegion(new Texture("speeditem.png"));
    }
}