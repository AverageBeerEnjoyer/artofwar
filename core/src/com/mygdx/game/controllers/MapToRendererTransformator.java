package com.mygdx.game.controllers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.mygdx.game.ProjectVariables;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;

import static com.mygdx.game.ProjectVariables.tileHeight;
import static com.mygdx.game.ProjectVariables.tileWidth;

public class MapToRendererTransformator {
    private final HexagonalTiledMapRenderer renderer;
    private TiledMap tiledMap;
    private Map map;

    public MapToRendererTransformator(Map map) {
        this.map = map;
        loadNewTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }

    private void loadNewTiledMap() {
        tiledMap = new TiledMap();
        createMapLayer();
        createGameObjectsLayer();
    }

    private void createMapLayer() {
        TiledMapTileLayer generalMapLayer = new TiledMapTileLayer(map.getWidth(), map.getHeight(), tileWidth, tileHeight);
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(map.getMapCreator().getCells()[i][j].getType().tile());
                generalMapLayer.setCell(i, j, cell);
            }
        }
        tiledMap.getLayers().add(generalMapLayer);
    }

    private void createGameObjectsLayer() {
        TiledMapTileLayer gameObjectsLayer = new TiledMapTileLayer(map.getWidth(), map.getHeight(), tileWidth, tileHeight);
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                GameObject obj = map.getMapCreator().getCells()[i][j].getGameObject();
                gameObjectsLayer.setCell(i, j, cell);
                if (obj == null) continue;
                cell.setTile(obj.getTile());
            }
        }
        tiledMap.getLayers().add(gameObjectsLayer);
    }

    public void createSelectedArea(int[][] area) {
        TiledMapTileLayer black = new TiledMapTileLayer(map.getWidth(), map.getHeight(), tileWidth, tileHeight);
        black.setName("selected");
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (area[i][j] != -1) continue;
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(ProjectVariables.blackTile);
                black.setCell(i, j, cell);
            }
        }
        black.setOpacity(0.7f);
        tiledMap.getLayers().add(black);
    }

    public void clearSelectedArea() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("selected");
        if (layer != null) tiledMap.getLayers().remove(layer);

    }

    public void update(int x, int y) {
        TiledMapTileLayer gameObjectsLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
        MapCell mapCell = map.getCell(x, y);
        TiledMapTileLayer.Cell cell = gameObjectsLayer.getCell(x, y);
        if (mapCell.getGameObject() == null) {
            cell.setTile(null);
            return;
        }
        gameObjectsLayer.getCell(x, y).setTile(map.getCell(x, y).getGameObject().getTile());
    }

    public HexagonalTiledMapRenderer getRenderer() {
        return renderer;
    }
}
