package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Knight extends Unit {
    public Knight(
            Map map,
            MapCell placement,
            Player owner
    ) {
        super(
                map,
                placement,
                owner,
                30,
                -10,
                3,
                3,
                4
        );
    }
}
