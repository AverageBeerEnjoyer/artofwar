package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.ProjectVariables.*;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

import static com.mygdx.game.model.ProjectVariables.*;

public class Militia extends Unit {
    public Militia(
            Map map,
            MapCell placement,
            Player owner
    ) {
        super(
                map,
                placement,
                owner,
                militiaCost,
                militiaMoneyPerTurn,
                militiaPower,
                militiaDefence,
                militiaDistance
        );
    }
}
