package com.mygdx.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.controllers.stages.MenuStage;
import com.mygdx.game.db.DBController;

import java.sql.SQLException;

public class Start extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public MainGameScreen mainGameScreen;

    public TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

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
        font = new BitmapFont();
        style.font =font;// use libGDX's default Arial font
        // this.setScreen(new MenuScreen(this));
        mainGameScreen = new MainGameScreen(100,100,this);
        MenuStage menu = new MenuStage(this);
        this.setScreen(menu);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}