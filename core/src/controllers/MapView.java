package controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.mygdx.game.model.maps.Map;

import static com.badlogic.gdx.graphics.g2d.TextureRegion.split;

public class MapView {
    private String rsrc = "pastel_resources_hex/rotat/";
    private final Map map;
    private HexagonalTiledMapRenderer renderer;

    public MapView(Map map) {
        this.map = map;
    }

    public HexagonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public void create() {
        TiledMap tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTile[] tiles = new TiledMapTile[1];
        TextureRegion tr= new TextureRegion(new Texture(Gdx.files.internal(rsrc+"desertHex.gif")));
        tiles[0]=new StaticTiledMapTile(tr);
        TiledMapTileLayer lay = new TiledMapTileLayer(10, 10, 64, 55);
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(tiles[0]);
                lay.setCell(i, j, cell);
            }
        }
        layers.add(lay);
        renderer = new HexagonalTiledMapRenderer(tiledMap);
//

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
    }

//    private PolygonRegion polygon(TextureRegion tr) {
//        float[] vertices = {
//                tr.getRegionWidth() * 0.25f, 0,
//                tr.getRegionWidth() * 0.75f, 0,
//                0, tr.getRegionHeight()/2.0f,
//                tr.getRegionWidth(), tr.getRegionHeight()/2.0f,
//                tr.getRegionWidth()*0.25f, tr.getRegionHeight(),
//                tr.getRegionWidth()*0.75f, tr.getRegionHeight()
//        };
//        return new PolygonRegion(
//                tr,
//                vertices,
//                new EarClippingTriangulator().computeTriangles(vertices).toArray()
//        );
//    }
}
