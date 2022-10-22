package com.mygdx.game.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.game.model.maps.MapCell;
import com.mygdx.game.model.maps.MapOperations;

public class WorldMap implements Screen {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001b[33m";
    private int width;
    private int height;
    private MapCell[][] scalesMap;
    TiledMap map;
    TiledMapTileLayer layer;

    public WorldMap(int height, int width) {
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        MapOperations mapOperations = new MapOperations();
        mapOperations.createMap(height, width, 0);
        scalesMap = mapOperations.getMap();

        System.out.println();
        printmap();
    }

    private void printmap() {
        for (int i = 0; i < height; ++i) {
            if ((i & 1) == 1) System.out.print(" ");
            for (int j = 0; j < width; ++j) {
                System.out.print(scalesMap[i][j].type.color() + "@" + ANSI_RESET + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
