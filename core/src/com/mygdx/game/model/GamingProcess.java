package com.mygdx.game.model;

import com.mygdx.game.db.GameDatabase;
import com.mygdx.game.model.gameobjects.buildings.Capital;
import com.mygdx.game.view.stages.MainGameStage;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;

import java.sql.SQLException;

public class GamingProcess {
    private int gameId;
    private int round;
    private int currentPlayer;

    private int playersNumber;
    private final Map map;
    private MainGameStage stage;
    private GameDatabase gameDatabase;

    public GamingProcess(Map map, GameDatabase gameDatabase) {
        this.playersNumber = map.getPlayerList().size();
        this.map = map;
        this.currentPlayer = 0;
        this.round = 0;
        this.gameDatabase = gameDatabase;
    }
    public void setStage(MainGameStage stage){
        this.stage = stage;
    }
    public Player getCurrentPlayer() {
        return map.getPlayerList().get(currentPlayer);
    }

    private void nextPlayer(){
        if(isLast()) ++round;
        currentPlayer = (++currentPlayer) % playersNumber;
        while(getCurrentPlayer().isDone()){
            removeCurrentPlayer();
        }
    }

    public void nextTurn() {
        nextPlayer();
        if(round == 0) return;
        Player player = getCurrentPlayer();
        player.countIncome();
        if(player.getCapital() == null) {
            stage.setGameObjectToPlace(new Capital(map, null, player));
        }
        try {
            gameDatabase.insertMove(player.getId(), gameId, round, player.getGold(), player.getTerritories());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        player.refreshUnits();
        stage.updateInfo();
    }

    public void removeCurrentPlayer() {
        Player player = getCurrentPlayer();
        try {
            gameDatabase.insertMove(player.getId(), gameId, round, player.getGold(), 0);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        map.getPlayerList().remove(getCurrentPlayer());
        playersNumber = map.getPlayerList().size();
        currentPlayer %= playersNumber;
        if(playersNumber == 1) stage.showEndStats();
    }
    public boolean isFirst(){
        return currentPlayer == 0;
    }
    public boolean isLast(){
        return currentPlayer == playersNumber - 1;
    }

    public void setId(int id) {
        this.gameId = id;
    }

    public int getGameId() {
        return gameId;
    }

    public int getRound() {
        return round;
    }
}
