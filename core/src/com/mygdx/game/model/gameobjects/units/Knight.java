package com.mygdx.game.model.gameobjects.units;

import static com.mygdx.game.model.ProjectVariables.*;

import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public class Knight extends Unit {
    public Knight(
            Map Map,
            MapCell placement,
            Player owner
    ) {
        super(
                Map,
                placement,
                owner,
                knightCost,
                knightMoneyPerTurn,
                knightPower,
                knightDefence,
                knightDistance
        );
    }

    @Override
    public StaticTiledMapTile getTile() {
        return knightPic;
    }

    @Override
    public int getCost() {
        return knightCost;
    }
}
