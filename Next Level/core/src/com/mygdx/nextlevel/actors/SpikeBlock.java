package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;

import java.util.ArrayList;


@Placeable(
        group = "Blocks",
        displayName = "Spike Block",
        textures = { "spike-block.png" }
)
public class SpikeBlock extends Actor2 {
    protected BoxCollider collider;

    public SpikeBlock(GameScreen2 screen, float x, float y) {
        super(screen, x, y, 1, 1);

        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false,
                CollisionGroups.ALL, CollisionGroups.WORLD
        );
        setRegion(new Texture("spike-block.png"));
    }
}
