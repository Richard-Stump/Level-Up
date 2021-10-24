package com.mygdx.nextlevel.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HoverListener extends ClickListener {
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        //Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
        Pixmap pixmap = new Pixmap(Gdx.files.internal("handcursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 0, 0));
        //System.out.println("im here");
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        //System.out.println("away from here");
    }

}
