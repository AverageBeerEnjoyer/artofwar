package com.mygdx.game.controllers;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.MapCell;

public class SelectCellClickListener extends ClickListener {
    private TiledMapActor actor;

    SelectCellClickListener(TiledMapActor actor){
        this.actor = actor;
    }
    @Override
    public void clicked(InputEvent event, float x, float y){
        System.out.println(actor.getUserObject().toString()+" click.");
        MapCell cell = (MapCell) actor.getUserObject();
        GameObject gameObject = cell.getGameObject();
        if(!(gameObject instanceof Unit)) return;

    }
}
