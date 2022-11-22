package com.mygdx.game.controllers;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.maps.MapCreator;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.view.utils.HexagonUtils;

public class MainGameStage extends Stage {
    private final int tileWidth = 64;
    private final int tileHeight = 55;
    private MapCreator mapCreator;
    private TiledMap tiledMap;
    private final HexagonalTiledMapRenderer renderer;
    private Group cellActors;
    private Group controls;
    private GameObject gameObjectToPlace = null;
    private Group moveArea;

    public MainGameStage(MapCreator mapCreator) {
        this.mapCreator = mapCreator;
        loadNewTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }

    public MainGameStage() {
        this.mapCreator = new MapCreator(10, 10, 0, 0);
        loadNewTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    public MapCreator getMap() {
        return this.mapCreator;
    }

    public Group getCellActors() {
        return cellActors;
    }

    public Group getControls() {
        return controls;
    }

    public HexagonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public void setMap(MapCreator mapCreator) {
        this.mapCreator = mapCreator;
        loadNewTiledMap();
        renderer.setMap(tiledMap);
    }

    private void loadNewTiledMap() {
        tiledMap = new TiledMap();
        createMapLayer();
        createActorsLayer();
    }

    private void createActorsLayer() {
        cellActors = new Group();
        cellActors.setName("cellActors");
        for (int i = 0; i < mapCreator.getWidth(); ++i) {
            for (int j = 0; j < mapCreator.getHeight(); ++j) {
                TiledMapActor actor = createActorForCell(i,j);
                cellActors.addActor(actor);
            }
        }
        addActor(cellActors);
    }

    private void createMapLayer() {
        int width = mapCreator.getWidth();
        int height = mapCreator.getHeight();
        TiledMap tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer lay = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(mapCreator.getCells()[i][j].getType().tile());
                lay.setCell(i, j, cell);
            }
        }
        layers.add(lay);
        this.tiledMap = tiledMap;

    }

    private void defineMoveArea(){

    }

    public void update() {
        int width = mapCreator.getWidth();
        int height = mapCreator.getHeight();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer lay = (TiledMapTileLayer) layers.get(0);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                TiledMapTileLayer.Cell cell = lay.getCell(i, j);
                cell.setTile(mapCreator.getCells()[i][j].getType().tile());
                lay.setCell(i, j, cell);
            }
        }
    }

    private void createControls() {
//        controls = new Group();
//        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
//        Button addPeasant = new TextButton("Peasant", style);
//        addPeasant.addListener((event, actor) -> {
//
//                }
//        )
    }

    private TiledMapActor createActorForCell(int i, int j) {
        MapCell cell = mapCreator.safeAccess(i, j);
        TiledMapActor actor = new TiledMapActor(this, cell);
        actor.setUserObject(cell);
        actor.setWidth(tileWidth);
        actor.setHeight(tileHeight);
        actor.setPosition(
                HexagonUtils.countXLayout(i, j) * tileWidth,
                HexagonUtils.countYLayout(i, j) * tileHeight
        );
        actor.debug();
        actor.setZIndex(2);
        EventListener eventListener = new SelectCellClickListener(actor);
        actor.addListener(eventListener);
        return actor;
    }
}
