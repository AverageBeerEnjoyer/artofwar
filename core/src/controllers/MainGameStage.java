package controllers;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;

public class MainGameStage extends Stage {
    private Map map;
    private TiledMap tiledMap;
    private HexagonalTiledMapRenderer renderer;
    public Group group = new Group();

    public MainGameStage(Map map) {
        this.map = map;
        createTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
        createActorsLayer();
    }
    public MainGameStage(){
        this.map=new Map(10,10,0,0);
        createTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
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

    private void createActorsLayer(){
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer lay = (TiledMapTileLayer) layers.get(0);
        for (int i = 0; i < lay.getWidth(); ++i) {
            for (int j = 0; j < lay.getHeight(); ++j) {
                MapCell cell = map.safeAccess(i,j);

                TiledMapActor actor = new TiledMapActor(tiledMap, lay, cell);
                actor.setUserObject(cell);
                actor.setWidth(lay.getTileWidth());
                actor.setHeight(lay.getTileHeight());
                actor.setPosition(
                        (float) (i*0.75*lay.getTileWidth()),
                        countHeight(i,j)*lay.getTileHeight()
                );
                group.addActor(actor);
                actor.debug();

                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);
            }
        }
        addActor(group);
    }
    private float countHeight(int i, int j){
        float res = j;
        if((i&1)==0) res+=0.5;
        return res;
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
