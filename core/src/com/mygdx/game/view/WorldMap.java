package com.mygdx.game.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.game.model.maps.Map;

public class WorldMap implements Screen {
    private int width;
    private int height;
    private Map map;
    TiledMap tiledMap;
    TiledMapTileLayer layer;

    public WorldMap(int height, int width) {
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        map = Map.createMap(height, width, 0);

        System.out.println();
        printmap();
    }

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
