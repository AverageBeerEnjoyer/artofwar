package com.mygdx.game.model.gameobjects;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCreator;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class GameObject {
    private final Map map;
    private MapCell placement;
    public final Player owner;
    public final int defence;
    public final int moneyPerTurn;
    public final int cost;

    public GameObject(
            Map map,
            MapCell placement,
            Player owner,
            int cost,
            int moneyPerTurn,
            int defence
    ) {
        this.map = map;
        this.placement = placement;
        this.owner = owner;
        this.cost = cost;
        this.moneyPerTurn = moneyPerTurn;
        this.defence = defence;
    }

    public Map getMap() {
        return map;
    }

    public MapCell getPlacement() {
        return placement;
    }

    public void setPlacement(MapCell placement) {
        this.placement = placement;
    }
}
