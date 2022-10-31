package com.mygdx.game.model.maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.view.utils.Pair;

import java.util.Random;

import static com.mygdx.game.model.maps.CellType.LAND;
import static com.mygdx.game.model.maps.CellType.WATER;

public class Map {
    /* coordinates of neighbour cells to current cell
     * row with number i    odd <=> (i & 1) = 1
     *                      even <=> (i & 0) = 0
     * pairs{row dif,column dif}
     */
    public static int[][] neighbourodd = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}};
    public static int[][] neighboureven = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, -1}};

    static final String ANSI_RESET = "\u001B[0m";
    private Random random;
    public MapCell[][] cells;

    private Map(MapCell[][] cells, long seed) {
        this.random = new Random(seed);
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
                int h = (int) (cells[i][j].elevation * 10);
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

    /**
     * creates map with 0.2 < land part < 0.5 else tries again
     * (if mode == 1 land part can be a bit bigger)
     *
     * @param height height of matrix
     * @param width  width of matrix
     * @param mode   0 - continent, 1 - island (could be removed)
     * @param seed   seed to
     * @return Map with only two types of cells: LAND and WATER
     */
    public static Map createMap(int height, int width, int mode, int seed) {
        assert height >= 1 && width >= 1;
        int landcnt = 0;
        Map map = new Map(new MapCell[height][width], seed);
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
        return map;
    }

    /**
     * generate map with BFS from the center
     *
     * @param height
     * @param width
     */
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

    /**
     * set the LAND type to all WATER cells with number of neighbour WATER cells less than 3
     */
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

    /**
     * @param cellType type of cells to count
     * @return matrix with numbers of neighbour cells with chosen type
     */
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
        int[][] nb;
        if ((x & 1) == 1) nb = neighbourodd;
        else nb = neighboureven;
        for(int i=0;i<6;++i){
            if (safeAccess(x+nb[i][0],y+nb[i][1]) != null && safeAccess(x+nb[i][0],y+nb[i][1]).type == cellType) ++res;
        }
        return res;
    }

    /**
     * @param x row
     * @param y column
     * @return cell of map with coordinates(x,y), null if (x,y) out of map
     */
    public MapCell safeAccess(int x, int y) {
        try {
            return cells[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private CellType randomLandOrWater() {
        int res = random.nextInt(2);
        if (res == 0) return LAND;
        return WATER;
    }
}

