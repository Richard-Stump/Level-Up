package com.mygdx.nextlevel.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class SlowItem extends Item {
    boolean lower = false;
    public SlowItem(Texture texture, World world, Vector2 position, float density, float restitution) {
        super(texture, world, position, density, restitution);
    }
    public void lowerSpeed() {
        this.lower = true;
    }
    public void normalSpeed() {
        this.lower = false;
    }

    public boolean getSlow() {
        return this.lower;
    }
}
