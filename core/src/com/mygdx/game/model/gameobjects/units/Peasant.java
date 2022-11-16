package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Peasant extends Unit {
    public Peasant(
            Map map,
            MapCell placement,
            Player owner) {
        super(
                map,
                placement,
                owner,
                10,
                -1,
                1,
                1,
                5
        );
    }
}
