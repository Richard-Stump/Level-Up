package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player {
    Texture player;
    Sprite playerSprite;
    BodyDef bodyPlayerDef;
    PolygonShape shapePlayer;
    FixtureDef playerFixtureDef;

    final float PIXELS_TO_METERS = 100f;

    final short PHYSICS_ENTITY = 0x1; //0001
    final short BLOCK_ENTITY = 0x1 << 2; //0100
    final short WORLD_ENTITY = 0x1 << 1; //0010

    public Player(Texture texture, float density, float restitution) {
        this.player = texture;
        this.playerSprite = new Sprite(player);

        playerSprite.setSize(64, 64);

        setPlayerPosition();
        setPlayerBody();
        setPlayerShape();
        setPlayerFixture(density, restitution);
    }

    private void setPlayerPosition() {
        this.playerSprite.setPosition(-playerSprite.getWidth()/2, -playerSprite.getHeight()/2 + 200);
    }

    private void setPlayerBody() {
        this.bodyPlayerDef = new BodyDef();
        this.bodyPlayerDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyPlayerDef.position.set((playerSprite.getX() + playerSprite.getWidth()/2)/PIXELS_TO_METERS, (playerSprite.getY() + playerSprite.getHeight()/2)/PIXELS_TO_METERS);
    }

    private void setPlayerShape() {
        this.shapePlayer = new PolygonShape();
        this.shapePlayer.setAsBox(playerSprite.getWidth()/2/PIXELS_TO_METERS, playerSprite.getHeight()/2/PIXELS_TO_METERS);
    }

    private void setPlayerFixture(float density, float restitution) {
        this.playerFixtureDef = new FixtureDef();
        this.playerFixtureDef.density = density;
        this.playerFixtureDef.restitution = restitution;
        this.playerFixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        this.playerFixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;

        this.playerFixtureDef.shape = this.shapePlayer;
    }

    public Texture getPlayer() {
        return player;
    }

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public BodyDef getBodyPlayerDef() {
        return bodyPlayerDef;
    }

    public PolygonShape getShapePlayer() {
        return shapePlayer;
    }

    public FixtureDef getPlayerFixtureDef() {
        return playerFixtureDef;
    }
}
