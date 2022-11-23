package com.mygdx.game.controllers.listeners.map_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.MapCell;

public class SelectCellCL extends ActionMapCL {

    public SelectCellCL(MainGameStage stage, MapCell cell) {
        super(stage, cell);
    }


    @Override
    public void clicked(InputEvent event, float x, float y) {
        if (!(cell.getGameObject() instanceof Unit)) return;
        Unit unit = (Unit) cell.getGameObject();
        stage.setUnitToMove(unit);
    }
}
