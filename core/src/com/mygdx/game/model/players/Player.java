package com.mygdx.game.model.players;

import com.mygdx.game.ProjectVariables;
import com.mygdx.game.model.GamingProcess;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.buildings.Building;
import com.mygdx.game.model.gameobjects.buildings.Capital;
import com.mygdx.game.model.gameobjects.buildings.Farm;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.maps.*;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id = -1;
    private GamingProcess gamingProcess;
    public static final Player NOBODY = new Player("", null);
    public final Border border;
    public final String name;
    private final List<Building> buildings;
    private final List<Unit> units;
    private final List<Farm> farms;

    boolean done = false;
    private Capital capital;
    private int gold = 0;
    private int territory = 0;

    public Player(String name, Border border) {
        this.border = border;
        this.name = name;
        buildings = new ArrayList<>();
        units = new ArrayList<>();
        farms = new ArrayList<>();
    }

    public Player(Player player){
        this.id = player.id;
        this.gamingProcess = player.gamingProcess;
        this.border = player.border;
        this.name = player.name;
        this.buildings = player.buildings;
        this.units = player.units;
        this.farms = player.farms;

        this.done = player.done;
        this.capital = player.capital;
        this.gold = player.gold;
        this.territory = player.territory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFarmsNumber() {
        return farms.size();
    }

    public void removeGameObject(GameObject gameObject) {
        if (gameObject instanceof Unit) {
            removeUnit((Unit) gameObject);
        }
        if (gameObject instanceof Building) {
            if(gameObject instanceof Capital){
                this.capital = null;
            }
            if(gameObject instanceof Farm){
                removeFarm((Farm) gameObject);
            } else {
                removeBuilding((Building) gameObject);
            }
        }
    }
    public void addGameObject(GameObject gameObject) {
        gold -= gameObject.getCost();
        if (gameObject instanceof Unit) {
            addUnit((Unit) gameObject);
        }
        if (gameObject instanceof Building) {
            if (gameObject instanceof Capital) {
                capital = (Capital) gameObject;
            }
            if (gameObject instanceof Farm) {
                addFarm((Farm) gameObject);
            } else {
                addBuilding((Building) gameObject);
            }
        }
    }

    private void addFarm(Farm farm){
        if(farms.contains(farm)) return;
        farms.add(farm);
    }

    private void addBuilding(Building building) {
        if(buildings.contains(building)) return;
        buildings.add(building);
    }

    private void addUnit(Unit unit) {
        if (units.contains(unit)) return;
        units.add(unit);
    }

    public GamingProcess getGamingProcess() {
        return gamingProcess;
    }

    private void removeFarm(Farm farm){
        farms.remove(farm);
    }

    private void removeBuilding(Building building) {
        buildings.remove(building);
    }

//    public void createCapitalArea() {
//        int x = capital.getPlacement().x;
//        int y = capital.getPlacement().y;
//        int[][] nb = MapCreator.getNeighbours(x);
//        for (int i = 0; i < 6; ++i) {
//            int dx = nb[i][0];
//            int dy = nb[i][1];
//            MapCell cell = gamingProcess.getMap().getCell(x + dx, y + dy);
//            if (cell == null) continue;
//            if (cell.getType() != CellType.WATER && cell.getOwner() == NOBODY) {
//                cell.setOwner(this);
//            }
//            //TODO убрать
//            gamingProcess.getMap().getMapToRendererTransformator().update(x + dx, x + dy);
//        }
//    }



    private void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public void armyWipe() {
        gold = 0;
    }
    public void refreshUnits(){
        for(Unit unit:units){
            unit.setMoved(false);
        }
    }
    public boolean countIncome() {
        if(capital!=null){
            gold+=getFarmsNumber()*ProjectVariables.BuildingSpec.farmMoneyPerTurn;
            gold+=capital.getMoneyPerTurn();
            gold+=territory;
        }
        for (Building building : buildings) {
            gold += building.getMoneyPerTurn();
        }
        for (Unit unit : units) {
            gold += unit.getMoneyPerTurn();
        }
        if (gold < 0) {
            armyWipe();
            gold = 0;
            return false;
        }
        return true;
    }

    public void setCapital(Capital capital){}

    public boolean isDone() {
        return done;
    }

    public void addTerritory() {
        ++territory;
    }

    public void removeTerritory() {
        --territory;
        if (territory == 0) done = true;
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

    public int getGold() {
        return gold;
    }

    public int getId() {
        return id;
    }

    public int getTerritories() {
        return territory;
    }

    public void setGamingProcess(GamingProcess gamingProcess){
        this.gamingProcess = gamingProcess;
    }
}
