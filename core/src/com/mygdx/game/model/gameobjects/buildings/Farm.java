package com.mygdx.game.model.gameobjects.buildings;

import static com.mygdx.game.model.ProjectVariables.*;

import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Farm extends Building{
    public Farm(
            Map Map,
            MapCell placement,
            Player owner
    ){
        super(
                Map,
                placement,
                owner,
                defaultFarmCost+additionalFarmCost*owner.getFarmCounter(),
                farmMoneyPerTurn,
                farmDefence
        );
    }

    @Override
    public StaticTiledMapTile getTile() {
        return farmPic;
    }

    @Override
    public int getCost() {
        return defaultFarmCost+owner.getFarmCounter()*additionalFarmCost;
    }
}
