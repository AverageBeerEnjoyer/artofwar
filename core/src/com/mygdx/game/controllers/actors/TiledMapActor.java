package com.mygdx.game.controllers.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.view.utils.HexagonUtils;

import static com.mygdx.game.model.ProjectVariables.tileHeight;
import static com.mygdx.game.model.ProjectVariables.tileWidth;

public class TiledMapActor extends Actor {

    public final MainGameStage stage;
    public final MapCell cell;
    public TiledMapActor(MainGameStage stage, MapCell cell, ClickListener listener, int zIndex){
        this.stage = stage;
        this.cell = cell;
        this.addListener(listener);
        setWidth(tileWidth);
        setHeight(tileHeight);
        setPosition(
                HexagonUtils.countXLayout(cell.x, cell.y) * tileWidth,
                HexagonUtils.countYLayout(cell.x, cell.y) * tileHeight
        );
        setZIndex(zIndex);
        debug();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && this.getTouchable()!= Touchable.enabled) return null;
        if (!isVisible()) return null;
        float centerX=getOriginX()+getWidth()/2;
        float centerY=getOriginY()+getHeight()/2;
        float a =(float) Math.sqrt(3)*getWidth()/4;
        float b = getHeight()/2;
        if(Math.sqrt(Math.pow((double) x-centerX,2)/(a*a) + Math.pow((double) y-centerY,2)/(b*b))>1) return null;
        return this;
    }
}
