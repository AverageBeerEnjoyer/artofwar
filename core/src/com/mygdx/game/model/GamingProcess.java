package com.mygdx.game.model;

import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;

import java.util.List;

public class GamingProcess {
    private int round;
    private int currentPlayer;

    private int playersNumber;
    private final Map map;
    private MainGameStage stage;

    public GamingProcess(List<Player> playerList, Map map) {
        this.playersNumber = playerList.size();
        this.map = map;
        this.currentPlayer = 0;
        this.round = 0;
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
    }

    public void nextTurn() {
        nextPlayer();
        if(round == 0) return;
        Player player = getCurrentPlayer();
        player.countIncome();
    }

    public void removeCurrentPlayer() {
        map.getPlayerList().remove(getCurrentPlayer());
        playersNumber = map.getPlayerList().size();
        currentPlayer = (--currentPlayer) % playersNumber;
    }
    public boolean isFirst(){
        return currentPlayer == 0;
    }
    public boolean isLast(){
        return currentPlayer == playersNumber - 1;
    }
}
