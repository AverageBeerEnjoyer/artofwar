package com.mygdx.game.controllers;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.mygdx.game.model.maps.Map;

import static com.mygdx.game.model.ProjectVariables.tileHeight;
import static com.mygdx.game.model.ProjectVariables.tileWidth;

public class MapToRendererTransformator {
    private final HexagonalTiledMapRenderer renderer;
    private TiledMap tiledMap;
    private Map map;
    public MapToRendererTransformator(Map map){
        this.map = map;
        loadNewTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }
    private void loadNewTiledMap() {
        tiledMap = new TiledMap();
        createMapLayer();
    }
    private void createMapLayer() {
        int width = map.getWidth();
        int height = map.getHeight();
        TiledMap tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer lay = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(map.getMapCreator().getCells()[i][j].getType().tile());
                lay.setCell(i, j, cell);
            }
        }
        layers.add(lay);
        this.tiledMap = tiledMap;

    }
    public void update() {
        int width = map.getWidth();
        int height = map.getHeight();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer lay = (TiledMapTileLayer) layers.get(0);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                TiledMapTileLayer.Cell cell = lay.getCell(i, j);
                cell.setTile(map.getMapCreator().getCells()[i][j].getType().tile());
                lay.setCell(i, j, cell);
            }
        }
    }
    public HexagonalTiledMapRenderer getRenderer() {
        return renderer;
    }
}
