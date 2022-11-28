package com.mygdx.game.model;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;

import java.util.List;

public class GamingProcess {
    private int round;
    private int currentPlayer;
    private final List<Player> playerList;
    private int playersNumber;
    private final Map map;

    public GamingProcess(List<Player> playerList, Map map) {
        this.playerList = playerList;
        this.playersNumber = playerList.size();
        this.map = map;
        this.currentPlayer = 0;
        this.round = 0;
    }

    public Player getCurrentPlayer() {
        return playerList.get(currentPlayer);
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
        playerList.remove(getCurrentPlayer());
        playersNumber = playerList.size();
        currentPlayer = (--currentPlayer) % playersNumber;
    }
    public boolean isFirst(){
        return currentPlayer == 0;
    }
    public boolean isLast(){
        return currentPlayer == playersNumber - 1;
    }
}
