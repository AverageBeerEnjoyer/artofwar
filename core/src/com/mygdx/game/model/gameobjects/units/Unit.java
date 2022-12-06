package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.Movable;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class Unit extends GameObject implements Movable {
    private boolean moved;
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
        moved = false;
    }

    public abstract int getPower();

    public abstract int getDistance();

    @Override
    public void move(int x, int y) {
        if (canMove(x, y)) {
            getMap().removeGameObject(this);
            getMap().setGameObjectOnCell(x, y, this);
        }
        moved = true;
    }

    private boolean canMove(int x, int y) {
        MapCell moveTo = getMap().getCell(x, y);
        if (moveTo.getOwner() == owner) return moveTo.getGameObject() == null;
        return getPower() > moveTo.getDefence();
    }

    public boolean isMoved() {
        return moved;
    }
    public void refresh(){
        moved = false;
    }
}
