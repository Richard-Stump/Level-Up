package com.mygdx.nextlevel.screens.editor;

import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

import java.util.ArrayList;

public class AssetSelectorWindow extends VisWindow {
    public AssetSelectorWindow() {
    super("Level Objects:");
    TableUtils.setSpacingDefaults(this);

    setResizable(false);
    setMovable(false);

    // This table holds the contents of the tab we are currently looking at.
    // This needs to be updated whenever a different tab is selected because the
    // tab pane doesn't handle the content drawing for us.
    final VisTable table = new VisTable();

    TabbedPane.TabbedPaneStyle style = VisUI.getSkin().get("default", TabbedPane.TabbedPaneStyle.class);
    final TabbedPane pane = new TabbedPane(style);

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

    ArrayList<String> tileNames = new ArrayList<String>() {{
        add("block.png");
        add("checkpoint.png");
        add("flag.png");
        add("item-block.png");
        add("pipe.png");
        add("tile1.png");
        add("tile2.png");
        add("used-item-block.png");
    }};

    ArrayList<String> actorNames = new ArrayList<String>() {{
        add("badlogic.jpg");
        add("enemy.jpg");
        add("goomba.png");
        add("mushroom.jpeg");
    }};

    ArrayList<String> enemyNames = new ArrayList<String>() {{
        add("goomba.png");
        add("paragoomba.png");
    }};

    pane.add(new AssetSelectionPane("Tiles", tileNames));
    pane.add(new AssetSelectionPane("Actors", actorNames));
    pane.add(new AssetSelectionPane("Enemies", enemyNames));

    setSize(400, 500 * 2 - 50);
    setPosition(0, -50);
}
}
