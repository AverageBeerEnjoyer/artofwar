package com.mygdx.game.controllers;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.mygdx.game.controllers.actors.TiledMapActor;
import com.mygdx.game.model.ProjectVariables;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.units.Paladin;
import com.mygdx.game.model.maps.CellType;
import com.mygdx.game.model.maps.Map;

import static com.mygdx.game.model.ProjectVariables.tileHeight;
import static com.mygdx.game.model.ProjectVariables.tileWidth;

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
        for(int i=0;i< map.getWidth();++i){
            for(int j=0;j< map.getHeight();++j){
                TiledMapTileLayer.Cell cell =new TiledMapTileLayer.Cell();
                GameObject obj = map.getMapCreator().getCells()[i][j].getGameObject();
                gameObjectsLayer.setCell(i,j,cell);
                if(obj == null) continue;
                cell.setTile(obj.getTile());
            }
        }
        tiledMap.getLayers().add(gameObjectsLayer);
    }

    public void update() {
        TiledMapTileLayer gameObjectsLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                TiledMapTileLayer.Cell cell = gameObjectsLayer.getCell(i, j);
                GameObject obj = map.getMapCreator().getCells()[i][j].getGameObject();
                if(obj == null){
                    cell.setTile(null);
                    continue;
                }
                cell.setTile(ProjectVariables.paladinPic);
            }
        }
    }

    public HexagonalTiledMapRenderer getRenderer() {
        return renderer;
    }
}
