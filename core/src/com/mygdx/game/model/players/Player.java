package com.mygdx.game.model.players;

import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.buildings.Building;
import com.mygdx.game.model.gameobjects.buildings.Capital;
import com.mygdx.game.model.gameobjects.buildings.Farm;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.CellType;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.maps.MapCreator;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Map map;
    public static Player NOBODY = new Player("", null);
    public final String name;
    private final List<Building> buildings;
    private final List<Unit> units;

    boolean done = false;
    private Capital capital;
    private int farmCounter;
    private int gold;
    private int territory = 0;

    public Player(String name, Map map) {
        this.map = map;
        this.name = name;
        buildings = new ArrayList<>();
        units = new ArrayList<>();
    }

    public int getFarmCounter() {
        return farmCounter;
    }

    public void removeGameObject(GameObject gameObject) {
        if (gameObject instanceof Unit) {
            removeUnit((Unit) gameObject);
            return;
        }
        if (gameObject instanceof Building) {
            removeBuilding((Building) gameObject);
            return;
        }
        capital = null;
    }

    public void addGameObject(GameObject gameObject) {
        gold -= gameObject.getCost();
        if (gameObject instanceof Unit) {
            addUnit((Unit) gameObject);
            return;
        }
        if (gameObject instanceof Building) {
            if(gameObject instanceof Capital){
                capital = (Capital) gameObject;
                return;
            }
            addBuilding((Building) gameObject);
            if (gameObject instanceof Farm) {
                ++farmCounter;
            }
            return;
        }
    }

    private void addBuilding(Building building) {
        if (buildings.contains(building)) return;
        buildings.add(building);
    }

    private void addUnit(Unit unit) {
        if (units.contains(unit)) return;
        units.add(unit);
    }

    private void removeBuilding(Building building) {
        if (building instanceof Farm) --farmCounter;
        buildings.remove(building);
    }

    public void createCapitalArea() {
        int x = capital.getPlacement().x;
        int y = capital.getPlacement().y;
        int[][] nb = MapCreator.getNeighbours(x);
        for (int i = 0; i < 6; ++i) {
            int dx = nb[i][0];
            int dy = nb[i][1];
            MapCell cell = map.getCell(x + dx, y + dy);
            if (cell == null) continue;
            if (cell.getType() != CellType.WATER && cell.getOwner() == NOBODY) {
                cell.setOwner(this);
            }
            map.getMapToRendererTransformator().update(x + dx, x + dy);
        }
    }

    private void removeUnit(Unit unit) {
        units.remove(unit);
    }

    private void armyWipe() {
        for (Unit unit : units) {
            map.removeGameObject(unit);
            map.killGameObject(unit);
        }
    }

    public void countIncome() {
        for (Building building : buildings) {
            gold += building.getMoneyPerTurn();
        }
        for (Unit unit : units) {
            gold += unit.getMoneyPerTurn();
        }
        gold += capital.getMoneyPerTurn();
        if (gold < 0) {
            armyWipe();
        }
    }

    public boolean isDone(){
        return done;
    }

    public void addTerritory(){
        ++territory;
    }

    public void removeTerritory(){
        --territory;
        if(territory == 0) done = true;
    }

    public Capital getCapital() {
        return capital;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public List<Unit> getUnits() {
        return units;
    }
}
