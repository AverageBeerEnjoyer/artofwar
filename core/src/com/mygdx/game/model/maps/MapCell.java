package com.mygdx.game.model.maps;


public class MapCell {
    public CellType type;
    public double elevation;
    public double humidity;

    public MapCell() {
        this.type = CellType.UNDEFINED;
        this.elevation = -1;
        this.humidity = -1;
    }
}
