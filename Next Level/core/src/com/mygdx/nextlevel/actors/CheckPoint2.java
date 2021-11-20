package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;

public class CheckPoint2 extends Actor2 {
    protected boolean activated = false;
    protected Player2 player;
    protected Texture regularTexture;
    protected BoxCollider collider;

    public CheckPoint2(GameScreen2 screen, Texture texture, float x, float y, Player2 player) {
        super(screen, x, y, 1, 1);
        this.player = player;
        this.regularTexture = texture;

        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(1.0f, 1.0f), false, true, CollisionGroups.ACTOR, CollisionGroups.ACTOR);

        setRegion(regularTexture);
    }

    @Override
    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if(other instanceof Player2 && !activated) {
            activated = true;
            player.addLife();
            player.setRespawnLocation(getPosition());
            setRegion(new Texture("checkpoint2.jpg"));
        }
    }

    public void reset() {
        activated = false;
        setRegion(regularTexture);
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) { this.activated = activated; }
}
