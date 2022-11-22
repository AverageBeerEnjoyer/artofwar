package controllers;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.view.utils.HexagonUtils;

public class MainGameStage extends Stage {
    private final int tileWidth = 64;
    private final int tileHeight = 55;
    private Map map;
    private TiledMap tiledMap;
    private final HexagonalTiledMapRenderer renderer;
    private Group cellActors;
    private Group controls;

    public MainGameStage(Map map) {
        this.map = map;
        loadNewTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }

    public MainGameStage() {
        this.map = new Map(10, 10, 0, 0);
        loadNewTiledMap();
        renderer = new HexagonalTiledMapRenderer(tiledMap);
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    public Map getMap() {
        return this.map;
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

    public void setMap(Map map) {
        this.map = map;
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
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                TiledMapActor actor = createActorForCell(i,j);
                cellActors.addActor(actor);
            }
        }
        addActor(cellActors);
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
                cell.setTile(map.getCells()[i][j].type.tile());
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
                cell.setTile(map.getCells()[i][j].type.tile());
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
        MapCell cell = map.safeAccess(i, j);
        TiledMapActor actor = new TiledMapActor(tiledMap, cell);
        actor.setUserObject(cell);
        actor.setWidth(tileWidth);
        actor.setHeight(tileHeight);
        actor.setPosition(
                HexagonUtils.countXLayout(i, j) * tileWidth,
                HexagonUtils.countYLayout(i, j) * tileHeight
        );
        actor.debug();
        actor.setZIndex(2);
        EventListener eventListener = new TiledMapClickListener(actor);
        actor.addListener(eventListener);
        return actor;
    }
}
