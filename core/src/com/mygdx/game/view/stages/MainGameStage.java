package com.mygdx.game.view.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.ProjectVariables.*;
import com.mygdx.game.model.maps.MapToRendererTransformator;
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
    private final MapToRendererTransformator mapToRendererTransformator;
    private Map map;
    private Group selectedArea;
    private final Group movableActors = new Group();
    private Group controls;
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
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
                TiledMapActor actor = artofWar.factory.createTiledMapActor(map.getCell(i, j), new SelectCellCL(this, map.getCell(i, j)), 2);
                cellActors.addActor(actor);
            }
        }
        movableActors.addActor(cellActors);
    }

    public void selectArea(BiFunction<MainGameStage, MapCell, ClickListener> listenerCreator, int[][] area) {
        selectedArea = new Group();
        for (int i = 0; i < map.getWidth(); ++i) {
            for (int j = 0; j < map.getHeight(); ++j) {
                if (area[i][j] != -1) {
                    TiledMapActor actor = artofWar.factory.createTiledMapActor(map.getCell(i, j), listenerCreator.apply(this, map.getCell(i, j)), 3);
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

    public void showEndStats() {
        Group endStat = new Group();
        endStat.setBounds(Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 200, 300, 400);

        Table table = new Table();

        Image background = new Image(new Texture(Gdx.files.internal("endStatBackground.jpg")));
        background.setBounds(0,0,300,400);
        endStat.addActor(background);


        Label label = artofWar.factory.createLabel(70, 300, "Game over!");
        table.add(label).colspan(2).center();
        table.row();

        label = artofWar.factory.createLabel(70, 200, "Winner:");
        table.add(label).right();

        label = artofWar.factory.createLabel(150, 200, gamingProcess.getCurrentPlayer().name);
        table.add(label).left();
        table.row();

        Button button = artofWar.factory.createTextButton(100, 30, "Back to menu", new EndGameCL(this));
        table.add(button).colspan(2).center();

        table.setPosition(endStat.getWidth()/2 - table.getWidth()/2,endStat.getHeight()/2-table.getHeight()/2);

        endStat.addActor(table);
        setRoot(endStat);
    }

    private void createControls() {
        controls = new Group();

        HorizontalGroup currentTurnInfo = new HorizontalGroup();
        currentTurnInfo.setPosition(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() - 50);
        HorizontalGroup unitButtons = new HorizontalGroup();
        unitButtons.setPosition(Gdx.graphics.getWidth() / 2 - 350, 90);


        Button peasant = artofWar.factory.createImageTextButton(
                0, 0,
                new Texture(Gdx.files.internal("button/peasant_button.png")),
                new GameObjectCreationCL(this, Peasant.class),
                UnitSpec.peasantCost + " G"
        );

        Button militia = artofWar.factory.createImageTextButton(
                0, 0,
                new Texture(Gdx.files.internal("button/militia_button.png")),
                new GameObjectCreationCL(this, Militia.class),
                UnitSpec.militiaCost + " G"
        );

        Button knight = artofWar.factory.createImageTextButton(
                0, 0,
                new Texture(Gdx.files.internal("button/knight_button.png")),
                new GameObjectCreationCL(this, Knight.class),
                UnitSpec.knightCost + " G"
        );

        Button paladin = artofWar.factory.createImageTextButton(
                0, 0,
                new Texture(Gdx.files.internal("button/paladin_button.png")),
                new GameObjectCreationCL(this, Paladin.class),
                UnitSpec.paladinCost + " G"
        );

        Button farm = artofWar.factory.createImageTextButton(
                0, 0,
                new Texture(Gdx.files.internal("button/farm_button.png")),
                new GameObjectCreationCL(this, Farm.class),
                (BuildingSpec.defaultFarmCost + gamingProcess.getCurrentPlayer().getFarmsNumber() * BuildingSpec.additionalFarmCost) + " G"
        );
        farm.getChild(1).setName("farm");

        Button tower = artofWar.factory.createImageTextButton(
                0, 0,
                new Texture(Gdx.files.internal("button/tower_button.png")),
                new GameObjectCreationCL(this, Tower.class),
                BuildingSpec.towerCost + " G"
        );

        Button supertower = artofWar.factory.createImageTextButton(
                0, 0,
                new Texture(Gdx.files.internal("button/superTower_button.png")),
                new GameObjectCreationCL(this, SuperTower.class),
                BuildingSpec.superTowerCost + " G"
        );

        Button nextTurn = artofWar.factory.createImageButton(
                Gdx.graphics.getWidth() - 100, 30,
                new Texture(Gdx.files.internal("button/arrow_next.png")),
                new NextTurnCL(this)
        );
        nextTurn.setName("next turn");

        unitButtons.addActor(peasant);
        unitButtons.addActor(militia);
        unitButtons.addActor(knight);
        unitButtons.addActor(paladin);
        unitButtons.addActor(farm);
        unitButtons.addActor(tower);
        unitButtons.addActor(supertower);


        Label playerName = artofWar.factory.createLabel(0, 0, gamingProcess.getCurrentPlayer().name);
        playerName.setName("name");
        Image goldPic = new Image(new Texture(Gdx.files.internal("button/coin_2.png")));
        Label gold = artofWar.factory.createLabel(0, 0, gamingProcess.getCurrentPlayer().getGold() + "");
        gold.setName("gold");


        currentTurnInfo.addActor(playerName);
        currentTurnInfo.addActor(goldPic);
        currentTurnInfo.addActor(gold);


        controls.addActor(nextTurn);
        controls.addActor(unitButtons);
        controls.addActor(currentTurnInfo);
        addActor(controls);
    }

    public void updateInfo() {
        Label name = controls.findActor("name");
        Label farm = controls.findActor("farm");
        Label gold = controls.findActor("gold");
        farm.setText((BuildingSpec.defaultFarmCost + gamingProcess.getCurrentPlayer().getFarmsNumber() * BuildingSpec.additionalFarmCost) + " G");
        name.setText(gamingProcess.getCurrentPlayer().name);
        gold.setText(gamingProcess.getCurrentPlayer().getGold() + "");
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
