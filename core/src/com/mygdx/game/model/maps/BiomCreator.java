package com.mygdx.game.model.maps;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.view.utils.BiomUtils;
import com.mygdx.game.view.utils.ConsoleColors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class BiomCreator {
    private final int width;
    private final int height;
    private final long seed;
    private final Map map;
    private final java.util.Map<String,Integer> statInfo = new HashMap<>();

    private double degree = 1.5;
    private int octaves = 2;
    private double persistence = 0.1;

    public BiomCreator(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        if (seed == -1) {
            this.seed = (long) (Math.random() * 2000);
        } else {
            this.seed = seed;
        }
        this.map = Map.createMap(height, width, 0, this.seed);
    }

    public void setDegree(double e){
        this.degree = e;
    }
    public void setOctaves(int e){
        this.octaves = e;
    }
    public void setPersistence(double e){
        this.persistence = e;
    }

    public long getSeed(){
        return seed;
    }

    public java.util.Map<String,Integer> getStatInfo(){
        return statInfo;
    }

    private void countStat(CellType t) {
        String key = String.valueOf(t);

        if(statInfo.get(key) == null){
            statInfo.put(key, 1);
        }else{
            statInfo.put(key, statInfo.get(key)+1);
        }
    }

    private CellType biomeEXE(double e, double m) {
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

    private double exponent(double e) {
         return Math.pow(Math.abs(e), degree);
    }
    private double growUp(double e){
        double res = e*1.3;
        if(res>1) return 1;
        else return res;
    }
    private double terrace(double e, double n) {
        return Math.round(e * n) / n;
    }
//    private double ridgenoise(double nx, double ny) {//������������� ���
//        return 2 * (0.5 - Math.abs(0.5 - elevation_map.getNoiseStandart((double) nx, (double) ny)));
//    }

    //create map
    public Map view_Up(int restart) {
        Perlin2D elevation_map = new Perlin2D(this.seed);
        Perlin2D humidity_map = new Perlin2D(this.seed + 1234);

        for (int i = 0; i < height; ++i) {//y
            for (int j = 0; j < width; ++j) {//x
                MapCell cell = map.cells[i][j];
                if (cell.type == CellType.LAND) {
                    double e = elevation_map.getNoise(i / (double) height, j / (double) width, octaves, persistence) + .5f;
                     //e = terrace(e, 22);
                    //e = exponent(e);
                    e = BiomUtils.round(e, 3);
                    map.cells[i][j].height = e;

                    double m = humidity_map.getNoise(i / (double) height, j / (double) width, 10, 0.1f) + .5f;
//                    m = exponent(m);
                    m = BiomUtils.round(m, 3);
                    map.cells[i][j].humidity = m;
                    map.cells[i][j].type = biomeEXE(e, m);
                    countStat(map.cells[i][j].type);
                }
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                System.out.print(map.cells[i][j].height + " ");
            }
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                System.out.print(map.cells[i][j].humidity + " ");
            }
            System.out.println();
        }
//        System.out.println("srednee: \n" + countLand / (width * height));
//        System.out.println(countWater / (width * height));
        return map;
    }
}
