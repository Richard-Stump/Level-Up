package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

import java.util.ArrayList;

/**
 * Window for asset selection in the level editor
 *
 * This is a window that features multiple tabs for sorting assets that can
 * be placed around the level. Each tab should be an @code AssetSelectionPane.
 */
public class AssetSelectorWindow extends VisWindow {
    protected TabbedPane pane;

    /**
     * Constructs the new asset window given a list of textures for tiles and actors
     * @param tiles         A list of tiles that can be selected
     * @param actorTextures A list of actors that can be selected
     */
    public AssetSelectorWindow(ArrayList<Texture> tiles, ArrayList<Texture> actorTextures) {
        super("Level Objects:");
        TableUtils.setSpacingDefaults(this);

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

        pane.add(new AssetSelectionPane("Tiles", tiles));
        pane.add(new AssetSelectionPane("Actors", actorTextures));

        setSize(400, 500 * 2 - 50);
        setPosition(0, -50);
    }

    /**
     * Gets the index for the currently selected item. Does not return any information
     * about which tab is selected
     * @return The current index
     */
    public int getSelectionIndex() {
        AssetSelectionPane currentTab = (AssetSelectionPane)pane.getActiveTab();

        return currentTab.getSelectionIndex();
    }

    /**
     * Returns the name of the tab that is currently selected
     * @return The name of the tab.
     */
    public String getCurrentTabTitle() {
        return pane.getActiveTab().getTabTitle();
    }
}
