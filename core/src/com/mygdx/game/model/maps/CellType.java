package com.mygdx.game.model.maps;

import com.badlogic.gdx.graphics.Color;

public enum CellType {
    NOTDEFINED(null),
    LAND( Color.GREEN),
    BEACH( Color.YELLOW),
    WATER( Color.BLUE),
    MOUNTAIN(Color.BLACK),
    JUNGLE(Color.RED);
    private final Color color;

    CellType(Color color) {
        this.color = color;
    }


    public Color color() {
        return this.color;
    }
}

