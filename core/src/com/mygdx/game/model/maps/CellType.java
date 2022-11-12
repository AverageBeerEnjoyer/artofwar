package com.mygdx.game.model.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public enum CellType {
    UNDEFINED(null),
    LAND("sheepHex.gif"),
    BEACH("desertHex.gif"),
    WATER("waterHex.gif"),
    MOUNTAIN("oreHex.gif"),
    JUNGLE("woodHex.gif");
    private final TiledMapTile tile;
    private static final String resourceCatalog = "pastel_resources_hex/rotat/";

    CellType(String tileTexturePath) {
        if (tileTexturePath == null) {
            this.tile = null;
            return;
        }
        this.tile = new StaticTiledMapTile(new TextureRegion(new Texture(Gdx.files.internal(resourceCatalog + tileTexturePath))));
    }

    public static CellType defineBiom(double e, double m) {
        if (e < 0) {
            return CellType.WATER;
        }
        if (e < 0.2) return CellType.BEACH;

        if (e > 0.8) {
            if (m < 0.1) return CellType.MOUNTAIN;
            if (m < 0.2) return CellType.MOUNTAIN;
            if (m < 0.5) return CellType.MOUNTAIN;
            return CellType.MOUNTAIN;
        }

        if (e > 0.6) {
            if (m < 0.33) return CellType.JUNGLE;
            if (m < 0.66) return CellType.JUNGLE;
            return CellType.JUNGLE;
        }

        if (e > 0.3) {
            if (m < 0.16) return CellType.LAND;
            if (m < 0.50) return CellType.LAND;
            if (m < 0.83) return CellType.LAND;
            return CellType.LAND;
        }

        if (m < 0.16) return CellType.LAND;
        if (m < 0.33) return CellType.LAND;
        if (m < 0.66) return CellType.LAND;
        return CellType.LAND;
    }

    public TiledMapTile tile() {
        return this.tile;
    }
}

