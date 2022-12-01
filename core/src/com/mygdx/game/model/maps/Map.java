package com.mygdx.game.model.maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.controllers.MapToRendererTransformator;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.buildings.Building;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.model.players.Player;
import com.mygdx.game.view.utils.Triple;

import java.util.Arrays;
import java.util.List;

import static com.mygdx.game.model.maps.CellType.WATER;

public class Map {
    private final MapCreator mapCreator;
    private final MapToRendererTransformator mapToRendererTransformator;
    private List<Player> playerList;

    public Map(int width, int height) throws IllegalArgumentException {
        this.mapCreator = new MapCreator(width, height, 0, -1);
        this.mapToRendererTransformator = new MapToRendererTransformator(this);
    }

    public Map(int width, int height, int mode, long seed) {
        this.mapCreator = new MapCreator(width, height, mode, seed);
        this.mapToRendererTransformator = new MapToRendererTransformator(this);
    }

    public MapCell getCell(int x, int y) {
        return mapCreator.safeAccess(x, y);
    }

    public void killGameObject(GameObject gameObject) {
        gameObject.owner.removeGameObject(gameObject);
        removeGameObject(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObject.getPlacement().setGameObject(null);
        recountDefenceCoverage();
        mapToRendererTransformator.update(gameObject.getPlacement().x, gameObject.getPlacement().y);
    }

    public void setGameObjectOnCell(int x, int y, GameObject gameObject) {
        MapCell cell = mapCreator.safeAccess(x, y);
        if (cell == null) return;
        if (gameObject.getPlacement() == null) {
            gameObject.owner.addGameObject(gameObject);
        }
        if (cell.getGameObject() != null) {
            killGameObject(gameObject);
        }
        gameObject.setPlacement(cell);
        cell.setGameObject(gameObject);
        cell.setOwner(gameObject.owner);
        recountDefenceCoverage();
        mapToRendererTransformator.update(x, y);
    }

    public int[][] selectCellsToMove(int xValue, int yValue) {
        int[][] mirror = new int[mapCreator.getWidth()][mapCreator.getHeight()];
        for (int[] row : mirror) {
            Arrays.fill(row, -1);
        }
        MapCell startCell;
        Unit unit;
        try {
            startCell = getCell(xValue, yValue);
            unit = (Unit) startCell.getGameObject();
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
        Queue<Triple<Integer, Integer, Integer>> q = new Queue<>();
        q.addFirst(Triple.triple(xValue, yValue, unit.getDistance()));
        while (q.notEmpty()) {
            Triple<Integer, Integer, Integer> t = q.removeLast();
            int x = t.first;
            int y = t.second;
            int n = t.third;
            MapCell cell = getCell(x, y);

            boolean stop = false;
            if (cell == null) continue;
            if (cell.getType() == WATER) continue;
            if (mirror[x][y] > 0) stop = true;
            if (!startCell.getOwner().equals(cell.getOwner())) {
                if (cell.getDefence() >= unit.getPower()) continue;
                stop = true;
            }
            mirror[x][y] = Math.max(mirror[x][y], n);

            if (n <= 0 || stop) continue;
            int[][] nb = MapCreator.getNeighbours(x);
            for (int[] ints : nb) {
                int dx = ints[0];
                int dy = ints[1];
                q.addFirst(Triple.triple(x + dx, y + dy, n - 1));
            }
        }
        return mirror;
    }

    public int[][] getPlayerTerritory(Player player) {
        int[][] territory = new int[getWidth()][getHeight()];
        for (int[] row : territory) {
            Arrays.fill(row, -1);
        }
        for (int i = 0; i < getWidth(); ++i) {
            for (int j = 0; j < getHeight(); ++j) {
                if (mapCreator.getCells()[i][j].getOwner() == player) {
                    territory[i][j] = 0;
                }
            }
        }
        return territory;
    }

    private void countGameObjectCoverage(GameObject gameObject) {
        int x = gameObject.getPlacement().x;
        int y = gameObject.getPlacement().y;
        int[][] nb = MapCreator.getNeighbours(x);
        MapCell cell;
        for (int[] d : nb) {
            cell = getCell(x + d[0], y + d[1]);
            if (cell == null || cell.getOwner() != gameObject.owner) continue;
            cell.setDefence(Math.max(cell.getDefence(), gameObject.getDefence()));
        }
        cell = gameObject.getPlacement();
        cell.setDefence(Math.max(cell.getDefence(), gameObject.getDefence()));
    }

    private void recountDefenceCoverage() {
        for (int i = 0; i < getWidth(); ++i) {
            for (int j = 0; j < getHeight(); ++j) {
                getCell(i, j).setDefence(0);
            }
        }
        for (Player player : playerList) {
            for (Building building : player.getBuildings()) {
                countGameObjectCoverage(building);
            }
            for (Unit unit : player.getUnits()) {
                countGameObjectCoverage(unit);
            }
            if (player.getCapital() != null)
                countGameObjectCoverage(player.getCapital());
        }
    }

    public int getWidth() {
        return mapCreator.getWidth();
    }

    public int getHeight() {
        return mapCreator.getHeight();
    }

    public MapCreator getMapCreator() {
        return mapCreator;
    }

    public MapToRendererTransformator getMapToRendererTransformator() {
        return mapToRendererTransformator;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
