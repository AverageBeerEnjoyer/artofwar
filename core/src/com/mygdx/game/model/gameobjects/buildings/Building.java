package com.mygdx.game.model.gameobjects.buildings;

import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.maps.MapCreator;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class Building extends GameObject {
    public Building(
            MapCreator mapCreator,
            MapCell placement,
            Player owner,
            int cost,
            int moneyPerTurn,
            int defence
    ){
        super(
                mapCreator,
                placement,
                owner,
                cost,
                moneyPerTurn,
                defence
        );
    }
}
