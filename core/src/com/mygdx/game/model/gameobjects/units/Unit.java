package com.mygdx.game.model.gameobjects.units;

import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.Movable;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;

public abstract class Unit extends GameObject implements Movable{
    private final int power;
    private final int defence;
    private final int distance;

    public Unit(Map map, MapCell placement, int power, int defence, int distance) {
        super(map, placement);
        this.power = power;
        this.defence = defence;
        this.distance = distance;
    }

    @Override
    public void move(int x, int y) {

    }

    private boolean canMove(int x, int y){
        int[][] availableCells = getMap().selectCellsToMove(x,y);
        int dist = availableCells[x][y];
        return dist>0 && dist<=distance;
    }

    public int getDefence() {
        return defence;
    }

    public int getDistance() {
        return distance;
    }

    public int getPower() {
        return power;
    }


}
