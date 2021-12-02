package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;

import java.util.ArrayList;

@Placeable(
        textures = { "checkpoint.png" },
        displayName = "Checkpoint",
        group = "Stage"
)
public class CheckPoint2 extends Actor2 {
    protected boolean activated = false;
    protected Player2 player;
    protected BoxCollider collider;
    protected ArrayList<Texture> textures;

    //for use by the level editor
    public CheckPoint2() {}

    public CheckPoint2(GameScreen2 screen, ArrayList<Texture> textures, float x, float y, Player2 player) {
        super(screen, x, y, 1, 1);
        this.player = player;
        this.textures = textures;

        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(1.0f, 1.0f), false, true, CollisionGroups.ACTOR, CollisionGroups.ACTOR);

        setRegion(textures.get(0));
    }

    @Override
    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2 && !activated) {
            activated = true;
            player.addLife();
            player.setRespawnLocation(new Vector2(getPosition().x + 0.5f, getPosition().y + 0.5f));
            setRegion(textures.get(1));
        }
    }

    public void reset() {
        activated = false;
        setRegion(textures.get(0));
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) { this.activated = activated; }
}
