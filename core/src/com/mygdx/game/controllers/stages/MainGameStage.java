package com.mygdx.game.controllers.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.controllers.actors.TiledMapActor;
import com.mygdx.game.controllers.listeners.creation_cl.*;
import com.mygdx.game.controllers.listeners.map_cl.MoveToCellCL;
import com.mygdx.game.controllers.listeners.map_cl.PlaceToCellCL;
import com.mygdx.game.controllers.listeners.map_cl.SelectCellCL;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.CellType;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

import static com.mygdx.game.model.ProjectVariables.tileHeight;
import static com.mygdx.game.model.ProjectVariables.tileWidth;

public class MainGameStage extends Stage {
    private Map map;
    private Group cellActors;
    private Group controls;
    private Group selectedArea;
    private Group movableActors = new Group();
    private GameObject gameObjectToPlace = null;
    private Unit unitToMove = null;


    public MainGameStage(Map Map) {
        this.map = Map;
        loadActors();
    }

    public MainGameStage() {
        this.map = new Map(10, 10, 0, 0);
        loadActors();
    }


    public Map getMap() {
        return this.map;
    }

    private void loadActors(){
        createActorsLayer();
        createControls();
        addActor(movableActors);
    }

    public Group getMovableActors() {
        return movableActors;
    }

    public void setMap(Map Map) {
        this.map = Map;
    }

    public void setGameObjectToPlace(GameObject gameObjectToPlace) {
        clearSelectedArea();
        this.gameObjectToPlace = gameObjectToPlace;
        definePlaceArea();
    }

    public void setUnitToMove(Unit unit) {
        clearSelectedArea();
        unitToMove = unit;
        defineMoveArea();
    }

    public Unit getUnitToMove() {
        return unitToMove;
    }

    public GameObject getGameObjectToPlace() {
        return gameObjectToPlace;
    }


    private void createActorsLayer() {
        cellActors = new Group();
        cellActors.setName("cellActors");
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (map.getMapCreator().getCells()[i][j].getType() == CellType.WATER) continue;
                TiledMapActor actor = createSelectActorForCell(i, j);
                cellActors.addActor(actor);
            }
        }
        movableActors.addActor(cellActors);
    }

    public void definePlaceArea() {
        selectedArea = new Group();
        Player player = gameObjectToPlace.owner;
        int[][] territory = map.getPlayerTerritory(player);
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (territory[i][j] == 0) {
                    TiledMapActor actor = createPlaceActorForCell(i,j);
                    selectedArea.addActor(actor);
                }
            }
        }
        movableActors.addActor(selectedArea);
    }

    public void defineMoveArea() {
        selectedArea = new Group();
        int[][] area = map.selectCellsToMove(unitToMove.getPlacement().x, unitToMove.getPlacement().y);
        if (area == null) return;
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (area[i][j] == -1) continue;
                TiledMapActor actor = createMoveActorForCell(i, j);
                selectedArea.addActor(actor);
            }
        }
        movableActors.addActor(selectedArea);
    }

    public void clearSelectedArea() {
        movableActors.removeActor(selectedArea);
        unitToMove = null;
        gameObjectToPlace = null;
        selectedArea = null;
    }

    public void update() {
    }

    private void createControls() {
        controls = new Group();

        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = myFont;

        Button addPeasant = new TextButton("Peasant", style);
        addPeasant.addListener(new PeasantCreationCL(this));
        addPeasant.moveBy(0,30);
        addPeasant.debug();

        Button addMilitia = new TextButton("Militia", style);
        addMilitia.addListener(new MilitiaCreationCL(this));
        addMilitia.moveBy(0,60);
        addMilitia.debug();

        Button addKnight = new TextButton("Knight", style);
        addKnight.addListener(new KnightCreationCL(this));
        addKnight.moveBy(0,90);
        addKnight.debug();

        Button addPaladin = new TextButton("Paladin", style);
        addPaladin.addListener(new PaladinCreationCL(this));
        addPaladin.moveBy(0,120);
        addPaladin.debug();

        Button addFarm = new TextButton("Farm", style);
        addFarm.addListener(new FarmCreationCL(this));
        addFarm.moveBy(0,150);
        addFarm.debug();

        Button addTower = new TextButton("Tower", style);
        addTower.addListener(new TowerCreationCL(this));
        addTower.moveBy(0,180);
        addTower.debug();

        Button addSuperTower = new TextButton("Supertower",style);
        addSuperTower.addListener(new SuperTowerCreationCL(this));
        addSuperTower.moveBy(0,210);
        addSuperTower.debug();

        controls.addActor(addPeasant);
        controls.addActor(addMilitia);
        controls.addActor(addKnight);
        controls.addActor(addPaladin);
        controls.addActor(addFarm);
        controls.addActor(addTower);
        controls.addActor(addSuperTower);

        addActor(controls);
    }

    private TiledMapActor createSelectActorForCell(int i, int j) {
        MapCell cell = map.getCell(i, j);
        return new TiledMapActor(
                this,
                cell,
                new SelectCellCL(this, cell),
                2
        );
    }

    private TiledMapActor createMoveActorForCell(int i, int j) {
        MapCell cell = map.getCell(i, j);
        return new TiledMapActor(
                this,
                cell,
                new MoveToCellCL(this, cell),
                3
        );
    }

    private TiledMapActor createPlaceActorForCell(int i, int j) {
        MapCell cell = map.getCell(i, j);
        TiledMapActor  actor = new TiledMapActor(
                this,
                cell,
                new PlaceToCellCL(this, cell),
                3
        );
        actor.debug();
        return actor;
    }
}
