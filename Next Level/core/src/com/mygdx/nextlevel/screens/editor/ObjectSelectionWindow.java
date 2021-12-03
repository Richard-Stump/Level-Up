package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener;
import com.mygdx.nextlevel.screens.EditLevelScreen;
import jdk.internal.org.jline.reader.Editor;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A window that allows the user to select which object they want to place in the level editor.
 *
 * This class constructs a window with tabs from a list of objects that can be placed in the level.
 * The objects are sorted into tabs based upon what group they are in.
 */
public class ObjectSelectionWindow extends VisWindow {
    protected TabbedPane pane;
    private Object currentObject;
    private PlaceableObject currentPlaceable;
    private EditLevelScreen screen;

    /**
     * Construct the window
     *
     * @param objects A list of objects that the user can place in the editor
     */
    public ObjectSelectionWindow(EditLevelScreen screen, ArrayList<PlaceableObject> objects) {
        super("Objects");

        this.screen = screen;

        //Sort the list of objects into groups
        HashMap<String, ArrayList<PlaceableObject>> groups = groupObjects(objects);

        setResizable(false);
        setMovable(false);

        // This table holds the contents of the tab we are currently looking at.
        // This needs to be updated whenever a different tab is selected because the
        // tab pane doesn't handle the content drawing for us.
        final VisTable table = new VisTable();

        TabbedPane.TabbedPaneStyle style = VisUI.getSkin().get("default", TabbedPane.TabbedPaneStyle.class);
        pane = new TabbedPane(style);
        pane.addListener(new TabbedPaneListener() {
            @Override
            public void switchedTab(Tab tab) {
                update();
            }

            @Override
            public void removedTab(Tab tab) {

            }

            @Override
            public void removedAllTabs() {

            }
        });

        // listener to recreate the tab's content when a new tab is selected
        pane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab (Tab tab) {
                table.clearChildren();
                table.add(tab.getContentTable()).expandX().fillX();
            }
        });

        //The minHeight field ensures that the buttons don't get squashed with the scroll pane
        //increases in height.
        add(pane.getTable()).expandX().fillX().minHeight(50.0f);
        row();
        add(table).expand().fill();

        for(String name : groups.keySet()) {
            pane.add(new ObjectSelectionTab(this, name, groups.get(name)));
        }

        setSize(400, 500 * 2 - 50);
        setPosition(0, -50);

        update();
    }

    /**
     * Construct a hashmap of array lists, where each key is a group and each value is a list of objects belonging
     * to that group.
     *
     * @param objects List of objects to sort into groups
     * @return HashMap of the objects sorted into groups
     */
    protected HashMap<String, ArrayList<PlaceableObject>> groupObjects(ArrayList<PlaceableObject> objects) {
        HashMap<String, ArrayList<PlaceableObject>> groups = new HashMap<>();

        for(PlaceableObject object : objects) {
            ArrayList list = groups.get(object.group);

            if(list == null) {
                list = new ArrayList();
                groups.put(object.group, list);
            }

            list.add(object);
        }

        return groups;
    }

    public void update() {
        ObjectSelectionTab tab = (ObjectSelectionTab) pane.getActiveTab();
        currentPlaceable = tab.getCurrentSelection();

        PlaceableObject po = currentPlaceable;

        try {
            Constructor constructor = po.clazz.getConstructor();
            currentObject = constructor.newInstance();

            screen.linkToPropWindow(currentObject);

            return;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        currentObject = null;
    }

    public PlaceableObject getCurrentPlaceable() {
        return currentPlaceable;
    }

    public Object getCurrentObject() {
        return currentObject;
    }
}

/**
 * A tab representing a group of placeable objects. Each tab is titled the group's name.
 */
class ObjectSelectionTab extends Tab {
    protected String groupName;
    protected ArrayList<PlaceableObject> objects;

    protected VisScrollPane scrollPane;
    protected VisTable mainTable;
    protected VisTable innerTable;
    protected ButtonGroup<Button> buttonGroup;

    protected final int BUTTONS_PER_ROW = 2;

    /**
     * Construct the tab from the group's name, and a list of objects belonging to that group
     * @param groupName The name of the group
     * @param objects A list of objects that belong in this group
     */
    public ObjectSelectionTab(final ObjectSelectionWindow win, String groupName, ArrayList<PlaceableObject> objects) {
        super(false, false);
        this.groupName = groupName;
        this.objects = objects;

        mainTable = new VisTable();
        innerTable = new VisTable();
        innerTable.top();

        scrollPane = new VisScrollPane(innerTable);
        scrollPane.setSmoothScrolling(true);
        scrollPane.setFadeScrollBars(false);

        buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);

        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                win.update();
            }
        };

        final float size = 128 + 48;

        int i = 0;
        for(PlaceableObject object : objects) {
            TextureRegionDrawable trd = new TextureRegionDrawable(object.texture);
            Image im = new Image(trd);
            VisLabel lab = new VisLabel(object.name);

            VisTable buttonTable = new VisTable();
            buttonTable.top();
            buttonTable.add(im).size(size).center();
            buttonTable.row();
            buttonTable.add(lab).width(size).align(Align.left).fillX();

            Button button = new Button(buttonTable, VisUI.getSkin(), "toggle");
            button.addListener(listener);

            buttonGroup.add(button);
            innerTable.add(button).expand().fill();

            if(++i % BUTTONS_PER_ROW == 0)
                innerTable.row();
        }

        scrollPane.setFillParent(true);

        mainTable.add(scrollPane).expand().fill().align(Align.top);
    }

    public PlaceableObject getCurrentSelection() {
        return objects.get(buttonGroup.getCheckedIndex());
    }

    @Override
    public String getTabTitle() {
        return groupName;
    }

    @Override
    public Table getContentTable() {
        return mainTable;
    }
}
