package com.mygdx.game.cur_project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.cur_project.perlins.scrin;

public class Start extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        this.setScreen(new MenuScreen(this));

//        scrin a= new scrin();
//        System.out.println("Type 1");
//        a.view();
//        System.out.println("Type 2");
//        a.view_Up(0);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}