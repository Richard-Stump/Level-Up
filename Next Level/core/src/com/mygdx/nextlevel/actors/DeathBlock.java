package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;
import com.mygdx.nextlevel.screens.GameScreen2;

public class DeathBlock extends Actor2 {
    protected BoxCollider collider;
    protected static float y = -3f;
    protected Player2 player;


    public DeathBlock(GameScreen2 screen, Player2 player, float x) {
        super(screen, x, y, 1, 1);
        this.player = player;

        collider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                false
        );

        setRegion(new Texture("Block.png"));
    }

    public void update(float delta) {
        collider.setPosition(new Vector2(player.getPosition().x, y));
    }

//    public void onCollision(Actor2 other, BoxCollider.Side side) {
//    }

    public void dispose() {
        collider.dispose();
    }
}
