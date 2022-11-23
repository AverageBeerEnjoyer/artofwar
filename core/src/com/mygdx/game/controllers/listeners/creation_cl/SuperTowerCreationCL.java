package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.buildings.SuperTower;

public class SuperTowerCreationCL extends GameObjectCreationCl {
    public SuperTowerCreationCL(MainGameStage stage){
        super(stage);
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        SuperTower superTower = new SuperTower(stage.getMap(),null,null);
        stage.setGameObjectToPlace(superTower);
    }
}
