package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.listeners.MainGameStageCL;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.units.Knight;

public class KnightCreationCL extends MainGameStageCL {
    public KnightCreationCL(MainGameStage stage){
        super(stage);
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Knight knight = new Knight(stage.getMap(),null,stage.getGamingProcess().getCurrentPlayer());
        stage.setGameObjectToPlace(knight);
    }
}
