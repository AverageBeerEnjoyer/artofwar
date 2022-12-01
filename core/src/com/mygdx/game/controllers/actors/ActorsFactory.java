package com.mygdx.game.controllers.actors;

import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.mygdx.game.controllers.listeners.game_cl.MoveToCellCL;
import com.mygdx.game.controllers.listeners.game_cl.PlaceCapitalFirstRoundCL;
import com.mygdx.game.controllers.listeners.game_cl.PlaceToCellCL;
import com.mygdx.game.controllers.listeners.game_cl.SelectCellCL;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.maps.MapCell;

public record ActorsFactory(MainGameStage stage) {

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

    private Spinner createIntSpinner(int lowerBound, int upperBound, String name) {
        IntSpinnerModel model = new IntSpinnerModel(lowerBound, lowerBound, upperBound);
        return new Spinner(name, model);
    }
}
