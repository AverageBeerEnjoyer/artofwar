package com.mygdx.game.model.players;

import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.buildings.Building;
import com.mygdx.game.model.gameobjects.buildings.Capital;
import com.mygdx.game.model.gameobjects.buildings.Farm;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.Map;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Map map;
    public static Player NOBODY = new Player("", null);
    public final String name;
    private final List<Building> buildings;
    private final List<Unit> units;

    private Capital capital;
    private int farmCounter;
    private int gold;

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
        gold-=gameObject.getCost();
        if (gameObject instanceof Unit) {
            addUnit((Unit) gameObject);
            return;
        }
        if (gameObject instanceof Building) {
            addBuilding((Building) gameObject);
            if(gameObject instanceof Farm){
                ++farmCounter;
            }
            return;
        }
        capital = (Capital) gameObject;
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
        if(building instanceof Farm) --farmCounter;
        buildings.remove(building);
    }

    private void removeUnit(Unit unit) {
        units.remove(unit);
    }
    private void armyWipe(){
        for(Unit unit:units){
            map.removeGameObject(unit);
            map.killGameObject(unit);
        }
    }
    public void countIncome() {
        for (Building building : buildings) {
            gold += building.getMoneyPerTurn();
        }
        for(Unit unit: units){
            gold+=unit.getMoneyPerTurn();
        }
        gold += capital.getMoneyPerTurn();
        if (gold < 0) {
            armyWipe();
        }
    }
}
