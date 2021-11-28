package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.Texture;

public class PlaceableObject {
    public Class clazz;
    public String group;
    public String name;
    public Texture texture;

    public PlaceableObject(Class clazz) {
        Placeable pa = (Placeable)clazz.getDeclaredAnnotation(Placeable.class);
        this.name = pa.displayName().equals("__DEFAULT__") ? clazz.getName() : pa.displayName();
        this.group = pa.group().equals("__DEFAULT__") ? "Actors" : pa.group();

        this.clazz = clazz;
        this.texture = new Texture(pa.defaultTexture());
    }
}
