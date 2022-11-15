package com.mygdx.game.model.gameobjects;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;

public abstract class GameObject {
    private final Map map;
    private MapCell placement;

    public GameObject(Map map, MapCell placement) {
        this.map = map;
        this.placement = placement;
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
