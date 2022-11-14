package com.mygdx.game.model.maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.view.utils.BiomUtils;
import com.mygdx.game.view.utils.Pair;

import java.util.HashMap;
import java.util.Random;

import static com.mygdx.game.model.maps.CellType.LAND;
import static com.mygdx.game.model.maps.CellType.WATER;

public class Map {
    /**
     * coordinates of neighbour cells to current cell
     * column with number i: odd <=> (i & 1) = 1, even <=> (i & 0) = 0
     * <p>
     * pairs{row dif,column dif}
     */
    public static int[][]
            neighbourodd = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, -1}},
            neighboureven = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}};

    private final Random random;
    private final MapCell[][] cells;
    private final long seed;
    private int mode;
    private double degree = 1.5;
    private int octaves = 2;
    private double persistence = 0.1;
    private final java.util.Map<String, Integer> statInfo = new HashMap<>();

    private final int width, height;

    public Map(int width, int height, int mode, long seed) {
        if (width < 10 || height < 10) throw new IllegalArgumentException("Too small map");
        this.width = width;
        this.height = height;
        this.mode = mode;
        if (seed == -1) {
            this.seed = (long) (Math.random() * 2000);
        } else {
            this.seed = seed;
        }
        random = new Random(this.seed);
        this.cells = new MapCell[width][height];
        createMap();
        view_Up(0);
    }

    /**
     * creates map consists of only 2 types of cells (land and water) with 0.2 < land part < 0.5 else tries again
     * (if mode == 1 land part can be a bit bigger)
     */
    private void createMap() {
        int landcnt = 0;
        while ((double) landcnt / (width * height) < 0.2 || (double) landcnt / (height * width) > 0.5) {
            initMap();
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
    }

    /**
     * generate map with BFS from the center
     */
    private void initMap() {
        for (MapCell[] row : cells) {
            for (int i = 0; i < height; ++i) {
                row[i] = new MapCell();
            }
        }
        Queue<Pair<Integer, Integer>> q = new Queue<>();
        q.addFirst(Pair.pair(width / 2, height / 2));
        while (q.notEmpty()) {
            Pair<Integer, Integer> p = q.removeLast();
            int x = p.first;
            int y = p.second;
            MapCell cell = safeAccess(x, y);
            if (cell == null) continue;
            if (cell.type != CellType.UNDEFINED) continue;

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
        changeCells(CellType.UNDEFINED, WATER);
    }

    private void changeCells(CellType cellTypeOld, CellType cellTypeNew) {
        for (MapCell[] row : cells) {
            for (MapCell mapCell : row) {
                if (mapCell.type == cellTypeOld)
                    mapCell.type = cellTypeNew;
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
        int[][] neighboursLandMap = neighboursMap(LAND);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (cells[i][j].type == WATER && neighboursLandMap[i][j]>=4) {
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
        int[][] neighboursMap = new int[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
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
        for (int i = 0; i < 6; ++i) {
            MapCell cell = safeAccess(x + nb[i][0], y + nb[i][1]);
            if (cell != null && cell.type == cellType) ++res;
        }
        return res;
    }

    public void view_Up(int restart) {
        Perlin2D elevation_map = new Perlin2D(seed);
        Perlin2D humidity_map = new Perlin2D(this.seed + 1234);

        for (int i = 0; i < height; ++i) {//y
            for (int j = 0; j < width; ++j) {//x
                MapCell cell = cells[i][j];
                if (cell.type == CellType.LAND) {
                    double e = elevation_map.getNoise(i / (double) height, j / (double) width, octaves, persistence) + .5f;
//                    e = terrace(e, 22);
//                    e = exponent(e);
                    e = BiomUtils.round(e, 3);
                    cells[i][j].elevation = e;

                    double m = humidity_map.getNoise(i / (double) height, j / (double) width, 10, 0.1f) + .5f;
//                    m = exponent(m);
                    m = BiomUtils.round(m, 3);
                    cells[i][j].humidity = m;
                    cells[i][j].type = CellType.defineBiom(e, m);
                    countStat(cells[i][j].type);
                }
            }
            System.out.println();
        }
        printStat();
    }

    private void printStat(){
        System.out.println();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                System.out.print(cells[i][j].elevation + " ");
            }
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                System.out.print(cells[i][j].humidity + " ");
            }
            System.out.println();
        }
        //        System.out.println("srednee: \n" + countLand / (width * height));
        //        System.out.println(countWater / (width * height));
    }

    private void countStat(CellType t) {
        String key = String.valueOf(t);
        if (!statInfo.containsKey(key)) {
            statInfo.put(key, 1);
            return;
        }
        statInfo.put(key, statInfo.get(key) + 1);
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


    private double exponent(double e) {
        return Math.pow(Math.abs(e), degree);
    }

    private double growUp(double e) {
        double res = e * 1.3;
        if (res > 1) return 1;
        else return res;
    }

    private double terrace(double e, double n) {
        return Math.round(e * n) / n;
    }

    //    private double ridgenoise(double nx, double ny) {//������������� ���
//        return 2 * (0.5 - Math.abs(0.5 - elevation_map.getNoiseStandart((double) nx, (double) ny)));
//    }


    public void setDegree(double e) {
        this.degree = e;
    }

    public void setOctaves(int e) {
        this.octaves = e;
    }

    public void setPersistence(double e) {
        this.persistence = e;
    }

    public long getSeed() {
        return seed;
    }

    public java.util.Map<String, Integer> getStatInfo() {
        return statInfo;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public MapCell[][] getCells() {
        return cells;
    }
}

