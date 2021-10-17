package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

import java.util.ArrayList;

public class AssetSelectionPane extends Tab {
    private String name;
    private ScrollPane scrollPane;
    private VisTable table;

    public AssetSelectionPane(String name, ArrayList<String> imageNames) {
        super(false, false);
        this.name = name;

        // This table holds the content the scroll pane will contain
        Table innerTable = new Table();
        innerTable.top();

        // We want the scroll pane to move smoothly, and we don't want the bar to disappear
        scrollPane = new ScrollPane(innerTable, VisUI.getSkin());
        scrollPane.setSmoothScrolling(true);
        scrollPane.setFadeScrollBars(false);

        final float size = 128;
        final float pad = 10.0f;

        // Add the images for each of the resources to the table.
        int i = 0;
        for(String imageName : imageNames) {
            Texture tex = new Texture(imageName);

            TextureRegionDrawable trd = new TextureRegionDrawable(tex);

            ImageButton ib = new ImageButton(trd, trd);

            innerTable.add(ib).expand().fill().padBottom(pad).padTop(pad).size(size);

            // A new row every 2 lines
            if(++i % 2 == 0)
                innerTable.row();
        }

        table = new VisTable();
        table.add(scrollPane).expand().fillX().align(Align.top);
    }

    @Override
    public String getTabTitle() {
        return name;
    }

    @Override
    public Table getContentTable() {
        return table;
    }
}