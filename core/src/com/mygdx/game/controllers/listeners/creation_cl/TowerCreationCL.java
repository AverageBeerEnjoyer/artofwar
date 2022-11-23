package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.buildings.Tower;

public class TowerCreationCL extends GameObjectCreationCl {
    public TowerCreationCL(MainGameStage stage){
        super(stage);
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Tower tower = new Tower(stage.getMap(),null,null);
        stage.setGameObjectToPlace(tower);
    }
}
