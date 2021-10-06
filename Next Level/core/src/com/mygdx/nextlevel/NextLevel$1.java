package com.mygdx.nextlevel;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

class NextLevel$1 implements ContactListener {
    NextLevel$1(NextLevel this$0) {
        this.this$0 = this$0;
    }

    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData().equals(this.this$0.player)) {
            System.out.println("Touching enemy");
            System.out.println("Killed enemy");
        }

        if (contact.getFixtureB().getBody().getUserData().equals(this.this$0.player)) {
            System.out.println("Touching ground");
        }

        this.this$0.landed = true;
        this.this$0.jumped = false;
    }

    public void endContact(Contact contact) {
    }

    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (contact.getFixtureA().getBody().getUserData().equals(this.this$0.player)) {
            this.this$0.deleteList.add(contact.getFixtureB().getBody());
            this.this$0.spriteDelList.add(this.this$0.enemy.getSprite());
        }

    }
}