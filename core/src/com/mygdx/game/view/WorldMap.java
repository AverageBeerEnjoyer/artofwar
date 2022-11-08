package com.mygdx.game.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.mygdx.game.model.maps.Map;
import controllers.MapView;

public class WorldMap implements Screen {
    private int width;
    private int height;
    private Map map;
    private HexagonalTiledMapRenderer renderer;

    public WorldMap(int height, int width) {
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        map = Map.createMap(height, width, 0,0);
        MapView mapView=new MapView(map);
        mapView.create();
        renderer=mapView.getRenderer();
        printmap();
    }

    // FIXME - shitcode for debug only
    private void printmap() {
        System.out.println(map.toString());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
