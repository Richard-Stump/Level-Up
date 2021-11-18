package com.mygdx.nextlevel.editor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.mygdx.nextlevel.screens.EditLevelScreen;

import java.io.FileNotFoundException;

/**
 * The window that handles load/save etc...
 */
public class MenuWindow extends VisWindow {
    Button levelSettingsButton;
    Button saveButton;
    Button loadButton;

    EditorLevel level;

    final float BUTTON_WIDTH = 160.0f;
    final float BUTTON_PADDING = 10.0f;

    public MenuWindow(final EditorLevel level, final Stage stage) {
        super("Menu:");

        this.level = level;

        VisTable table = new VisTable();

        saveButton = new TextButton("Save", VisUI.getSkin());
        table.add(saveButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();
        loadButton = new TextButton("Load", VisUI.getSkin());
        table.add(loadButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();
        levelSettingsButton = new TextButton("Level\nSettings", VisUI.getSkin());
        table.add(levelSettingsButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING);

        final EditorLevel lev = level;

        final Stage finalStage = stage;

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(lev.saveName == null)
                    finalStage.addActor(new SaveAsDialog(level, stage));
                else {
                    try {
                        lev.exportTo(lev.saveName);
                    }
                    catch (FileNotFoundException e) {
                        stage.addActor(new MessageDialog("Could not open + \"" + lev.saveName + "\"to save the level"));
                    }
                }
            }
        });

        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(new LoadDialog(level, stage));
            }
        });

        add(table).fill();

        int numButtons = 3;
        float width = 50 + BUTTON_WIDTH * numButtons + BUTTON_PADDING * numButtons;
        float x = EditLevelScreen.STAGE_WIDTH - width;
        float y = EditLevelScreen.STAGE_HEIGHT;

        setSize(width, 150);
        setPosition(x, y);
    }

    public void addLevelSettingsListener(ClickListener listener) {
        levelSettingsButton.addListener(listener);
    }
}
