package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.kotcrab.vis.ui.widget.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;

public class AssetTextureEditTable extends VisTable {
    /**
     * This contains a property that can be edited in the class.
     */
    class EditTexture {
        public String name;
        public Field field;
        public Actor widget;
    }

    /**
     * A group of properties. This allows for properties to be grouped up like the completion
     * flags in the level settings.
     */
    class AssetGroups {
        public String name;
        public ArrayList<AssetTextureEditTable.EditTexture> properties;

        public AssetGroups(String name) {
            this.name = name;
            this.properties = new ArrayList<>();
        }
    }

    protected ArrayList<AssetTextureEditTable.AssetGroups> groups;
    protected Object object;

    /**
     * Initialize the table.
     * @param object The object to examine in the property table.
     */
    public AssetTextureEditTable(Object object) {
        this.object = object;

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

        for(AssetGroups group : groups) {
            VisTable groupTable = new VisTable();
            groupTable.top();

            float indent = 0.0f;

            if(!group.name.equals("__DEFAULT__")) {
                groupTable.row();
                groupTable.add(new VisLabel(group.name)).colspan(2).left();
                indent = 40.0f;
            }

            for(EditTexture property : group.properties) {
                groupTable.row();
                groupTable.add(new VisLabel(property.name + ": ")).left().padLeft(indent);
                groupTable.add(property.widget).expandX().fillX();
            }

            add(groupTable).expandX().fillX().pad(10.0f);
            row();
        }
    }

    public void updateObjectProperties() {
        for(AssetTextureEditTable.AssetGroups group : groups) {
            for(AssetTextureEditTable.EditTexture property : group.properties) {
                Field field = property.field;

                try {
                    field.set(object, getPropertyValue(property));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        Hashtable<String, AssetTextureEditTable.AssetGroups> groupsHash = new Hashtable<>();

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
                    groupsHash.put(groupName, new AssetGroups(groupName));

                //Initialize the property with metadata about it. This is used later to construct the actual
                //widgets to display information on the property.
                EditTexture editProperty = new EditTexture();
                editProperty.name = prop.displayName().equals("__DEFAULT__") ? f.getName() : prop.displayName();
                editProperty.field = f;
                editProperty.widget = getWidgetForField(f, object);

                groupsHash.get(groupName).properties.add(editProperty);
            }
        }

        //Do another to convert the group hashtable to an arraylist
        for(AssetGroups group : groupsHash.values()) {
            groups.add(group);
        }
    }

    //====================================================================================
    //                                     WARNING:
    // Long if-else statement chains ahead. This is the only Way I could figure out how to
    // use different widgets types for the different property types.
    //====================================================================================

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
    protected Actor getWidgetForField(Field field, Object object) throws IllegalAccessException {
        Class fieldType = field.getType();
        Object value = field.get(object);

        if(fieldType.equals(Integer.TYPE)) {
            String valueStr = value.toString();
            VisTextField widget = new VisTextField(valueStr);
            widget.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());

            return widget;
        }
        else if(fieldType.equals(Float.TYPE)) {
            VisTextField widget = new VisTextField(value.toString());
            widget.setTextFieldFilter(new VisTextField.TextFieldFilter() {
                @Override
                public boolean acceptChar(VisTextField textField, char c) {
                    if(c == '-' && textField.getCursorPosition() == 0 && !textField.getText().contains("-"))
                        return true;
                    else if(c == '.' && !textField.getText().contains("."))
                        return true;
                    else if (Character.isDigit(c))
                        return true;

                    return false;
                }
            });
            return widget;
        }
        else if(fieldType.equals(Boolean.TYPE)) {
            boolean valueBool = (boolean)value;
            VisCheckBox widget = new VisCheckBox( "", valueBool);
            return widget;
        }
        else if(fieldType.equals(String.class)) {
            VisTextField widget = new VisTextField((String)value);
            return widget;
        }
        else if (fieldType.isEnum()) {
            Object[] values = fieldType.getEnumConstants();

            VisSelectBox widget = new VisSelectBox();
            widget.setItems(values);
            widget.setSelected(value);

            return widget;
        }

        return new Widget();
    }

    /**
     * Gets the value for the specified property. This is used to update properties of
     * the object passed to this class when wanted.
     *
     * @param property The property to retrieve the value of
     * @return An object containing the value of the specified property.
     */
    protected Object getPropertyValue(AssetTextureEditTable.EditTexture property) {
        Field field = property.field;
        Class fieldType = property.field.getType();

        if(fieldType.equals(Integer.TYPE)) {
            VisTextField widget = (VisTextField)property.widget;
            return Integer.parseInt(widget.getText());
        }
        else if(fieldType.equals(Float.TYPE)) {
            VisTextField widget = (VisTextField)property.widget;
            return Float.parseFloat(widget.getText());
        }
        else if(fieldType.equals(Boolean.TYPE)) {
            VisCheckBox widget = (VisCheckBox)property.widget;
            return widget.isChecked();
        }
        else if(fieldType.equals(String.class)) {
            VisTextField widget = (VisTextField)property.widget;
            return widget.getText();
        }
        else if(fieldType.isEnum()) {
            VisSelectBox widget = (VisSelectBox)property.widget;
            return widget.getSelected();
        }

        throw new RuntimeException("Invalid Property Type. None Returned");
    }

}
