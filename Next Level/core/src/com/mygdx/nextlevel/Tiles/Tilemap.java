package com.mygdx.nextlevel.Tiles;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

public class Tilemap{
    private int[][]             tiles;
    private Texture             tilesTexture;
    private TiledMapTileSet     tileSet;
    private TiledMap            map;
    private TiledMapTileLayer   mapLayer;
    private int                 mapWidth;
    private int                 mapHeight;
    private int                 tileSize;
    public FileHandle           tmFile;
    public TiledMapRenderer     tiledMapRenderer;
    public OrthographicCamera   orthographicCamera;

    /*public Tilemap(final FileHandle tsConfigFile) {
        init(tsConfigFile);
    }*/

    public Tilemap() {
        initMap();
    }

    public TiledMap createMap(final FileHandle tmFile){
        parseTileMapFile(tmFile);
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

    public void load(FileHandle tmFile){
        this.tmFile = tmFile;
        Tilemap tm = new Tilemap();
        map = tm.createMap(tmFile);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);


        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthographicCamera.update();
        tiledMapRenderer.setView(orthographicCamera);
        tiledMapRenderer.render();
    }

    public void parseTileMapFile(final FileHandle tmFile){
        //Create tile array
        String input = tmFile.readString();
        String[] info = input.split("\\r?\\n");
        mapHeight = Integer.parseInt(info[0]);
        mapWidth = Integer.parseInt(info[1]);
        final int[][] tiles = new int[mapHeight][mapWidth];
        for (int i = 2; i < info.length; i++) {
            String[] buff = info[i].split(" ");
            for (int j = 0; j < mapWidth; j++) {
                tiles[i-2][j] = Integer.parseInt(buff[j]);
            }
        }
    }

    /*
    private void init(FileHandle tsConfigFile){
        //load config file
        final Json json = new Json();
        final TileSetConfig config = json.fromJson(TileSetConfig.class, tsConfigFile);

        final FileHandle tilesTexHandle = Gdx.files.internal(config.getTexPath());
        if(!tilesTexHandle.exists() || tilesTexHandle.isDirectory()){
            throw new IllegalArgumentException("Tileset Texture Path Invalid");
        }

        tileSize = config.getTileSize();
        if(tileSize != 64){
            throw new IllegalArgumentException("Tile Size Invalid");
        }

        tilesTexture = new Texture(config.getTexPath());

        try {
            initMap();
        } catch (Exception e){
            tilesTexture.dispose();
            throw e;
        }
    }
*/

    private void initMap(){
        final TextureRegion[][] tileSplit = TextureRegion.split(tilesTexture, tileSize, tileSize);
        final int rows = tileSplit.length;



        //Create a tileset
        tileSet = new TiledMapTileSet();
        int tileId = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < tileSplit[i].length; j++) {
                final StaticTiledMapTile tile = new StaticTiledMapTile(tileSplit[i][j]);
                tile.setId(tileId++);
                tileSet.putTile(tile.getId(), tile);
            }
        }


        //Create an empty map
        map = new TiledMap();
        mapLayer = new TiledMapTileLayer(mapWidth, mapHeight, 64, 64);
        map.getLayers().add(mapLayer);
        final Array<Texture> texts = Array.with(tilesTexture);
        map.setOwnedResources(texts);
    }
}
