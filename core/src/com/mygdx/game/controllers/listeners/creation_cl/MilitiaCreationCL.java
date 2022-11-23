package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.units.Militia;

public class MilitiaCreationCL extends GameObjectCreationCl {
    public MilitiaCreationCL(MainGameStage stage){
        super(stage);
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Militia militia = new Militia(stage.getMap(),null,null);
        stage.setGameObjectToPlace(militia);
    }
}
