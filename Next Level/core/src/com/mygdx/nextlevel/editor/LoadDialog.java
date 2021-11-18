package com.mygdx.nextlevel.editor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class LoadDialog extends VisWindow {
    protected VisSelectBox<String> fileSelection;
    protected VisTextButton confirmButton;
    protected VisTextButton cancelButton;

    public LoadDialog(final EditorLevel level, final Stage stage) {
        super("Load From:");

        String[] names = getLevelList(stage);
        if(names != null) {
            fileSelection = new VisSelectBox<>();
            fileSelection.setItems(names);
            confirmButton = new VisTextButton("Confirm");
            cancelButton = new VisTextButton("Cancel");

            top();
            add(fileSelection).expandX().fillX().colspan(2).pad(4.0f);
            row();
            add(confirmButton).pad(4.0f);
            add(cancelButton).pad(4.0f);

            confirmButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String fileName = fileSelection.getSelected();

                    try {
                        level.importFrom(fileName);
                        level.saveName = fileName;
                    } catch (Exception e) {
                        stage.addActor(new MessageDialog("Could not open \"" + fileName + "\" for loading"));
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

            pack();
            centerWindow();
        }
    }

    protected String[] getLevelList(final Stage stage) {
        ArrayList<String> mapList = new ArrayList<>();

        try {
            File dir = new File("./");

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".tmx");
                }
            };

            return dir.list(filter);
        } catch (Exception e) {
            stage.addActor(new MessageDialog("Could not open "));
            close();
            return null;
        }
    }
}