package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;

import javax.swing.*;

public class Actor2 extends Sprite  {
    public Actor2(float x, float y, float width, float height) {
        setSize(width, height);
        setPosition(x, y);
    }

    public void update(float deltaTime) {

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(
                x - 0.5f * getWidth(),
                y - 0.5f * getHeight()
        );
    }

    public void setPosition(Vector2 pos) {
        setPosition(pos.x, pos.y);
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {

    }
}
