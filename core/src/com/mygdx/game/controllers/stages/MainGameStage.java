package com.mygdx.game.controllers.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.controllers.MapToRendererTransformator;
import com.mygdx.game.controllers.actors.ActorsFactory;
import com.mygdx.game.controllers.actors.TiledMapActor;
import com.mygdx.game.controllers.listeners.game_cl.NextTurnCL;
import com.mygdx.game.controllers.listeners.game_cl.GameObjectCreationCL;
import com.mygdx.game.model.GamingProcess;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.buildings.Farm;
import com.mygdx.game.model.gameobjects.buildings.SuperTower;
import com.mygdx.game.model.gameobjects.buildings.Tower;
import com.mygdx.game.model.gameobjects.units.*;
import com.mygdx.game.model.maps.CellType;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;
import com.mygdx.game.view.Start;

import java.util.function.BiFunction;

public class MainGameStage extends Stage implements Screen {
    private final Start start;
    private final OrthographicCamera camera = new OrthographicCamera();
    private MapToRendererTransformator mapToRendererTransformator;
    private Map map;
    private Group selectedArea;
    private Group movableActors = new Group();
    private GameObject gameObjectToPlace = null;
    private Unit unitToMove = null;
    private GamingProcess gamingProcess;
    private ActorsFactory factory;


    public MainGameStage(Map map, GamingProcess gamingProcess, Start start) {
        this.start = start;
        this.map = map;
        mapToRendererTransformator = map.getMapToRendererTransformator();
        factory = new ActorsFactory(this);
        this.gamingProcess = gamingProcess;
        this.gamingProcess.setStage(this);
        addActor(movableActors);
        placeCapitalArea();
//        camera.setToOrtho(false, 1080, 720);
//        addListener(
//                new DragListener() {
//                    @Override
//                    public void drag(InputEvent event, float x, float y, int pointer) {
//                        camera.position.set(camera.position.x - getDeltaX(), camera.position.y - getDeltaY(), 0);
//                        movableActors.moveBy(getDeltaX(), getDeltaY());
//                    }
//                }
////        );
    }

    public MainGameStage(Start start) {
        this.start = start;
        this.map = new Map(10, 10, 0, 0);
        loadActors();
    }


    public Map getMap() {
        return this.map;
    }

    public void loadActors() {
        createActorsLayer();
        createControls();
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
        selectArea(factory::createPlaceActor, map.getPlayerTerritory(gamingProcess.getCurrentPlayer()));
    }

    public void setUnitToMove(Unit unit) {
        clearSelectedArea();
        unitToMove = unit;
        selectArea(factory::createMoveActor, map.selectCellsToMove(unitToMove.getPlacement().x, unitToMove.getPlacement().y));
    }


    public Unit getUnitToMove() {
        return unitToMove;
    }

    public GameObject getGameObjectToPlace() {
        return gameObjectToPlace;
    }


    public void placeCapitalArea() {
        selectArea(factory::createPlaceCapitalActor, map.getPlayerTerritory(Player.NOBODY));
    }

    private void createActorsLayer() {
        Group cellActors = new Group();
        cellActors.setName("cellActors");
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (map.getMapCreator().getCells()[i][j].getType() == CellType.WATER) continue;
                TiledMapActor actor = factory.createSelectActor(i, j);
                cellActors.addActor(actor);
            }
        }
        movableActors.addActor(cellActors);
    }

    public void selectArea(BiFunction<Integer, Integer, TiledMapActor> actorCreator, int[][] area) {
        selectedArea = new Group();
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (area[i][j] != -1) {
                    TiledMapActor actor = actorCreator.apply(i, j);
                    selectedArea.addActor(actor);
                }
            }
        }
        map.getMapToRendererTransformator().createSelectedArea(area);
        movableActors.addActor(selectedArea);
    }

    public void clearSelectedArea() {
        movableActors.getChild(0).setVisible(true);
        movableActors.removeActor(selectedArea);
        map.getMapToRendererTransformator().clearSelectedArea();
        unitToMove = null;
        gameObjectToPlace = null;
        selectedArea = null;
    }

    private void createControls() {
        Group controls = new Group();

        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = myFont;

        Button peasant = new TextButton("Peasant", style);
        peasant.addListener(new GameObjectCreationCL(this, Peasant.class));
        peasant.moveBy(0, 30);
        peasant.setZIndex(10);
        peasant.debug();

        Button militia = new TextButton("Militia", style);
        militia.addListener(new GameObjectCreationCL(this, Militia.class));
        militia.moveBy(0, 60);
        militia.setZIndex(10);
        militia.debug();

        Button knight = new TextButton("Knight", style);
        knight.addListener(new GameObjectCreationCL(this, Knight.class));
        knight.moveBy(0, 90);
        knight.setZIndex(10);
        knight.debug();

        Button paladin = new TextButton("Paladin", style);
        paladin.addListener(new GameObjectCreationCL(this, Paladin.class));
        paladin.moveBy(0, 120);
        paladin.setZIndex(10);
        paladin.debug();

        Button farm = new TextButton("Farm", style);
        farm.addListener(new GameObjectCreationCL(this, Farm.class));
        farm.moveBy(0, 150);
        farm.setZIndex(10);
        farm.debug();

        Button tower = new TextButton("Tower", style);
        tower.addListener(new GameObjectCreationCL(this, Tower.class));
        tower.moveBy(0, 180);
        tower.setZIndex(10);
        tower.debug();

        Button supertower = new TextButton("Supertower", style);
        supertower.addListener(new GameObjectCreationCL(this, SuperTower.class));
        supertower.moveBy(0, 210);
        supertower.setZIndex(10);
        supertower.debug();

        Button nextTurn = new TextButton("Next turn", style);
        nextTurn.addListener(new NextTurnCL(this));
        nextTurn.moveBy(0, 240);
        nextTurn.setZIndex(10);
        nextTurn.debug();

        controls.addActor(peasant);
        controls.addActor(militia);
        controls.addActor(knight);
        controls.addActor(paladin);
        controls.addActor(farm);
        controls.addActor(tower);
        controls.addActor(supertower);
        controls.addActor(nextTurn);

        controls.setZIndex(10);
        addActor(controls);
    }


    public GamingProcess getGamingProcess() {
        return gamingProcess;
    }

    @Override
    public void show() {
//        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
//        ScreenUtils.clear(0, 0, 0.2f, 1);
//        camera.update();
//        mapToRendererTransformator.getRenderer().setView(camera);
//        mapToRendererTransformator.getRenderer().render();
//        act();
//        draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
