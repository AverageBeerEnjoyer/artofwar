package com.mygdx.game.model.maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.view.utils.Pair;

import java.util.Random;

import static com.mygdx.game.model.maps.CellType.LAND;
import static com.mygdx.game.model.maps.CellType.WATER;

public class Map {

    public static int[][] neighbourodd = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}};
    public static int[][] neighboureven = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, -1}};

    private static final double persistence = 0.1;
    private static final int octaves = 8;
    private static final int seed = 777;
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
            if ((i & 1) == 1) sb.append(" ");
            for (int j = 0; j < width; ++j) {
                int h = (int) (cells[i][j].height * 10);
                String s;
                if (cells[i][j].type == WATER) s = "@";
                else s = "" + h;
                sb.append(cells[i][j].type.color()).append(s).append(ANSI_RESET).append(" ");
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
            map.initMap(height, width);
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
        map.setRandomHeight();
        //map.setBeaches();
        return map;
    }

    private void initMap(int height, int width) {
        for (MapCell[] row : cells) {
            for (int i = 0; i < row.length; ++i) {
                row[i] = new MapCell();
            }
        }
        Queue<Pair<Integer, Integer>> q = new Queue<>();
        q.addFirst(Pair.pair(height / 2, width / 2));
        while (q.notEmpty()) {
            Pair<Integer, Integer> p = q.removeLast();
            int x = p.first;
            int y = p.second;
            MapCell cell = safeAccess(x, y);
            if (cell == null) continue;
            if (cell.type != CellType.NOTDEFINED) continue;

            if (x == width / 2 && y == height / 2) {
                cell.type = LAND;
            } else cell.type = randomLandOrWater();

            if (cell.type == WATER) continue;
            int[][] nb;
            if ((x & 1) == 1) nb = neighbourodd;
            else nb = neighboureven;
            for (int i = 0; i < nb.length; ++i) {
                int dx = nb[i][0];
                int dy = nb[i][1];
                q.addFirst(Pair.pair(x + dx, y + dy));
            }
        }
        changeCells(CellType.NOTDEFINED, WATER);
    }

    private void setRandomHeight() {
        int height = cells.length;
        int width = cells[0].length;
        double min = 1;
        Perlin2D perlin = new Perlin2D(seed);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (cells[i][j].type == LAND) {
                    double h = perlin.getNoise((double) i / height, (double) j / width, octaves, persistence);
                    h=(h+1)/2;
                    if (h < min) min = h;
                    cells[i][j].height = h;
                }
            }
        }
        for (MapCell[] row : cells) {
            for (MapCell cell : row) {
                if (cell.type == LAND) cell.height -= min;
            }
        }
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
                neighboursMap[i][j] = cntNeighbours(i, j, cellType);
            }
        }
        return neighboursMap;
    }

    private int cntNeighbours(int x, int y, CellType cellType) {
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

    public MapCell safeAccess(int x, int y) {
        try {
            return cells[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private CellType randomLandOrWater() {
        Random r = new Random();
        int res = r.nextInt(2);
        if (res == 0) return LAND;
        return WATER;
    }
}

