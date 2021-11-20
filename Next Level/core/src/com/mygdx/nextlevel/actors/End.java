package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;

public class End extends Actor2 {
    boolean activated = false;
    protected Player2 player;

    protected BoxCollider collider;

    public End(GameScreen2 screen, float x, float y, Player2 player) {
        super(screen, x, y, 1, 1);
        this.player = player;

        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(1.0f, 1.0f), false, true, CollisionGroups.ACTOR, CollisionGroups.ACTOR);

        setRegion(new Texture("end.jpeg"));
    }

    @Override
    public void onCollision(Actor2 other, BoxCollider.Side side) {
//        if(other instanceof Player2 && !activated) {
//            activated = true;
////            setRegion(new Texture("end.jpeg"));
//        }
//        if (player.getCondition() == 1 && player.getCondition2() == 2 && player.getCoins() == 5 && player.getEnemiesKilled() == 1) {
//            player.setWin(true);
//        }
//        if (condition == 1 && condition2 == 2 && coin == 5 && enemiesKilled == 1) {
//            win = true;
//        }
//        if (player.getConditions().contains(5)) {
//            player.setWin(true);
//        }
    }
}
