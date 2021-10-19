package com.mygdx.nextlevel.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.nextlevel.NextLevel;

public class Tile extends Sprite {

    public Texture tileTexture;
    //public static Tile tiles[] = new Tile[16];
    public BodyDef tileBody;
    public PolygonShape tileShape = new PolygonShape();
    public World world;

    public Tile(FileHandle filename, World world, String type) {
        tileTexture = new Texture(Gdx.files.internal("tile1.png"));
        tileBody = new BodyDef();
        this.world = world;

        switch (type) {
            case ("Static"):
                tileBody.type = BodyDef.BodyType.StaticBody;
                break;
            case ("Kinematic"):
                tileBody.type = BodyDef.BodyType.KinematicBody;
                break;
            case ("Dynamic"):
                tileBody.type = BodyDef.BodyType.DynamicBody;
            default:
                throw new IllegalStateException("Please select a valid Body type");
        }

        tileShape.setAsBox(64, 64);
        //tileShape.dispose();
    }

    public void setTileBody(String type) {
        switch (type) {
            case ("Static"):
                tileBody.type = BodyDef.BodyType.StaticBody;
                break;
            case ("Kinematic"):
                tileBody.type = BodyDef.BodyType.KinematicBody;
                break;
            case ("Dynamic"):
                tileBody.type = BodyDef.BodyType.DynamicBody;
            default:
                throw new IllegalStateException("Please select a valid Body type");
        }
    }

    public void setTilePosition(float x, float y) {
        tileBody.position.set(x, y);
    }






}
