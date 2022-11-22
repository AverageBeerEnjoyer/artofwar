package com.mygdx.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Start extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public MenuScreen menuScreen;
    public MainGameScreen mainGameScreen;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        // this.setScreen(new MenuScreen(this));
        menuScreen = new MenuScreen(this);
        mainGameScreen = new MainGameScreen(25,25,this);
        this.setScreen(menuScreen);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}