package com.mygdx.nextlevel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.screens.GameScreen;

import java.util.ArrayList;

public class WorldContactListener implements ContactListener {

    public Player player;
    public Enemy enemy;
    public Checkpoint checkpoint;
    public Block block1;
    public Block block2;
    public Item item;
    public ArrayList<Body> deleteList;

    public boolean touchedItemBlock;
    public boolean touchedPowerUp;
    public boolean landed;
    public boolean jumped;


    public WorldContactListener (GameScreen gameScreen) {
        this.player = gameScreen.getPlayer();
        this.enemy = gameScreen.getEnemy();
        this.checkpoint = gameScreen.getCheckpoint();
        this.block1 = gameScreen.getBlock1();
        this.block2 = gameScreen.getBlock2();
        this.item = gameScreen.getItem();
        this.deleteList = gameScreen.getDeleteList();

        touchedItemBlock = gameScreen.isTouchedItemBlock();
        touchedPowerUp = gameScreen.isTouchedPowerUp();
        landed = gameScreen.isLanded();
        jumped = gameScreen.isJumped();
    }

    @Override
    public void beginContact(Contact contact) { //called when two fixtures begin contact
        if (contact.getFixtureA().getBody().getUserData().equals(player)) {
//					System.out.println("Touching object");
            if (contact.getFixtureB().getBody().getUserData().equals(checkpoint) && !checkpoint.isTriggered()) {
                checkpoint.setTriggered(true);
                checkpoint.changeSpawn(player);
                checkpoint.setTexture(new Texture("checkpoint.png"));
                player.addLife();
                System.out.println(player.getLives());
                return;
            } else if (contact.getFixtureB().getBody().getUserData().equals(block2) && !touchedItemBlock) {
                touchedItemBlock = true;
            } else if (contact.getFixtureB().getBody().getUserData().equals(item) && !touchedPowerUp) {
                touchedPowerUp = true;
            }
//					System.out.println("Touching enemy");
//					System.out.println("Killed enemy");
        }
        if (contact.getFixtureB().getBody().getUserData().equals(player)) {
//					System.out.println("Touching ground");
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        }
        landed = true;
        jumped = false;
    }

    @Override
    public void endContact(Contact contact) { //called when two fixtures stop contact
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (contact.getFixtureA().getBody().getUserData().equals(player)) {
            if (contact.getFixtureB().getBody().getUserData().equals(enemy) || contact.getFixtureB().getBody().getUserData().equals(block1)) {
                deleteList.add(contact.getFixtureB().getBody());
//						spriteDelList.add(enemy.getSprite());
            }
        }
    }
}