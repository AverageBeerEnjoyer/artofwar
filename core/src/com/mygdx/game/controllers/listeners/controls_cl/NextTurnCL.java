package com.mygdx.game.controllers.listeners.controls_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.listeners.MainGameStageCL;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.MapCell;

public class NextTurnCL extends MainGameStageCL {

    public NextTurnCL(MainGameStage stage) {
        super(stage);
    }


    @Override
    public void clicked(InputEvent event, float x, float y) {
        stage.getGamingProcess().nextTurn();
    }
}
