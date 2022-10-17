package com.mygdx.game.cur_project.utils;

import java.util.Arrays;

public class MyUtils {
    public  static void fillArray(int[][] a,int n){
        for(int row[]:a){
            Arrays.fill(row,-1);
        }
    }
}
