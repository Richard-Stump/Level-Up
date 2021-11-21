package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.io.FileNotFoundException;

class SaveAsDialog extends VisWindow {
    protected VisTextField nameField;
    protected VisTextButton confirmButton;
    protected VisTextButton cancelButton;

    public SaveAsDialog(EditorLevel level, final Stage stage) {
        super("Save Map As:");

        setModal(true);
        setMovable(false);
        centerWindow();

        top();
        nameField = new VisTextField("");
        add(nameField).colspan(2).expandX().fillX().pad(4.0f);
        row();
        confirmButton = new VisTextButton("Confirm");
        cancelButton = new VisTextButton("Cancel");
        add(confirmButton).pad(4.0f);
        add(cancelButton).pad(4.0f);
        pack();

        final EditorLevel finalLevel = level;

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = nameField.getText();

                if (!name.endsWith(".tmx"))
                    name = name + ".tmx";

                try {
                    finalLevel.exportTo(name);
                    finalLevel.saveName = name;

                    //save to created database (and publish)
                    /*
                    //TODO: save the tmx file in the database
                    CreatedLevelsDB dbCreated = new CreatedLevelsDB();
                    ServerDBHandler dbServer = new ServerDBHandler();
                    String currUser = LoginScreen.getCurAcc();

                    LevelInfo newLevel = new LevelInfo(dbCreated.generateUniqueID(currUser), name.substring(0, name.length()-4), currUser);
                    File fTmx = new File(name);
                    newLevel.setTmx(fTmx);
                     */

                } catch (FileNotFoundException e) {
                    stage.addActor(new MessageDialog("Could not open \"" + name + "\" to save file"));
                }

                close();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });
    }
}
