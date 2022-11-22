package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.maps.MapCreator;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

import static com.mygdx.game.model.ProjectVariables.*;

public class Militia extends Unit {
    public Militia(
            MapCreator mapCreator,
            MapCell placement,
            Player owner
    ) {
        super(
                mapCreator,
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
