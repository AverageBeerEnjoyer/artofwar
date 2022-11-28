package com.mygdx.game.controllers.listeners.map_cl;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.controllers.listeners.MainGameStageCL;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.maps.MapCell;

abstract class ActionMapCL extends MainGameStageCL {
    protected final MapCell cell;
    public ActionMapCL(MainGameStage stage, MapCell cell){
        super(stage);
        this.cell = cell;
    }
}
