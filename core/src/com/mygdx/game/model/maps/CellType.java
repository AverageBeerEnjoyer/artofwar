package com.mygdx.game.model.maps;

enum CellType {
    NOTDEFINED(-1, ""),
    LAND(0, "\u001B[32m"),
    BEACH(1, "\u001b[33m"),
    WATER(2, "\u001b[34m");
    private final int type;
    private final String color;

    CellType(int type, String color) {
        this.type = type;
        this.color = color;
    }

    public int type() {
        return this.type;
    }

    public String color() {
        return this.color;
    }
}

