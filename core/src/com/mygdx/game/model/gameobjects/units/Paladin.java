package com.mygdx.game.model.gameobjects.units;

import static com.mygdx.game.model.ProjectVariables.*;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Paladin extends Unit {
    public Paladin(
            Map Map,
            MapCell placement,
            Player owner
    ) {
        super(
                Map,
                placement,
                owner,
                paladinCost,
                paladinMoneyPerTurn,
                paladinPower,
                paladinDefence,
                paladinDistance
        );
    }
}
