package com.mygdx.game.model.maps;

public class MapCell {
    CellType type;
    int height;

    public MapCell() {
        this.type = CellType.NOTDEFINED;
        this.height = 0;
    }
}
