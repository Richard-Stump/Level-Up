package com.mygdx.nextlevel.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class TileMapTest {

    public TileMapTest() {

    }

    public void run(){
        FileHandle handle = new FileHandle("Next Level/core/assets/Tiled/next-level_test.tmx");
        Tilemap test = new Tilemap();
        test.load(handle);
    }

    public static void main(String[] args) {
        TileMapTest test = new TileMapTest();
        try {
            test.run();
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
