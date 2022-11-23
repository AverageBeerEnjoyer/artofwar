package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.gameobjects.units.Paladin;

public class PaladinCreationCL extends GameObjectCreationCl {
    public PaladinCreationCL(MainGameStage stage){
        super(stage);
    }
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Paladin paladin = new Paladin(stage.getMap(),null,null);
        stage.setGameObjectToPlace(paladin);
    }
}
