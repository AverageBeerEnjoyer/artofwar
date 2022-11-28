package com.mygdx.game.controllers.listeners;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.controllers.stages.MainGameStage;

public class MainGameStageCL extends ClickListener {
    protected final MainGameStage stage;
    public MainGameStageCL(MainGameStage stage){
        this.stage = stage;
    }
}
