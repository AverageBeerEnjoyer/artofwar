package com.mygdx.game.controllers.listeners.creation_cl;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.controllers.stages.MainGameStage;

abstract class GameObjectCreationCl extends ClickListener {
    protected final MainGameStage stage;
    public GameObjectCreationCl(MainGameStage stage){
        this.stage = stage;
    }
}
