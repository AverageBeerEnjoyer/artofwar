package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.buildings.Farm;
public class FarmCreationCL extends GameObjectCreationCl {
    public FarmCreationCL(MainGameStage stage){
        super(stage);
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Farm farm = new Farm(stage.getMap(),null,null);
        stage.setGameObjectToPlace(farm);
    }
}
