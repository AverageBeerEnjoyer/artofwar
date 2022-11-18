package com.mygdx.game.controllers;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.mygdx.game.model.maps.Map;

public class MapToRendererTransformator {
    private Map map;
    private TiledMap tiledMap;
    private HexagonalTiledMapRenderer renderer;

    public MapToRendererTransformator(Map map) {
        this.map = map;
        createTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }
    public MapToRendererTransformator(){
        this.map=new Map(10,10,0,0);
        createTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }

    public HexagonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public void setMap(Map map){
        this.map=map;
        createTiledMap();
        renderer.setMap(tiledMap);
    }

    private void createTiledMap() {
        int width = map.getWidth();
        int height = map.getHeight();
        TiledMap tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer lay = new TiledMapTileLayer(width, height, 64, 55);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(map.getCells()[i][j].getType().tile());
                lay.setCell(i, j, cell);
            }
        }
        layers.add(lay);
        this.tiledMap=tiledMap;

    }
    public void update(){
        int width = map.getWidth();
        int height = map.getHeight();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer lay = (TiledMapTileLayer) layers.get(0);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                TiledMapTileLayer.Cell cell = lay.getCell(i,j);
                cell.setTile(map.getCells()[i][j].getType().tile());
                lay.setCell(i, j, cell);
            }
        }
    }
}
