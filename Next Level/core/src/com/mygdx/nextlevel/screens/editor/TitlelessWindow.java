package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TitlelessWindow extends VisWindow {

    public TitlelessWindow() {
        super(" ");


    }

    protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
        super.drawBackground(batch, parentAlpha, x, y);
    }
}
