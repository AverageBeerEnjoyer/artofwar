package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Militia extends Unit {
    public Militia(Map map, MapCell placement, Player owner) {
        super(map, placement, owner, 2, 2, 5);
    }
}
