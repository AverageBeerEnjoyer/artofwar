package com.mygdx.game.model.gameobjects.buildings;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Farm extends Building{
    public Farm(
            Map map,
            MapCell placement,
            Player owner
    ){
        super(
                map,
                placement,
                owner,
                15,
                5,
                0
        );
    }
}
