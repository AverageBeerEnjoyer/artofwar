package com.mygdx.game.model.maps;

import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.model.gameobjects.GameObject;
import com.mygdx.game.model.gameobjects.units.Unit;
import com.dongbat.jbump.Cell;
import com.mygdx.game.view.utils.BiomUtils;
import com.mygdx.game.view.utils.Pair;
import com.mygdx.game.view.utils.Triple;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static com.mygdx.game.model.maps.CellType.*;

public class MapCreator {
    /**
     * <p>
     * coordinates of neighbour cells to current cell<br>
     * column with number i:<br> odd <=> (i & 1) = 1<br> even <=> (i & 0) = 0
     * </p>
     * <p>
     * pairs{row dif,column dif}
     * </p>
     */
    public static final int[][]
            neighbourodd = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, -1}},
            neighboureven = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}};

    private final Random random;
    private final MapCell[][] cells;
    private final long seed;
    private final int mode;

    private double degree = 1.5;
    private int octaves = 2;
    private double persistence = 0.1;

    private final java.util.Map<String, Integer> statInfo = new HashMap<>();

    private final int width, height;

    public MapCreator(int width, int height, int mode, long seed) throws IllegalArgumentException {
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

        //createMagic();
    }

    private void createMagic() {
        int area = 5,
                x = random.nextInt(height - area * 2) + area,
                y = random.nextInt(width - area * 2) + area;

        MapCell cell = safeAccess(x, y);
        while (cell == null || cell.getType() == WATER) {
            x = random.nextInt(height - area * 2) + area;
            y = random.nextInt(width - area * 2) + area;

            cell = safeAccess(x, y);
//            System.out.println("type "+cell.type+ " cod "+width+"-"+x+ ":"+height+"-"+y);
        }

        double[] mean = isNeighbour(x, y, 2);
        mean[0] += 1;
        mean[1] += cell.getElevation();
        mean[2] += cell.getHumidity();
//        System.out.println(mean[1]+" / "+mean[0]+" = "+mean[1]/mean[0]+" :elevation");
//        System.out.println(mean[2]+" / "+mean[0]+" = "+mean[2]/mean[0]+" :humidity");
//        System.out.println(mean[3]+" :min elevation");
//        System.out.println(mean[4]+" :min humidity");

        mean[1] = mean[1] / mean[0];//mean ele...
        mean[2] = mean[2] / mean[0];// mean hum..
        isNeighbourSet(x, y, area, mean);
    }

    private double[] isNeighbour(int x, int y, int area) {
        double[] mean = new double[5];
        mean[3] = 1;
        mean[4] = 1;
        if (safeAccess(x, y).getType() == WATER) return mean;
        else if (safeAccess(x, y).getType() == MOUNTAIN) return mean;
        if (area <= 0) {
            mean[0] += 1;
            mean[1] += safeAccess(x, y).getElevation();
            mean[2] += safeAccess(x, y).getHumidity();
            if (mean[3] >= mean[1])
                mean[3] = mean[1];
            if (mean[4] >= mean[2])
                mean[4] = mean[2];
            return mean;
        }

        int[][] nb;
        if ((x & 1) == 1) nb = neighbourodd;
        else nb = neighboureven;

        for (int i = 0; i < 6; ++i) {
            double[] tmp_mean = isNeighbour(x + nb[i][0], y + nb[i][1], area - 1);
            mean[0] += tmp_mean[0];
            mean[1] += tmp_mean[1];
            mean[2] += tmp_mean[2];
            if (mean[3] >= tmp_mean[3])
                mean[3] = tmp_mean[3];
            if (mean[4] >= tmp_mean[4])
                mean[4] = tmp_mean[4];
        }
        return mean;
    }

    private boolean isNeighbourSet(int x, int y, int area, double[] mean) {
        if (safeAccess(x, y).getType() == WATER) return false;
        else if (safeAccess(x, y).getType() == MOUNTAIN) return false;
        if (area <= 0) return true;

        int[][] nb;
        double leftLimit, rightLimit;

        if ((x & 1) == 1) nb = neighbourodd;
        else nb = neighboureven;

        for (int i = 0; i < 6; ++i) {
            if (isNeighbourSet(x + nb[i][0], y + nb[i][1], area - 1, mean)) {
                MapCell cell = safeAccess(x + nb[i][0], y + nb[i][1]);

//                System.out.print(" Elevation old "+cell.elevation);
//                System.out.print(" Humidity old "+cell.humidity);
//                System.out.print(" Type old "+cell.type);

                leftLimit = BiomUtils.round(mean[1] - mean[3], 3);
                rightLimit = BiomUtils.round(mean[1] + mean[3], 3);
                System.out.println("left " + leftLimit + " right " + rightLimit + "\n");

                cell.setElevation(BiomUtils.round(
                        leftLimit + random.nextDouble() * (rightLimit - leftLimit),
                        3)
                );

                leftLimit = BiomUtils.round(mean[2] - mean[4], 3);
                rightLimit = BiomUtils.round(mean[2] + mean[4], 3);

                cell.setHumidity(BiomUtils.round(
                        leftLimit + random.nextDouble() * (rightLimit - leftLimit),
                        3)
                );

                cell.setType(defineBiom(cell.getElevation(), cell.getHumidity()));

//                System.out.print(" | Elevation new "+cell.elevation);
//                System.out.print(" | Humidity new "+cell.humidity);
//                System.out.print(" |Type new "+cell.type+"\n");
            }
        }
        return false;
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

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                cells[i][j] = new MapCell(i, j);
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
            if (cell.getType() != UNDEFINED) continue;

            if (x == width / 2 && y == height / 2) {
                cell.setType(LAND);
            } else cell.setType(randomLandOrWater());

            if (cell.getType() == WATER) continue;
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
                if (mapCell.getType() == cellTypeOld)
                    mapCell.setType(cellTypeNew);
            }
        }
    }

    private int cntCell(CellType cellType) {
        int res = 0;
        for (MapCell[] row : cells) {
            for (MapCell cell : row) {
                if (cell.getType() == cellType) ++res;
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
                if (cells[i][j].getType() == WATER && neighboursLandMap[i][j] >= 4) {
                    cells[i][j].setType(LAND);
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
            if (cell != null && cell.getType() == cellType) ++res;
        }
        return res;
    }

    public void view_Up(int restart) {
        Perlin2D elevation_map = new Perlin2D(seed);
        Perlin2D humidity_map = new Perlin2D(this.seed + 1234);

        for (int i = 0; i < height; ++i) {//y
            for (int j = 0; j < width; ++j) {//x
                MapCell cell = cells[i][j];
                if (cell.getType() == CellType.LAND) {
                    double e = elevation_map.getNoise(i / (double) height, j / (double) width, octaves, persistence) + .5f;
                    //e = terrace(e, 22);
                    //e = exponent(e);
                    e = BiomUtils.round(e, 3);
                    cells[i][j].setElevation(e);

                    double m = humidity_map.getNoise(i / (double) height, j / (double) width, 10, 0.1f) + .5f;
//                    m = exponent(m);
                    m = BiomUtils.round(m, 3);
                    cells[i][j].setHumidity(m);
                    cells[i][j].setType(CellType.defineBiom(e, m));
                    countStat(cells[i][j].getType());
                }
            }
            System.out.println();
        }
        printStat();
    }

    private void printStat() {
        System.out.println();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                System.out.print(cells[i][j].getElevation() + " ");
            }
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                System.out.print(cells[i][j].getHumidity() + " ");
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
        } catch (IndexOutOfBoundsException | NullPointerException e) {
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

