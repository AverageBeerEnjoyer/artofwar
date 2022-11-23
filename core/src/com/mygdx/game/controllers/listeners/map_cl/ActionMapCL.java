package com.mygdx.game.controllers.listeners.map_cl;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.maps.MapCell;

abstract class ActionMapCL extends ClickListener {
    protected final MainGameStage stage;
    protected final MapCell cell;
    public ActionMapCL(MainGameStage stage, MapCell cell){
        this.stage = stage;
        this.cell = cell;
    }
}
