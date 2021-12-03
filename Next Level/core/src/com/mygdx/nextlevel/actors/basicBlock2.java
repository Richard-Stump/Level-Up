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
        displayName = "Basic Block 2",
        textures = { "dirt-grass.png" }
)
public class basicBlock2 extends Actor2 {
    protected boolean spawned;
    protected BoxCollider collider;
    protected ArrayList<Texture> blockTextures;


    //This is here for use by the level editor
    public basicBlock2() {}

    public basicBlock2(GameScreen2 screen, ArrayList<Texture> blockTextures, float x, float y) {
        super(screen, x, y, 1, 1);
        this.blockTextures = blockTextures;

        this.spawned = false;


        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false,
                (short) (CollisionGroups.ACTOR | CollisionGroups.ITEM | CollisionGroups.WORLD), CollisionGroups.BLOCK
        );
        setRegion(blockTextures.get(0));
    }

    public void reset() {
        setRegion(blockTextures.get(0));
    }


    public void dispose() {
        collider.dispose();
    }


}
