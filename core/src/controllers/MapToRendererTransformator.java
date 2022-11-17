package controllers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.model.maps.Map;

public class MapToRendererTransformator extends Stage {
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
    public MapToRendererTransformator(TiledMap tiledMap, Map map){
        this.tiledMap = tiledMap;
        this.map = map;
//        for(MapLayer layer: tiledMap.getLayers()){
//            TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
//            createActorsForLayer(tiledLayer);
//        }
        createActorsForLayer();
    }

    public TiledMap getTiledMap(){
        return this.tiledMap;
    }
    public Map getMap(){
        return this.map;
    }

    public HexagonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public void setMap(Map map){
        this.map=map;
        createTiledMap();
        renderer.setMap(tiledMap);
    }

    private void createActorsForLayer(){
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer llay = (TiledMapTileLayer) layers.get(0);

        TiledMapTileLayer lay = new TiledMapTileLayer(llay.getWidth(), llay.getHeight(), llay.getTileWidth(), llay.getTileHeight());
        for (int i = 0; i < llay.getWidth(); ++i) {
            for (int j = 0; j < llay.getHeight(); ++j) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                cell.setTile(map.getCells()[0][0].type.tile());

                TiledMapActor actor = new TiledMapActor(tiledMap, lay, cell);

                actor.setBounds(i * lay.getTileWidth(), j * lay.getTileHeight(),
                        lay.getTileWidth(), lay.getTileHeight());
                addActor(actor);

                actor.debug();

                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);

                lay.setCell(i, j, cell);
            }
        }
        layers.add(lay);
    }

//    private void createActorsForLayer(TiledMapTileLayer tiledLayer){
//        for(int x=0; x<tiledLayer.getWidth();++x){
//            for(int y=0; y< tiledLayer.getHeight();++y){
//                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x,y);
//
//                TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
//
//                actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(),
//                        tiledLayer.getTileWidth(), tiledLayer.getTileHeight());
//                addActor(actor);
//                actor.debug();
//                EventListener eventListener = new TiledMapClickListener(actor);
//                actor.addListener(eventListener);
//            }
//        }
//    }
    public void updateLayer(){
//        for(int x=0; x<tiledLayer.getWidth();++x){
//            for(int y=0; y< tiledLayer.getHeight();++y){
//                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x,y);
//
//                TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
//
//                actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(),
//                        tiledLayer.getTileWidth(), tiledLayer.getTileHeight());
//                addActor(actor);
//                actor.debug();
//                EventListener eventListener = new TiledMapClickListener(actor);
//                actor.addListener(eventListener);
//            }
//        }
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer llay = (TiledMapTileLayer) layers.get(0);

        TiledMapTileLayer lay = (TiledMapTileLayer) layers.get(0);
        for (int i = 0; i < llay.getWidth(); ++i) {
            for (int j = 0; j < llay.getHeight(); ++j) {
                TiledMapTileLayer.Cell cell = lay.getCell(i,j);

                cell.setTile(map.getCells()[0][0].type.tile());

                TiledMapActor actor = new TiledMapActor(tiledMap, lay, cell);

                actor.setBounds(i * lay.getTileWidth(), j * lay.getTileHeight(),
                        lay.getTileWidth(), lay.getTileHeight());
                addActor(actor);

                actor.debug();

                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);

                lay.setCell(i, j, cell);
            }
        }
        layers.add(lay);
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
                cell.setTile(map.getCells()[i][j].type.tile());

//                TiledMapActor actor = new TiledMapActor(tiledMap, lay, cell);
//                actor.setBounds(i * lay.getTileWidth(), j * lay.getTileHeight(),
//                        lay.getTileWidth(), lay.getTileHeight());
//                addActor(actor);
//                EventListener eventListener = new TiledMapClickListener(actor);
//                actor.addListener(eventListener);

//                actor.debug();/
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
                cell.setTile(map.getCells()[i][j].type.tile());
                lay.setCell(i, j, cell);
            }
        }
    }
}
