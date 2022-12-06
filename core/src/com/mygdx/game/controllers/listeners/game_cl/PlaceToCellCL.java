package com.mygdx.game.controllers.listeners.game_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.view.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.maps.MapCell;

public class PlaceToCellCL extends CellActionCL {
    public PlaceToCellCL(MainGameStage stage, MapCell cell) {
        super(stage, cell);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        GameObject object = stage.getGameObjectToPlace();
        if (cell.getGameObject() != null) return;
        stage.getMap().setGameObjectOnCell(cell.x, cell.y, object);
        stage.clearSelectedArea();
    }
}
