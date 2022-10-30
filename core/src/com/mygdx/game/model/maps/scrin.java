package com.mygdx.game.model.maps;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.view.utils.BiomUtils;
import com.mygdx.game.view.utils.ConsoleColors;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class scrin {
    private int width = 80;
    private int height = 20;
    private final double seed;
    private Color[][] map_color;
    private ConsoleColors cols = new ConsoleColors();
    private int c_water = 0;
    private int max_procent_water = 50;
    private int restart = 0;
    private double sr = 0, sr2 = 0;

    public scrin(int width, int height, int seed){
        this.width = width;
        this.height = height;
        if(seed == -1){
            this.seed = Math.random()*2000;
        }else
            this.seed = seed;
    }

    //instrument


    //create color
    private void convertSymD(int i, int j, double e, double m){
        sr += e;
        sr2 += m;
        map_color[i][j] = biomeEXE(e,m);
    }
    private Color biomeEXE(double e, double m){
        if(e < 0.1) {
            c_water++;
            return Color.BLUE;
        }
        if(e < 0.2) return Color.YELLOW;

        if (e > 0.8) {
            if (m < 0.1) return Color.BLACK;
            if (m < 0.2) return Color.BLACK;
            if (m < 0.5) return Color.BLACK;
            return Color.BLACK;
        }

        if (e > 0.6) {
            if (m < 0.33) return Color.RED;
            if (m < 0.66) return Color.RED;
            return Color.RED;
        }

        if (e > 0.3) {
            if (m < 0.16) return Color.GREEN;
            if (m < 0.50) return Color.GREEN;
            if (m < 0.83) return Color.GREEN;
            return Color.GREEN;
        }

        if (m < 0.16) return Color.GREEN;
        if (m < 0.33) return Color.GREEN;
        if (m < 0.66) return Color.GREEN;
        return Color.GREEN;
    }

    //create graphic
    private  double exponent(double e, double exp){
        return Math.pow(Math.abs(e),exp);
    }
    private double terrace(double e, double n){
        return Math.round(e*n)/n;
    }
//    private double ridgenoise(double nx, double ny) {//������������� ���
//        return 2 * (0.5 - Math.abs(0.5 - elevation_map.getNoiseStandart((double) nx, (double) ny)));
//    }

    //create map
    public Color[][] view_Up(int restart){
        this.restart = restart;
        Perlin2D elevation_map = new Perlin2D(this.seed);
        Perlin2D humidity_map = new Perlin2D(this.seed+1234);
        double[][] elevation = new double[height][width];
        double[][] humidity = new double[height][width];
        map_color = new Color[height][width];

        for(int i =0;i<height;++i){//y
            for(int j =0;j<width;++j){//x
                double e = elevation_map.getNoise(i / (double)height, j / (double)width,8,0.125f)+ .5f;
                e = terrace(e,22);
                e = exponent(e, 2.3);
                e = BiomUtils.round(e,3);
                elevation[i][j]= e;

                double m = humidity_map.getNoise(i / (double)height, j / (double)width,10,0.1f)+ .5f;
                m = terrace(m,10);
                m = exponent(m, 2);
                m = BiomUtils.round(m, 3);
                humidity[i][j] = m;
                convertSymD(i,j,e,m);
            }
            System.out.println();
        }
//        System.out.println();
//        for(int i=0;i<height;++i){
//            for (int j=0; j<width;++j){
//                System.out.print(elevation[i][j]+" ");
//            }
//            System.out.println();
//        }
//
//        System.out.println();
//        for(int i=0;i<height;++i){
//            for (int j=0; j<width;++j){
//                System.out.print(humidity[i][j]+" ");
//            }
//            System.out.println();
//        }
        System.out.println("srednee: \n"+sr/(width*height));
        System.out.println(sr2/(width*height));
        if(this.restart < 0){
            if(c_water >(max_procent_water * (height*width))/100  ){
                view_Up(this.restart+1);
            }
        }
        return this.map_color;
    }
}
