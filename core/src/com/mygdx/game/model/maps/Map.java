package com.mygdx.game.model.maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.mygdx.game.view.utils.Triple;

import java.util.Arrays;

import static com.mygdx.game.model.maps.CellType.WATER;
import static com.mygdx.game.model.maps.MapCreator.neighboureven;
import static com.mygdx.game.model.maps.MapCreator.neighbourodd;

public class Map {
    private final MapCreator mapCreator;

    public Map(int width, int height) throws IllegalArgumentException {
        this.mapCreator = new MapCreator(width,height, 0, -1);
    }
    public Map(int width, int height, int mode, long seed){
        this.mapCreator = new MapCreator(width, height, mode, seed);
    }
    public MapCell safeAccess(int x, int y){
        return mapCreator.safeAccess(x,y);
    }

    public void setGameObjectOnCell(int x, int y, GameObject gameObject) {
        MapCell cell = mapCreator.safeAccess(x, y);
        if (cell == null) return;
        if (gameObject.getPlacement() != null) gameObject.getPlacement().setGameObject(null);
        gameObject.setPlacement(cell);
        cell.setGameObject(gameObject);
    }
    public int[][] selectCellsToMove(int xValue, int yValue) {
        int[][] mirror = new int[mapCreator.getWidth()][mapCreator.getHeight()];
        Arrays.fill(mirror, -1);
        MapCell startCell;
        Unit unit;
        try {
            startCell = safeAccess(xValue, yValue);
            unit = (Unit) startCell.getGameObject();
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
        Queue<Triple<Integer, Integer, Integer>> q = new Queue<>();
        q.addFirst(Triple.triple(xValue, yValue, unit.distance));
        while (q.notEmpty()) {
            Triple<Integer, Integer, Integer> t = q.removeLast();
            int x = t.first;
            int y = t.second;
            int n = t.third;
            MapCell cell = safeAccess(x, y);

            boolean stop = false;
            if (cell == null) continue;
            if (cell.getType() == WATER) continue;
            if (mirror[x][y] > 0) stop = true;
            if (!startCell.getOwner().equals(cell.getOwner()) && cell.getDefence() >= unit.power) continue;

            mirror[x][y] = Math.max(mirror[x][y], n);

            if (n <= 0 || stop) continue;
            int[][] nb;
            if ((x & 1) == 1) nb = neighbourodd;
            else nb = neighboureven;
            for (int[] ints : nb) {
                int dx = ints[0];
                int dy = ints[1];
                q.addFirst(Triple.triple(x + dx, y + dy, n - 1));
            }
        }
        return mirror;
    }
    public int getWidth(){
        return mapCreator.getWidth();
    }
    public int getHeight(){
        return mapCreator.getHeight();
    }
    public MapCell[][] getCells(){
        return mapCreator.getCells();
    }
    public java.util.Map<String, Integer> getStatInfo() {
        return mapCreator.getStatInfo();
    }
    public MapCreator getMapCreator(){
        return this.mapCreator;
    }
    public void setDegree(double e) {
        this.mapCreator.setDegree(e);
    }

    public void setOctaves(int e) {
        this.mapCreator.setOctaves(e);
    }

    public void setPersistence(double e) {
        this.mapCreator.setPersistence(e);
    }
}
