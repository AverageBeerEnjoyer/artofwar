package com.mygdx.game.controllers.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.controllers.MapToRendererTransformator;
import com.mygdx.game.controllers.actors.TiledMapActor;
import com.mygdx.game.controllers.listeners.game_cl.*;
import com.mygdx.game.model.GamingProcess;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.buildings.Farm;
import com.mygdx.game.model.gameobjects.buildings.SuperTower;
import com.mygdx.game.model.gameobjects.buildings.Tower;
import com.mygdx.game.model.gameobjects.units.*;
import com.mygdx.game.model.maps.CellType;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;
import com.mygdx.game.view.ArtofWar;

import java.util.function.BiFunction;

public class MainGameStage extends Stage implements Screen {
    private final ArtofWar artofWar;
    private final OrthographicCamera camera = new OrthographicCamera();
    private MapToRendererTransformator mapToRendererTransformator;
    private Map map;
    private Group selectedArea;
    private Group movableActors = new Group();
    private GameObject gameObjectToPlace = null;
    private Unit unitToMove = null;
    private GamingProcess gamingProcess;


    public MainGameStage(Map map, GamingProcess gamingProcess, ArtofWar artofWar) {
        this.artofWar = artofWar;
        this.map = map;
        mapToRendererTransformator = map.getMapToRendererTransformator();
        this.gamingProcess = gamingProcess;
        this.gamingProcess.setStage(this);
        artofWar.factory.setGameStage(this);
        addActor(movableActors);
        placeCapitalArea();
        camera.setToOrtho(false, 1080, 720);
        addListener(
                new DragListener() {
                    @Override
                    public void drag(InputEvent event, float x, float y, int pointer) {
                        camera.position.set(camera.position.x - getDeltaX(), camera.position.y - getDeltaY(), 0);
                        movableActors.moveBy(getDeltaX(), getDeltaY());
                    }
                }
        );
    }

    public MainGameStage(ArtofWar artofWar) {
        this.artofWar = artofWar;
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
        selectArea(PlaceToCellCL::new, map.getPlayerTerritory(gamingProcess.getCurrentPlayer()));
    }

    public void setUnitToMove(Unit unit) {
        clearSelectedArea();
        unitToMove = unit;
        selectArea(MoveToCellCL::new, map.selectCellsToMove(unitToMove.getPlacement().x, unitToMove.getPlacement().y));
    }


    public Unit getUnitToMove() {
        return unitToMove;
    }

    public GameObject getGameObjectToPlace() {
        return gameObjectToPlace;
    }


    public void placeCapitalArea() {
        selectArea(PlaceCapitalFirstRoundCL::new, map.getPlayerTerritory(Player.NOBODY));
    }

    private void createActorsLayer() {
        Group cellActors = new Group();
        cellActors.setName("cellActors");
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (map.getMapCreator().getCells()[i][j].getType() == CellType.WATER) continue;
                TiledMapActor actor = artofWar.factory.createTiledMapActor(map.getCell(i,j), new SelectCellCL(this, map.getCell(i,j)),2);
                cellActors.addActor(actor);
            }
        }
        movableActors.addActor(cellActors);
    }

    public void selectArea(BiFunction<MainGameStage,MapCell, ClickListener> listenerCreator, int[][] area) {
        selectedArea = new Group();
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (area[i][j] != -1) {
                    TiledMapActor actor = artofWar.factory.createTiledMapActor(map.getCell(i,j), listenerCreator.apply(this,map.getCell(i,j) ), 3);
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

    public void endGame() {
        artofWar.setScreen(artofWar.menuStage);
        artofWar.menuStage.toMain();
        dispose();
    }

    private void createControls() {
        Group controls = new Group();

        Button peasant = artofWar.factory.createTextButton(0, 30, "Peasant", new GameObjectCreationCL(this, Peasant.class));

        Button militia = artofWar.factory.createTextButton(0, 60, "Militia", new GameObjectCreationCL(this, Militia.class));

        Button knight = artofWar.factory.createTextButton(0, 90, "Knight", new GameObjectCreationCL(this, Knight.class));

        Button paladin = artofWar.factory.createTextButton(0, 120, "Paladin", new GameObjectCreationCL(this, Paladin.class));

        Button farm = artofWar.factory.createTextButton(0, 150, "Farm", new GameObjectCreationCL(this, Farm.class));

        Button tower = artofWar.factory.createTextButton(0, 180, "Tower", new GameObjectCreationCL(this, Tower.class));

        Button supertower = artofWar.factory.createTextButton(0, 210, "Supertower", new GameObjectCreationCL(this, SuperTower.class));

        Button nextTurn = artofWar.factory.createTextButton(0,240,"Next turn",new NextTurnCL(this));

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
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        mapToRendererTransformator.getRenderer().setView(camera);
        mapToRendererTransformator.getRenderer().render();
        act();
        draw();
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

    @Override
    public void dispose() {
        super.dispose();
        mapToRendererTransformator.getRenderer().dispose();
    }
}
