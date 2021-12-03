package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.LevelsDBController;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.EditLevelScreen;
import com.mygdx.nextlevel.screens.GameScreen2;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * The window that handles load/save etc...
 */
public class MenuWindow extends VisWindow {
    Button levelSettingsButton;
    Button saveButton;
    Button testButton;
    Button publishButton;

    EditorLevel level;
    EditLevelScreen screen;

    final float BUTTON_WIDTH = 160.0f;
    final float BUTTON_PADDING = 10.0f;

    public MenuWindow(EditLevelScreen screen, final EditorLevel level, final Stage stage) {
        super("Menu:");

        setMovable(false);

        this.level = level;
        this.screen = screen;

        VisTable table = new VisTable();

        saveButton = new TextButton("Save", VisUI.getSkin());
        table.add(saveButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();
        levelSettingsButton = new TextButton("Level\nSettings", VisUI.getSkin());
        table.add(levelSettingsButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();
        testButton = new TextButton("Test", VisUI.getSkin());
        table.add(testButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();
        publishButton = new TextButton("Publish", VisUI.getSkin());
        table.add(publishButton).width(BUTTON_WIDTH).pad(BUTTON_PADDING).fillY();

        final EditorLevel lev = level;

        final Stage finalStage = stage;

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    File file = level.exportTo(level.info.getId() + ".tmx");
                    String name2 = file.getName();

                    ServerDBHandler handler = new ServerDBHandler();
                    handler.updateLevel(level.info);
                } catch (FileNotFoundException e) {
                    stage.addActor(new MessageDialog("Could not open + \"" + lev.saveName + "\"to save the level"));
                }
            }
        });

        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    File file = level.exportTo(level.info.getId() + ".tmx");
                    String name2 = file.getName();

                    ServerDBHandler handler = new ServerDBHandler();
                    handler.updateLevel(level.info);


                    NextLevel game = screen.getGame();
                    CreatedLevelsDB dbCreated = new CreatedLevelsDB();
                    game.setScreen(new GameScreen2(game, level.info.getId()));

                } catch (FileNotFoundException e) {
                    stage.addActor(new MessageDialog("Could not open + \"" + lev.saveName + "\"to save the level"));
                }
            }
        });

        add(table).fill();

        int numButtons = 4;
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
