package com.mygdx.game.controllers.listeners.map_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.MapCell;

public class MoveToCellCL extends ActionMapCL {
    public MoveToCellCL(MainGameStage stage, MapCell cell) {
        super(stage,cell);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Unit movingUnit = stage.getUnitToMove();
        movingUnit.move(cell.x, cell.y);
        stage.clearSelectedArea();

        stage.clearFogOfWarArea();
        Unit unit = (Unit) cell.getGameObject();
        stage.setFogOfWar(unit);
    }
}
