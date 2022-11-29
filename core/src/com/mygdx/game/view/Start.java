package com.mygdx.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.db.DBController;

import java.sql.SQLException;

public class Start extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public MainGameScreen mainGameScreen;

    public Start() {
        super();
        try {
            DBController dbController = new DBController();
            dbController.openConnection();
            dbController.createSchema();
            dbController.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        // this.setScreen(new MenuScreen(this));
        mainGameScreen = new MainGameScreen(25,25,this);
        this.setScreen(mainGameScreen);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}