package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.listeners.MainGameStageCL;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.units.Militia;

public class MilitiaCreationCL extends MainGameStageCL {
    public MilitiaCreationCL(MainGameStage stage){
        super(stage);
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Militia militia = new Militia(stage.getMap(),null,stage.getGamingProcess().getCurrentPlayer());
        stage.setGameObjectToPlace(militia);
    }
}
