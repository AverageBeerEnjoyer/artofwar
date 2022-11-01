package com.mygdx.game.view;

import com.mygdx.game.model.maps.Map;

public class MapView {
    private final Map map;
    public MapView(Map map){
        this.map=map;
    }
//    public void create () {
//
//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, (w / h) * 480, 480);
//        camera.update();
//
//        cameraController = new OrthoCamController(camera);
//        Gdx.input.setInputProcessor(cameraController);
//
//        hexture = new Texture(Gdx.files.internal("data/maps/tiled/hex/hexes.png"));
//        TextureRegion[][] hexes = TextureRegion.split(hexture, 112, 97);
//        map = new TiledMap();
//        MapLayers layers = map.getLayers();
//        TiledMapTile[] tiles = new TiledMapTile[3];
//        tiles[0] = new StaticTiledMapTile(new TextureRegion(hexes[0][0]));
//        tiles[1] = new StaticTiledMapTile(new TextureRegion(hexes[0][1]));
//        tiles[2] = new StaticTiledMapTile(new TextureRegion(hexes[1][0]));
//
//        for (int l = 0; l < 1; l++) {
//            TiledMapTileLayer layer = new TiledMapTileLayer(45, 30, 112, 97);
//            for (int y = 0; y < 30; y++) {
//                for (int x = 0; x < 45; x++) {
//                    int id = (int)(Math.random() * 3);
//                    Cell cell = new Cell();
//                    cell.setTile(tiles[id]);
//                    layer.setCell(x, y, cell);
//                }
//            }
//            layers.add(layer);
//        }
//
//        renderer = new HexagonalTiledMapRenderer(map);
//    }
}
