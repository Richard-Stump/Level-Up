package com.mygdx.nextlevel.screens.editor;

import com.kotcrab.vis.ui.widget.VisWindow;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class PropertyWindow extends VisWindow {
    private PropertyEditTable propertyEditTable;
    public PropertyWindow() {
        super("Properties:");

        setSize(400, 250 * 2 - 50);
        setPosition(1600, -50);
    }

    public void setObject(Object object) {
        removeActor(propertyEditTable);
        propertyEditTable = new PropertyEditTable(object);
        add(propertyEditTable);
    }

    public void updateObject() {
        propertyEditTable.updateObjectProperties();
    }
}
