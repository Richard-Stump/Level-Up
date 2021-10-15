package com.mygdx.nextlevel.Tiles;

import com.badlogic.gdx.utils.Array;

public class TileSetConfig {
    private String texPath;
    private int tileSize = 64;
    private Array<Array<String>> tileDef;

    public void setTexPath(String texPath) {
        this.texPath = texPath;
    }

    public String getTexPath() {
        return texPath;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileDef(Array<Array<String>> tileDef) {
        this.tileDef = tileDef;
    }

    public Array<Array<String>> getTileDef() {
        return tileDef;
    }
}
