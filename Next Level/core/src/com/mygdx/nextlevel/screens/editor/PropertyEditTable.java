/*                             WARNING                                         */
/* IntelliJ might mark this file as having an error regarding the line:        */
/*     Property prop = f.getDeclaredAnnotation(Property.class);                */
/* This is not an error, IntelliJ will suggest switching language versions.    */
/* It is not necessary to downgrade to Java 8                                  */

package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A special table that enables the editing of the properties of a class/object.
 * This is for use in the editor to allow easy modification/addition of new properties
 * to the levels, and for editing properties of objects placed in game.
 */
public class PropertyEditTable extends VisTable {
    /**
     * This contains a property that can be edited in the class.
     */
    class EditProperty {
        public String name;
        public Field field;
        public Widget widget;
    }

    /**
     * A group of properties. This allows for properties to be grouped up like the completion
     * flags in the level settings.
     */
    class Group {
        public String name;
        public ArrayList<EditProperty> properties;

        public Group(String name) {
            this.name = name;
            this.properties = new ArrayList<>();
        }
    }

    protected ArrayList<Group> groups;
    protected Class<?> resultClass;

    /**
     * Initialize the table.
     * @param object The object to examine in the property table.
     */
    public PropertyEditTable(Object object) {
        resultClass = object.getClass();

        try {
            setupLists(object.getClass(), object);
            buildTable();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Sets up the actual table information to be displayed on screen.
     */
    protected void buildTable() {
        top();

        for(Group group : groups) {
            float indent = 0.0f;

            if(!group.name.equals("__DEFAULT__")) {
                row();
                add(new VisLabel(group.name)).colspan(2).left();
                indent = 40.0f;
            }

            for(EditProperty property : group.properties) {
                row();
                add(new VisLabel(property.name + ":")).left().padLeft(indent);
                add(property.widget).expandX().fillX();
            }
        }
    }

    /**
     * Sets up lists of groups, and their property lists.
     * @param clazz The type to construct the groups from.
     */
    protected void setupLists(Class<?> clazz, Object object) throws IllegalAccessException {
        groups = new ArrayList<>();

        //Keep track of each group and their names. This is needed because you can't do ArrayList.contains()
        //for the name of a group.
        Hashtable<String, Group> groupsHash = new Hashtable<>();

        //Do one pass to discover the properties of the class
        for(Field f : clazz.getFields()) {
            //IntelliJ displays an error, suggesting to use Java 8. It lies, this code complies and works perfectly
            //fine.
            Property prop = f.getDeclaredAnnotation(Property.class);

            //If the property annotation exists, add it the lists
            if(prop != null) {
                String groupName = prop.group();

                //Add a new group to the list if it does not exist for the group this
                //property belongs to
                if(!groupsHash.containsKey(groupName))
                    groupsHash.put(groupName, new Group(groupName));

                //Initialize the property with metadata about it. This is used later to construct the actual
                //widgets to display information on the property.
                EditProperty editProperty = new EditProperty();
                editProperty.name = prop.displayName().equals("__DEFAULT__") ? f.getName() : prop.displayName();
                editProperty.field = f;
                editProperty.widget = getWidgetForField(f, object);

                groupsHash.get(groupName).properties.add(editProperty);
            }
        }

        //Do another to convert the group hashtable to an arraylist
        for(Group group : groupsHash.values()) {
            groups.add(group);
        }
    }

    /**
     * Creates a widget for the editing of the specified field in the given object. Whatever editing
     * widget is created, the editor portion is initialized to have a value the same as in the passed
     * object.
     *
     * @param field     The field to create the widget for
     * @param object    The object to retrieve the field's default values from
     * @return          An editing widget for the passed field
     * @throws IllegalAccessException
     */
    protected Widget getWidgetForField(Field field, Object object) throws IllegalAccessException {
        Class fieldType = field.getType();
        Object value = field.get(object);

        if(fieldType.equals(Integer.TYPE)) {
            String valueStr = value.toString();
            VisTextField widget = new VisTextField(valueStr);
            widget.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());

            return widget;
        }
        else if(fieldType.equals(Float.TYPE)) {

        }
        else if(fieldType.equals(Boolean.TYPE)) {

        }

        return new Widget();
    }
}
