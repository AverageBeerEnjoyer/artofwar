package com.mygdx.game.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.mygdx.game.model.maps.Map;
import controllers.MapToRendererTransformator;

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
        map = new Map(height, width, 0,0);
        MapToRendererTransformator mapToRendererTransformator =new MapToRendererTransformator(map);
        renderer= mapToRendererTransformator.getRenderer();
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
