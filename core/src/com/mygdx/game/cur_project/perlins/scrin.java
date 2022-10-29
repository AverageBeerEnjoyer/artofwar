package com.mygdx.game.cur_project.perlins;

import com.badlogic.gdx.graphics.Color;

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

    private void convertSym(float sym){
        if(sym > 0.55){
            System.out.print("\u001B[32m"+"@"+"\u001B[0m"+" ");
        } else if(sym > 0.52){
            System.out.print("\u001b[33m"+"@"+"\u001B[0m"+" ");
        }else {
            System.out.print("\u001b[34m" + "@" + "\u001B[0m" + " ");
        }
    }
    public void view(){
//        ImprovedNoise f = new ImprovedNoise();
//        float value = perlin.getNoise(x / 100f, y / 100f, 8, 0.2f) + .5f;
//        int[] a = f.getP();
//        System.out.println(a);
        float[][] map = new float[height][width];
        Perlin2D a = new Perlin2D(777);
        for(int i =0;i<height;++i){
            for(int j =0;j<width;++j){
//                float b = a.getNoise(i / 100f, j / 100f)+ .5f;
                float b = a.getNoiseUpdate(i / 100f, j / 100f, 8, 0.2f) + .5f;
                map[i][j] = b;
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        for(int i =0;i<height;++i){
            for(int j =0;j<width;++j){
                convertSym(map[i][j]);
            }
            System.out.println();
        }

    }

    //instrument
    private static  double round(double value, int places){
        if(places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //create color
    private void convertSymD(int i, int j, double e, double m){
        sr += e;
        sr2 += m;
        map_color[i][j] = biomeEXE(e,m);
        System.out.print(biomeConsole(e, m)+"@"+"\u001B[0m"+" ");
    }
    private String biomeConsole(double e, double m){
        if(e < 0.1) {
            c_water++;
            return cols.BLUE;
        }
        if(e < 0.2) return cols.YELLOW;

        if (e > 0.8) {
            if (m < 0.1) return cols.BLACK;
            if (m < 0.2) return cols.BLACK;
            if (m < 0.5) return cols.BLACK;
            return cols.BLACK;
        }

        if (e > 0.6) {
            if (m < 0.33) return cols.RED;
            if (m < 0.66) return cols.RED;
            return cols.RED;
        }

        if (e > 0.3) {
            if (m < 0.16) return cols.GREEN;
            if (m < 0.50) return cols.GREEN;
            if (m < 0.83) return cols.GREEN;
            return cols.GREEN;
        }

        if (m < 0.16) return cols.GREEN;
        if (m < 0.33) return cols.GREEN;
        if (m < 0.66) return cols.GREEN;
        return cols.GREEN;
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

    //create graph
    private  double exponent(double e, double exp){
        return Math.pow(Math.abs(e),exp);
    }
    private double terrace(double e, double n){
        return Math.round(e*n)/n;
    }
//    private double ridgenoise(double nx, double ny) {//Остроконечный шум
//        return 2 * (0.5 - Math.abs(0.5 - elevation_map.getNoiseStandart((float) nx, (float) ny)));
//    }

    //create map
    public Color[][] view_Up(int restart){
        this.restart = restart;
//        double a1 = Math.random()*1000;
//        double a2 = Math.random()*2000;
//        System.out.println(a1);
//        System.out.println(a2);
        Perlin2D elevation_map = new Perlin2D(this.seed);
        Perlin2D humidity_map = new Perlin2D(this.seed+1234);
        double[][] elevation = new double[height][width];
        double[][] humidity = new double[height][width];
        map_color = new Color[height][width];

        for(int i =0;i<height;++i){//y
            for(int j =0;j<width;++j){//x
                //высоты
                double nx =(double) j/width, ny = (double)i/height;
//                double e =      (1.00 * humidity_map.getNoiseStandart((float)(nx*1),(float)(ny*1))
//                        + 0.50 * humidity_map.getNoiseStandart((float)(nx*2),(float)(ny*2))
//                        + 0.25 * humidity_map.getNoiseStandart((float)(nx*4),(float)(ny*4))
//                        + 0.13 * humidity_map.getNoiseStandart((float)(nx*8),(float)(ny*8))
//                        + 0.06 * humidity_map.getNoiseStandart((float)(nx*16),(float)(ny*16))
//                        + 0.03 * humidity_map.getNoiseStandart((float)(nx*32),(float)(ny*32)));
//                e /= (1.00+0.50+0.25+0.13+0.06+0.03);
//                e = Math.pow(Math.abs(e), 2);
//                elevation[i][j] = round(e,3);
//
//                //влажность
//                 double m =      (1.00 * humidity_map.getNoiseStandart((float)(nx*1),(float)(ny*1))
//                        + 0.75 * humidity_map.getNoiseStandart((float)(nx*2),(float)(ny*2))
//                        + 0.33 * humidity_map.getNoiseStandart((float)(nx*4),(float)(ny*4))
//                        + 0.33 * humidity_map.getNoiseStandart((float)(nx*8),(float)(ny*8))
//                        + 0.33 * humidity_map.getNoiseStandart((float)(nx*16),(float)(ny*16))
//                        + 0.50 * humidity_map.getNoiseStandart((float)(nx*32),(float)(ny*32)));
//                 m /= (1.00 + 0.75 + (0.33*3) +0.50 );
////                humidity[i][j] = Math.pow(Math.abs(e), 2.7);
//                humidity[i][j] = round(Math.abs(e),3);
                double e = elevation_map.getNoiseUpdate(i / (float)height, j / (float)width,8,0.125f)+ .5f;
                e = terrace(e,22);
                e = exponent(e, 2.3);
                e = round(e,3);
                elevation[i][j]= e;

                double m = humidity_map.getNoiseUpdate(i / (float)height, j / (float)width,10,0.1f)+ .5f;
                m = terrace(m,10);
                m = exponent(m, 2);
                m = round(m, 3);
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
