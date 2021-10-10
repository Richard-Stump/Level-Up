package com.mygdx.nextlevel;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class Tilemap {
    private int[][]             tiles;
    private Texture             tilesTexture;
    private TiledMapTileSet     tileSet;
    private TiledMap            map;
    private TiledMapTileLayer   mapLayer;
    private int                 mapWidth;
    private int                 mapHeight;

    public Tilemap() {
        tiles = new int[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 2, 2, 0, 0, 0 },
                { 0, 0, 0, 2, 2, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
        };
    }

    public TiledMap createMap(){
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                final int tileId = tiles[row][col];
                final TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(tileSet.getTile(tileId));
                mapLayer.setCell(col, row, cell);
            }
        }

        return map;
    }

    private void init(FileHandle tsConfigFile){
        //load config file
        final Json json = new Json();



    }

    private void initMap(){
        //Create an empty map
        map = new TiledMap();
        mapLayer = new TiledMapTileLayer(mapWidth, mapHeight, 64, 64);
        map.getLayers().add(mapLayer);

    }
}
