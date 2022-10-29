package com.mygdx.game.model.maps;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MapCell {
    CellType type;
    int height;

    public MapCell() {
        this.type = CellType.NOTDEFINED;
        this.height = -1;
    }
}
