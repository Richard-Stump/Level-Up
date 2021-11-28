package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

import java.lang.reflect.Array;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.HashMap;

public class ObjectSelectionWindow extends VisWindow {
    protected TabbedPane pane;

    public ObjectSelectionWindow(ArrayList<PlaceableObject> objects) {
        super("Objects");

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
            pane.add(new ObjectSelectionTab(name, groups.get(name)));
        }

        setSize(400, 500 * 2 - 50);
        setPosition(0, -50);
    }

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

    public PlaceableObject getCurrentSelection() {
        ObjectSelectionTab tab = (ObjectSelectionTab) pane.getActiveTab();

        return tab.getCurrentSelection();
    }
}

class ObjectSelectionTab extends Tab {
    protected String groupName;
    protected ArrayList<PlaceableObject> objects;

    protected VisScrollPane scrollPane;
    protected VisTable mainTable;
    protected VisTable innerTable;
    protected ButtonGroup<Button> buttonGroup;

    protected final int BUTTONS_PER_ROW = 2;

    public ObjectSelectionTab(String groupName, ArrayList<PlaceableObject> objects) {
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
