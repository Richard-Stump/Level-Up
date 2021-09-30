package com.mygdx.nextlevel.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Hero extends Sprite {
    public World world;
    public Body body;
    public Hero(World world) {
        this.world = world;
        defineHero();
    }
    public void defineHero() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32,32);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        body.createFixture(fdef);
    }
}
