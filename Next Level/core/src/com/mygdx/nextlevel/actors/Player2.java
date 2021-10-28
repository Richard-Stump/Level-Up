package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.BoxCollider.Side;

public class Player2 extends Actor2 {
    protected Vector2 position;
    protected Vector2 velocity;
    protected BoxCollider boxCollider;

    private boolean canJump = false;

    public Player2(float x, float y) {
        super(x, y, 1.0f, 1.0f);

        boxCollider = new BoxCollider(this,
                new Vector2(x, x),
                new Vector2(1, 1),
                true);

        position = boxCollider.getPosition();

        setRegion(new Texture("goomba.png"));
    }

    public void update(float delta) {
        Vector2 dir = new Vector2();
        dir.y = boxCollider.getVelocity().y;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dir.add(-5.0f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dir.add(5.0f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(canJump) {
                dir.add(0.0f, 13.0f);
                canJump = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

        }

        boxCollider.setVelocity(dir);
        setPosition(boxCollider.getPosition().x, boxCollider.getPosition().y);
    }

    public void onCollision(Actor2 other, BoxCollider.Side side) {
        System.out.println("On Player Collision, side = " + side.toString());

        if(other instanceof Enemy2 && (side == Side.LEFT || side == Side.RIGHT)) {
            System.out.println("Player die");
        }

        if(side == Side.BOTTOM) {
            canJump = true;
        }
    }
}
