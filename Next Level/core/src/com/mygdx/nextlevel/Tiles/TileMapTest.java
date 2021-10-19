package com.mygdx.nextlevel.Tiles;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class TileMapTest {
    public Tile groundTile;
    public Body groundTileWorldBody;
    public World world;

    public TileMapTest(World world) {
        this.world = world;
        groundTile = new Tile(new FileHandle("tile1.png"), world, "Static");
        groundTile.setTilePosition(0, 0);

        groundTileWorldBody = world.createBody(groundTile.tileBody);
        groundTileWorldBody.createFixture(groundTile.tileShape, 0.0f);
        groundTile.tileShape.dispose();
    }

    public void updateMap() {

    }


}
