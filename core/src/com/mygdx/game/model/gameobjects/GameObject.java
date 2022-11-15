package com.mygdx.game.model.gameobjects;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class GameObject {
    private final Map map;
    private MapCell placement;
    private final Player owner;

    public GameObject(Map map, MapCell placement, Player owner) {
        this.map = map;
        this.placement = placement;
        this.owner=owner;
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
