package com.mygdx.game.controllers.actors;

import com.mygdx.game.controllers.listeners.map_cl.MoveToCellCL;
import com.mygdx.game.controllers.listeners.map_cl.PlaceCapitalFirstRoundCL;
import com.mygdx.game.controllers.listeners.map_cl.PlaceToCellCL;
import com.mygdx.game.controllers.listeners.map_cl.SelectCellCL;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.maps.MapCell;

public class ActorsFactory {
    private final MainGameStage stage;

    public ActorsFactory(MainGameStage stage) {
        this.stage = stage;
    }
    public TiledMapActor createSelectActor(int i, int j) {
        MapCell cell = stage.getMap().getCell(i, j);
        return new TiledMapActor(
                i, j,
                new SelectCellCL(stage, cell),
                2
        );
    }

    public TiledMapActor createMoveActor(int i, int j) {
        MapCell cell = stage.getMap().getCell(i, j);
        TiledMapActor actor = new TiledMapActor(
                i, j,
                new MoveToCellCL(stage, cell),
                3
        );
        actor.debug();
        return actor;
    }

    public TiledMapActor createPlaceActor(int i, int j) {
        MapCell cell = stage.getMap().getCell(i, j);
        TiledMapActor actor = new TiledMapActor(
                i, j,
                new PlaceToCellCL(stage, cell),
                3
        );
        actor.debug();
        return actor;
    }

    public TiledMapActor createPlaceCapitalActor(int i, int j) {
        MapCell cell = stage.getMap().getCell(i, j);
        TiledMapActor actor = new TiledMapActor(
                i, j,
                new PlaceCapitalFirstRoundCL(stage, cell),
                3
        );
        actor.debug();
        return actor;
    }
}
