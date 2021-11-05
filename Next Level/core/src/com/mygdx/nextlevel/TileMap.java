package com.mygdx.nextlevel;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.nextlevel.actors.Player2;


public class TileMap extends ApplicationAdapter{
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    public void create () {
        tiledMap = new TmxMapLoader().load("test3.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1.0f/32.0f);
    }

    public void loadObjects(){
        MapLayer objectLayer = tiledMap.getLayers().get("Object Layer 1");
        for(MapObject object : objectLayer.getObjects()){
            if(object instanceof TextureMapObject) {
                TextureMapObject mapObject = (TextureMapObject) object;

            }
        }
    }

    public void render (OrthographicCamera camera, Player2 player) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }
}
