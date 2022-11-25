package com.mygdx.game.model;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;

import java.util.List;

public class GamingProcess {
    private int currentPlayer;
    private final List<Player> playerList;
    private int playersNumber;
    private final Map map;

    public GamingProcess(List<Player> playerList, Map map) {
        this.playerList = playerList;
        this.playersNumber = playerList.size();
        this.map = map;
        this.currentPlayer = 0;
    }

    public Player getCurrentPlayer() {
        return playerList.get(currentPlayer);
    }

    public void nextTurn() {
        currentPlayer = (++currentPlayer) % playersNumber;
        Player player = getCurrentPlayer();
        player.countIncome();
    }

    public void removeCurrentPlayer() {
        playerList.remove(getCurrentPlayer());
        playersNumber = playerList.size();
        currentPlayer = (--currentPlayer) % playersNumber;
    }
}
