package com.mygdx.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.controllers.actors.ActorsFactory;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.controllers.stages.MenuStage;
import com.mygdx.game.db.DBController;
import com.mygdx.game.model.GamingProcess;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtofWar extends Game {

    public SpriteBatch batch;
    public MainGameStage mainGameStage;
    public MenuStage menuStage;
    public ActorsFactory factory;

    public ArtofWar() {
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
        factory = new ActorsFactory();
        menuStage = new MenuStage(this);
        factory.setMenuStage(menuStage);
        this.setScreen(menuStage);
    }
    public void newGame(int width, int height, List<String> playersNames){
        Map map  = new Map(width,height);
        List<Player> players = playersNames.stream().map((name)-> new Player(name, map)).collect(Collectors.toList());
        map.setPlayerList(players);
        GamingProcess gamingProcess = new GamingProcess(map);
        mainGameStage = new MainGameStage(map, gamingProcess, this);
        setScreen(mainGameStage);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
    }

}