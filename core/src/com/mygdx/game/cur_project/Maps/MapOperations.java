package com.mygdx.game.cur_project.Maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.cur_project.utils.MyUtils;
import com.mygdx.game.cur_project.utils.Pair;

import static com.mygdx.game.cur_project.Maps.CellType.LAND;
import static com.mygdx.game.cur_project.Maps.CellType.WATER;

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

public class MapOperations {

    private MapCell[][] map;

    public MapCell[][] getMap() {
        return map;
    }

    public void createMap(int height, int width, int mode) {

        int landcnt = 0;
        while ((double) landcnt / (width * height) < 0.2 || (double) landcnt / (height * width) > 0.5) {
            initmap(height, width);
            landcnt = cntCell(LAND);
            if (mode == 1) {
                for (int i = 0; i < 7; ++i) {
                    removeWater();
                }
            }
        }
        if (mode == 0) {
            for (int i = 0; i < 7; ++i) {
                removeWater();
            }
        }
        setBeaches();
    }

    private void initmap(int height, int width) {
        map = new MapCell[height][width];
        for(MapCell[] row:map){
            for(int i=0;i<row.length;++i){
                row[i]=new MapCell();
            }
        }
        double randomCoef = 1.999;

        Queue<Pair<Integer, Integer>> q = new Queue<>();
        q.addFirst(Pair.pair(height / 2, width / 2));

        while (q.notEmpty()) {
            Pair<Integer, Integer> p = q.removeLast();
            int x = p.first;
            int y = p.second;
            if (x >= height || x < 0) continue;
            if (y >= width || y < 0) continue;
            if (map[x][y].type == CellType.NOTDEFINED) {
                if (x == width / 2 && y == height / 2)
                    map[x][y].type = LAND;
                else {
                    int cell = (int) (Math.random() * randomCoef);
                    if (cell == 0)
                        map[x][y].type = LAND;
                    else map[x][y].type = WATER;
                }
            } else continue;
            if (map[x][y].type != CellType.NOTDEFINED && map[x][y].type != LAND) continue;
            q.addFirst(Pair.pair(x - 1, y));
            q.addFirst(Pair.pair(x + 1, y));
            q.addFirst(Pair.pair(x, y - 1));
            q.addFirst(Pair.pair(x, y + 1));
            if ((x & 1) == 1) {
                q.addFirst(Pair.pair(x - 1, y + 1));
                q.addFirst(Pair.pair(x + 1, y + 1));
            } else {
                q.addFirst(Pair.pair(x - 1, y - 1));
                q.addFirst(Pair.pair(x + 1, y - 1));
            }
        }
        changeCells(CellType.NOTDEFINED, WATER);
    }

    private void removeWater() {
        int[][] neighbours_map = neighboursMap(WATER);
        for (int i = 0; i < neighbours_map.length; ++i) {
            for (int j = 0; j < neighbours_map[0].length; ++j) {
                if (map[i][j].type == WATER && neighbours_map[i][j] < 3) {
                    map[i][j].type = LAND;
                }
            }
        }
    }

    private void setBeaches() {
        int[][] neighbourMap = neighboursMap(WATER);
        for (int i = 0; i < neighbourMap.length; ++i) {
            for (int j = 0; j < neighbourMap[0].length; ++j) {
                if (map[i][j].type == LAND && neighbourMap[i][j] > 0) {
                    map[i][j].type = CellType.BEACH;
                }
            }
        }
    }

    private int[][] neighboursMap(CellType cellType) {
        int[][] neighboursMap = new int[map.length][map[0].length];
        for (int i = 0; i < neighboursMap.length; ++i) {
            for (int j = 0; j < neighboursMap[0].length; ++j) {
                neighboursMap[i][j] = cnt_neighbours(i, j, cellType);
            }
        }
        return neighboursMap;
    }

    private int cnt_neighbours(int x, int y, CellType cellType) {
        int res = 0;
        if (safeAccess(x - 1, y) != null && safeAccess(x - 1, y).type == cellType) ++res;
        if (safeAccess(x + 1, y) != null && safeAccess(x + 1, y).type == cellType) ++res;
        if (safeAccess(x, y + 1) != null && safeAccess(x, y + 1).type == cellType) ++res;
        if (safeAccess(x, y - 1) != null && safeAccess(x, y - 1).type == cellType) ++res;
        if ((x & 1) == 1) {
            if (safeAccess(x - 1, y + 1) != null && safeAccess(x - 1, y + 1).type == cellType) ++res;
            if (safeAccess(x + 1, y + 1) != null && safeAccess(x + 1, y + 1).type == cellType) ++res;
        } else {
            if (safeAccess(x - 1, y - 1) != null && safeAccess(x - 1, y - 1).type == cellType) ++res;
            if (safeAccess(x + 1, y - 1) != null && safeAccess(x + 1, y - 1).type == cellType) ++res;
        }
        return res;
    }

    private MapCell safeAccess(int x, int y) {
        try {
            if (map[x][y].type == CellType.NOTDEFINED) return null;
            return map[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private void changeCells(CellType cellTypeOld, CellType cellTypeNew) {
        for (MapCell[] row : map) {
            for (int i = 0; i < row.length; ++i) {
                if (row[i].type == cellTypeOld)
                    row[i].type = cellTypeNew;
            }
        }

    }

    private int cntCell(CellType cellType) {
        int res = 0;
        for (MapCell[] row : map) {
            for (MapCell cell : row) {
                if (cell.type == cellType) ++res;
            }
        }
        return res;
    }
}
