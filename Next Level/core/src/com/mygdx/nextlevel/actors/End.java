package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.CollisionGroups;
import com.mygdx.nextlevel.screens.GameScreen2;
import com.mygdx.nextlevel.screens.editor.Placeable;

@Placeable(
        group = "Stage",
        displayName = "Finish",
        defaultTexture =  "end.jpeg"
)
public class End extends Actor2 {
    protected Player2 player;
    protected BoxCollider collider;

    public End(GameScreen2 screen, Texture texture, float x, float y, Player2 player) {
        super(screen, x, y, 1, 1);
        this.player = player;

        collider = new BoxCollider(this, new Vector2(x, y), new Vector2(1.0f, 1.0f), false, true, CollisionGroups.ACTOR, CollisionGroups.ACTOR);

        setRegion(texture);
    }

    @Override
    public void onCollision(Actor2 other, BoxCollider.Side side) {
        if (other instanceof Player2) {
//            player.setWin(true);
            player.checkConditions(player.getConditions());
            System.out.println("Kill condition: " + player.getKillCondition());
            System.out.println("Coin condition: " + player.getCoinCondition());
            System.out.println("NoKill condition: " + player.getNoKillCondition());
            System.out.println("Jewel condition: " + player.getJewelCondition());
            System.out.println("Kill condition: " + player.getTimeCondition());
            if (player.getKillCondition() && player.getCoinCondition() && player.getNoKillCondition() && player.getJewelCondition() && player.getTimeCondition()) {
                player.setWin(true);
            }
        }
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
