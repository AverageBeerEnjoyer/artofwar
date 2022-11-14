package controllers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TiledMapClickListener extends ClickListener {
    private TiledMapActor actor;

    TiledMapClickListener(TiledMapActor actor){
        this.actor = actor;
    }
    @Override
    public void clicked(InputEvent event, float x, float y){
        System.out.println(actor.cell+" click.");
    }
}
