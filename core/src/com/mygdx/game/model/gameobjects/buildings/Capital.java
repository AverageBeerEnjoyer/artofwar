package com.mygdx.game.model.gameobjects.buildings;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Capital extends Building{
    public Capital(
            Map map,
            MapCell placement,
            Player owner
    ){
        super(
                map,
                placement,
                owner,
                0,
                2,
                1
        );
    }
}