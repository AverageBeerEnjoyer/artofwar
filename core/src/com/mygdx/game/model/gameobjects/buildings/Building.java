package com.mygdx.game.model.gameobjects.buildings;

import com.mygdx.game.model.ProjectVariables;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCreator;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class Building extends GameObject {
    public Building(
            Map map,
            MapCell placement,
            Player owner,
            int cost,
            int moneyPerTurn,
            int defence
    ){
        super(
                map,
                placement,
                owner,
                cost,
                moneyPerTurn,
                defence
        );
    }
}
