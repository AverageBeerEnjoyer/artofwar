package com.mygdx.game.model.maps;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MapCell {
    public CellType type;
    public double height;
    public double humidity;

    public MapCell() {
        this.type = CellType.NOTDEFINED;
        this.height = -1;
        this.humidity = -1;
    }
}
