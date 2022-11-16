package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.Movable;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.players.Player;

public abstract class Unit extends GameObject implements Movable {
    public final int power;
    public final int distance;

    public Unit(
            Map map,
            MapCell placement,
            Player owner,
            int cost,
            int moneyPerTurn,
            int power,
            int defence,
            int distance
    ) {
        super(
                map,
                placement,
                owner,
                cost,
                moneyPerTurn,
                defence
        );
        this.power = power;
        this.distance = distance;
    }

    @Override
    public void move(int x, int y) {
        if (canMove(x, y)) getMap().setGameObjectOnCell(x, y, this);
    }

    private boolean canMove(int x, int y) {
        int[][] availableCells = getMap().selectCellsToMove(x, y);
        int dist = availableCells[x][y];
        return dist > 0 && dist <= distance;
    }


}
