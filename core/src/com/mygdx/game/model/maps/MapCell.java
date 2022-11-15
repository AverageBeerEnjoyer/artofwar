package com.mygdx.game.model.maps;


import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.players.Player;

public class MapCell {
    private CellType type;
    private double elevation;
    private double humidity;
    private int defence;
    private final int x, y;
    private Player owner;
    private GameObject gameObject;

    public MapCell(int x, int y) {
        this.type = CellType.UNDEFINED;
        this.elevation = -1;
        this.humidity = -1;
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getElevation() {
        return elevation;
    }

    public CellType getType() {
        return type;
    }

    public Player getOwner() {
        return owner;
    }

    public int getDefence() {
        return defence;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
