package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;

public class Enemy2 extends Actor2 {

    protected BoxCollider boxCollider;

    public Enemy2(float x, float y) {
        super(x, y, 1, 1);

        boxCollider = new BoxCollider(
                this,
                new Vector2(x, y),
                new Vector2(1, 1),
                true
        );

        setRegion(new Texture("enemy.jpg"));
    }

    public void update(float delta) {
        setPosition(boxCollider.getPosition());
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        System.out.println("On Enemy Collision, side = " + side.toString());

        if(side == BoxCollider.Side.LEFT) {
            System.out.println("Enemy die");
        }
    }

}
