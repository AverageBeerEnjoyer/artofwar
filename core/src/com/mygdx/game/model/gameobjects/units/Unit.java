package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.Movable;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class Unit extends GameObject implements Movable {

    public Unit(
            Map Map,
            MapCell placement,
            Player owner
    ) {
        super(
                Map,
                placement,
                owner
        );
    }

    public abstract int getPower();

    public abstract int getDistance();

    @Override
    public void move(int x, int y) {
        if (canMove(x, y)) {
            getMap().removeGameObject(this);
            getMap().setGameObjectOnCell(x, y, this);
        }
    }

    private boolean canMove(int x, int y) {
        MapCell moveTo = getMap().getCell(x, y);
        if (moveTo.getOwner() == owner) return moveTo.getGameObject() == null;
        return getPower() > moveTo.getDefence();
    }
}
