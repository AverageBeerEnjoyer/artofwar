package com.mygdx.game.model.gameobjects.units;

import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

import static com.mygdx.game.model.ProjectVariables.*;

public class Militia extends Unit {
    public Militia(
            Map Map,
            MapCell placement,
            Player owner
    ) {
        super(
                Map,
                placement,
                owner,
                militiaCost,
                militiaMoneyPerTurn,
                militiaPower,
                militiaDefence,
                militiaDistance
        );
    }

    @Override
    public StaticTiledMapTile getTile() {
        return militiaPic;
    }

    @Override
    public int getCost() {
        return militiaCost;
    }
}
