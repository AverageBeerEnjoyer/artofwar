package com.mygdx.game.model.maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.view.utils.Pair;

import java.util.Objects;

import static com.mygdx.game.model.maps.CellType.LAND;
import static com.mygdx.game.model.maps.CellType.WATER;

public class Map {

    static final String ANSI_RESET = "\u001B[0m";

    private MapCell[][] cells;

    private Map(MapCell[][] cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int width = cells.length;
        int height = cells[0].length;
        for (int i = 0; i < height; ++i) {
            if ((i & 1) == 1) System.out.print(" ");
            for (int j = 0; j < width; ++j) {
                sb.append(cells[i][j].type.color()).append("@").append(ANSI_RESET).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public static Map createMap(int height, int width, int mode) {
        assert height >= 1 && width >= 1;
        int landcnt = 0;
        Map map = new Map(new MapCell[height][width]);
        while ((double) landcnt / (width * height) < 0.2 || (double) landcnt / (height * width) > 0.5) {
            map.initmap(height, width);
            landcnt = map.cntCell(LAND);
            if (mode == 1) {
                for (int i = 0; i < 7; ++i) {
                    map.removeWater();
                }
            }
        }
        if (mode == 0) {
            for (int i = 0; i < 7; ++i) {
                map.removeWater();
            }
        }
        map.setBeaches();
        return map;
    }

    private void initmap(int height, int width) {
        for (MapCell[] row : cells) {
            for (int i = 0; i < row.length; ++i) {
                row[i] = new MapCell();
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
            if (cells[x][y].type == CellType.NOTDEFINED) {
                if (x == width / 2 && y == height / 2)
                    cells[x][y].type = LAND;
                else {
                    int cell = (int) (Math.random() * randomCoef);
                    if (cell == 0)
                        cells[x][y].type = LAND;
                    else cells[x][y].type = WATER;
                }
            } else continue;
            if (cells[x][y].type != CellType.NOTDEFINED && cells[x][y].type != LAND) continue;
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

    private void changeCells(CellType cellTypeOld, CellType cellTypeNew) {
        for (MapCell[] row : cells) {
            for (int i = 0; i < row.length; ++i) {
                if (row[i].type == cellTypeOld)
                    row[i].type = cellTypeNew;
            }
        }
    }

    private int cntCell(CellType cellType) {
        int res = 0;
        for (MapCell[] row : cells) {
            for (MapCell cell : row) {
                if (cell.type == cellType) ++res;
            }
        }
        return res;
    }

    private void removeWater() {
        int[][] neighbours_map = neighboursMap(WATER);
        for (int i = 0; i < neighbours_map.length; ++i) {
            for (int j = 0; j < neighbours_map[0].length; ++j) {
                if (cells[i][j].type == WATER && neighbours_map[i][j] < 3) {
                    cells[i][j].type = LAND;
                }
            }
        }
    }

    private void setBeaches() {
        int[][] neighbourMap = neighboursMap(WATER);
        for (int i = 0; i < neighbourMap.length; ++i) {
            for (int j = 0; j < neighbourMap[0].length; ++j) {
                if (cells[i][j].type == LAND && neighbourMap[i][j] > 0) {
                    cells[i][j].type = CellType.BEACH;
                }
            }
        }
    }

    private int[][] neighboursMap(CellType cellType) {
        int[][] neighboursMap = new int[cells.length][cells[0].length];
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
            if (cells[x][y].type == CellType.NOTDEFINED) return null;
            return cells[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}

