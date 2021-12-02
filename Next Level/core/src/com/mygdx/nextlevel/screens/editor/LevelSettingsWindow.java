package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.EditLevelScreen;

public class LevelSettingsWindow extends VisWindow {
    protected PropertyEditTable propertyTable;
    protected VisTextButton applyButton;
    protected VisTextButton cancelButton;
    protected Actor previousScrollFocus;

    public LevelSettingsWindow(EditLevelScreen screen, final EditorLevel level) {
        super("Level Settings");

        screen.setScrollFocus(this);
        centerWindow();
        setModal(true);
        setMovable(false);

        propertyTable = new PropertyEditTable(level);
        add(propertyTable).colspan(2);

        final Stage stage = screen.getStage();
        previousScrollFocus = stage.getScrollFocus();

        applyButton = new VisTextButton("Apply");
        applyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                propertyTable.updateObjectProperties();
                level.updateFromProperties();
                close();

                //TODO: update the level.info object to reflect the changes so we can update the entry in the database

                ServerDBHandler dbServer = new ServerDBHandler();
                dbServer.updateLevel(level.info);
                dbServer.closeConnection();

                stage.setScrollFocus(previousScrollFocus);
            }
        });

        cancelButton = new VisTextButton("Cancel");
        cancelButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                close();
                stage.setScrollFocus(previousScrollFocus);
            }
        });

        row();
        add(applyButton);
        add(cancelButton);

        padTop(40.0f);
        pack();
    }
}
