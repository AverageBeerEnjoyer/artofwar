package com.mygdx.game.model.gameobjects.units;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

import static com.mygdx.game.model.ProjectVariables.*;

public class Peasant extends Unit {
    public Peasant(
            Map Map,
            MapCell placement,
            Player owner) {
        super(
                Map,
                placement,
                owner,
                peasantCost,
                peasantMoneyPerTurn,
                peasantPower,
                peasantDefence,
                peasantDistance
        );
    }

    @Override
    public int getCost() {
        return peasantCost;
    }

    @Override
    public TiledMapTile getTile() {
        return peasantPic;
    }
}
