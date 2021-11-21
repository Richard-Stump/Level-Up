package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

public class MessageDialog extends VisWindow {
    public MessageDialog(String str) {
        super("Warning:");

        VisTextButton button = new VisTextButton("Okay");
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        top();
        add(str).expandX().fillX().pad(4.0f);
        row();
        add(button).pad(4.0f);
    }
}
