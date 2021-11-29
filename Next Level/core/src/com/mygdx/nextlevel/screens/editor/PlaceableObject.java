package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.Texture;

/**
 * A container to store information on objects that can be selected and placed in the editor.
 *
 * A collection of these classes gets compiled from all the classes with the @Placeable annotation.
 */
public class PlaceableObject {
    public Class clazz;
    public String group;
    public String name;
    public Texture texture;

    /**
     * Construct the information from a given class, assuming it has an @Placeable annotation
     *
     * @param clazz
     */
    public PlaceableObject(Class clazz) {
        Placeable pa = (Placeable)clazz.getDeclaredAnnotation(Placeable.class);
        this.name = pa.displayName().equals("__DEFAULT__") ? clazz.getName() : pa.displayName();
        this.group = pa.group().equals("__DEFAULT__") ? "Actors" : pa.group();

        this.clazz = clazz;
        this.texture = new Texture(pa.defaultTexture());
    }
}
