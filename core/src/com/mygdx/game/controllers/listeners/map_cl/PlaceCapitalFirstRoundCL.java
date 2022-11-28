package com.mygdx.game.controllers.listeners.map_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.buildings.Capital;
import com.mygdx.game.model.maps.MapCell;

public class PlaceCapitalFirstRoundCL extends ActionMapCL{
    public PlaceCapitalFirstRoundCL(MainGameStage stage, MapCell cell){
        super(stage,cell);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        stage.clearSelectedArea();
        stage.getMap().setGameObjectOnCell(
                cell.x,
                cell.y,
                new Capital(stage.getMap(),null,stage.getGamingProcess().getCurrentPlayer())
        );
        stage.getGamingProcess().getCurrentPlayer().createCapitalArea();
        if(!stage.getGamingProcess().isLast()) stage.placeCapitalArea();
        stage.getGamingProcess().nextTurn();
    }
}
