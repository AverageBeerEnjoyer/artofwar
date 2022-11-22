package com.mygdx.game.model.gameobjects;

import com.mygdx.game.model.maps.MapCreator;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class GameObject {
    private final MapCreator mapCreator;
    private MapCell placement;
    public final Player owner;
    public final int defence;
    public final int moneyPerTurn;
    public final int cost;

    public GameObject(
            MapCreator mapCreator,
            MapCell placement,
            Player owner,
            int cost,
            int moneyPerTurn,
            int defence
    ) {
        this.mapCreator = mapCreator;
        this.placement = placement;
        this.owner = owner;
        this.cost = cost;
        this.moneyPerTurn = moneyPerTurn;
        this.defence = defence;
    }

    public MapCreator getMap() {
        return mapCreator;
    }

    public MapCell getPlacement() {
        return placement;
    }

    public void setPlacement(MapCell placement) {
        this.placement = placement;
    }
}
