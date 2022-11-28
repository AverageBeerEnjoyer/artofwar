package com.mygdx.game.controllers.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import java.util.Arrays;

public class MainGameStage extends Stage {
    private Map map;
    private Group cellActors;
    private Group controls;
    private Group selectedArea;
    private Group movableActors = new Group();
    private GameObject gameObjectToPlace = null;
    private Unit unitToMove = null;

    private Group playerFogArea;
    private Unit unitObjectPlayer = null;
    private GameObject[] gameObjects = new GameObject[10];
    private int count = 0;


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
        movableActors.toBack();
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
        this.gameObjects[count]= gameObjectToPlace;
        count++;
        definePlaceArea();
    }

    public void setUnitToMove(Unit unit) {
        clearSelectedArea();
        unitToMove = unit;
        defineMoveArea();
    }

    public void setFogOfWar(Unit unit) {
        clearFogOfWarArea();
        unitObjectPlayer = unit;
        defineFogOfWarArea();
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
        map.getMapToRendererTransformator().createSelectedArea(territory);
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
        map.getMapToRendererTransformator().createSelectedArea(area);
        movableActors.addActor(selectedArea);
    }

    public void defineFogOfWarArea(){
        playerFogArea = new Group();
        int[][] area = new int[map.getWidth()][map.getHeight()];
        for (int[] row : area) {
            Arrays.fill(row, -1);
        }

        for(int i = 0; i < this.count; ++i){
            if(gameObjects[i].getOwner() == gameObjects[0].getOwner()){
                area = mergeFogArea(
                        area,
                        map.selectCellsToFogArea(gameObjects[i].getPlacement().x, gameObjects[i].getPlacement().y)
                );
            }

        }

        map.getMapToRendererTransformator().fogOfWar(area);
    }

    public int[][] mergeFogArea(int[][] area, int[][] merge){
        for(int i=0;i<area.length;++i){
            for(int j=0;j<area[i].length;++j){
                if(area[i][j] == -1 ){
                    if(merge[i][j] != -1)
                        area[i][j] = merge[i][j];
                }
            }
        }
        return area;
    }

    public void clearSelectedArea() {
        movableActors.removeActor(selectedArea);
        map.getMapToRendererTransformator().clearSelectedArea();
        unitToMove = null;
        gameObjectToPlace = null;
        selectedArea = null;
    }

    public void clearFogOfWarArea() {
        movableActors.removeActor(playerFogArea);
        map.getMapToRendererTransformator().clearSelectedFogArea();
        unitObjectPlayer = null;
//        gameObjectToPlace = null;
        playerFogArea = null;
    }

    public void update() {
    }

    private void createControls() {
        controls = new Group();

        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = myFont;

        Button peasant = new TextButton("Peasant", style);
        peasant.addListener(new PeasantCreationCL(this));
        peasant.moveBy(0,30);
        peasant.setZIndex(10);
        peasant.debug();

        Button militia = new TextButton("Militia", style);
        militia.addListener(new MilitiaCreationCL(this));
        militia.moveBy(0,60);
        militia.setZIndex(10);
        militia.debug();

        Button knight = new TextButton("Knight", style);
        knight.addListener(new KnightCreationCL(this));
        knight.moveBy(0,90);
        knight.setZIndex(10);
        knight.debug();

        Button paladin = new TextButton("Paladin", style);
        paladin.addListener(new PaladinCreationCL(this));
        paladin.moveBy(0,120);
        paladin.setZIndex(10);
        paladin.debug();

        Button farm = new TextButton("Farm", style);
        farm.addListener(new FarmCreationCL(this));
        farm.moveBy(0,150);
        farm.setZIndex(10);
        farm.debug();

        Button tower = new TextButton("Tower", style);
        tower.addListener(new TowerCreationCL(this));
        tower.moveBy(0,180);
        tower.setZIndex(10);
        tower.debug();

        Button supertower = new TextButton("Supertower",style);
        supertower.addListener(new SuperTowerCreationCL(this));
        supertower.moveBy(0,210);
        supertower.setZIndex(10);
        supertower.debug();

        controls.addActor(peasant);
        controls.addActor(militia);
        controls.addActor(knight);
        controls.addActor(paladin);
        controls.addActor(farm);
        controls.addActor(tower);
        controls.addActor(supertower);

        controls.setZIndex(10);
        addActor(controls);
    }

    private TiledMapActor createSelectActorForCell(int i, int j) {
        MapCell cell = map.getCell(i, j);
        return new TiledMapActor(
                cell,
                new SelectCellCL(this, cell),
                2
        );
    }

    private TiledMapActor createMoveActorForCell(int i, int j) {
        MapCell cell = map.getCell(i, j);
        return new TiledMapActor(
                cell,
                new MoveToCellCL(this, cell),
                3
        );
    }

    private TiledMapActor createPlaceActorForCell(int i, int j) {
        MapCell cell = map.getCell(i, j);
        TiledMapActor  actor = new TiledMapActor(
                cell,
                new PlaceToCellCL(this, cell),
                3
        );
        actor.debug();
        return actor;
    }
}
