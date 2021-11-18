package com.mygdx.nextlevel.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

public class ObjectPalette {
    private ArrayList<Image>    images;

    public ObjectPalette() {
        images = new ArrayList<Image>();
    }

    public ObjectPalette(ArrayList<Image> images) {
        this.images = images;
    }

    public void add(Image image) {
        images.add(image);
    }

    public Image getIndexImage(int index) {
        return images.get(index);
    }
}
