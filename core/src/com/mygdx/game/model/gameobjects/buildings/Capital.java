package com.mygdx.game.model.gameobjects.buildings;

import static com.mygdx.game.model.ProjectVariables.*;
import com.mygdx.game.model.maps.MapCreator;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Capital extends Building{
    public Capital(
            MapCreator mapCreator,
            MapCell placement,
            Player owner
    ){
        super(
                mapCreator,
                placement,
                owner,
                capitalCost,
                capitalMoneyPerTurn,
                capitalDefence
        );
    }
}
