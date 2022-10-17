package com.mygdx.game.cur_project;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Queue;
import com.mygdx.game.cur_project.utils.MyUtils;
import com.mygdx.game.cur_project.utils.Pair;

import java.util.List;

public class WorldMap implements Screen {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private int width;
    private int height;
    private int[][] scalesMap;
    private int landcnt=0;
    private int trycnt=0;
    private double randomCoef=2;
    TiledMap map;
    TiledMapTileLayer layer;
    public WorldMap(int height, int width){
        this.width=width;
        this.height=height;
        scalesMap=new int[height][width];
        init();
    }
    private void init(){
        /*
        * если земли меньше, чем нужно, карта рандомится заново
        * */

//       while((double)landcnt/(width*height)<0.3){
//            landcnt=0;
//            ++trycnt;
//            System.out.println("try "+trycnt);
//            MyUtils.fillArray(scalesMap,-1);
//            initcell(height/2,width/2);
//            printmap();
//            randomCoef-=0.1;
//        }
        while((double)landcnt/(width*height)<0.2||(double)landcnt/(height*width)>0.5){
            ++trycnt;
            System.out.println("try "+trycnt);
            landcnt=0;
            MyUtils.fillArray(scalesMap,-1);
            initcell();
            removeWater();
        }

        removeWater();
        removeWater();
        removeWater();
        removeWater();
        removeWater();
        removeWater();


        System.out.println(trycnt+"\n"+randomCoef);

        System.out.println();
        printmap();
    }
    private int cnt_neighbours(int x,int y){
        if(scalesMap[x][y]==-1) return 6;
        int res=0;
        res+=safeAccess(x-1,y);
        res+=safeAccess(x+1,y);
        res+=safeAccess(x,y+1);
        res+=safeAccess(x,y-1);
        if((x&1)==1){
            res+=safeAccess(x-1,y+1);
            res+=safeAccess(x+1,y+1);
        } else {
            res+=safeAccess(x-1,y-1);
            res+=safeAccess(x+1,y-1);
        }
        return res;
    }
    private int safeAccess(int x,int y){
        try{
            if(scalesMap[x][y]==-1) return 0;
            return scalesMap[x][y];
        }catch(IndexOutOfBoundsException e){
            return 0;
        }
    }
    private void removeWater(){
        int[][] neighbours_map=new int[height][width];
        for(int i=0;i<height;++i){
            for(int j=0;j<width;++j){
                neighbours_map[i][j]=cnt_neighbours(i,j);
            }
        }
        for(int i=0;i<height;++i){
            for(int j=0;j<width;++j){
                if(neighbours_map[i][j]<3){
                    ++landcnt;
                    scalesMap[i][j]=0;
                }
            }
        }
    }
    private void initcell(){
        Queue<Pair<Integer,Integer>> q=new Queue<>();
        q.addFirst(Pair.pair(height/2,width/2));
        while(q.notEmpty()){
            Pair<Integer,Integer> p=q.removeLast();
            int x=p.first;
            int y=p.second;
            if(x>=height||x<0) continue;
            if(y>=width||y<0) continue;
            if(scalesMap[x][y]==-1) {
                if (x == width / 2 && y == height / 2)
                    scalesMap[x][y] = 0;
                else
                    scalesMap[x][y] = (int) (Math.random() * randomCoef);
            }else continue;
            if(scalesMap[x][y]>0) continue;
            q.addFirst(Pair.pair(x-1,y));
            q.addFirst(Pair.pair(x+1,y));
            q.addFirst(Pair.pair(x,y-1));
            q.addFirst(Pair.pair(x,y+1));
            if((x&1)==1){
                q.addFirst(Pair.pair(x-1,y+1));
                q.addFirst(Pair.pair(x+1,y+1));
            }else {
                q.addFirst(Pair.pair(x-1,y-1));
                q.addFirst(Pair.pair(x+1,y-1));
            }
        }
    }
    private void printmap(){
        for(int i=0;i<height;++i){
            if((i&1)==1) System.out.print(" ");
            for(int j=0;j<width;++j){
                if(scalesMap[i][j]==0){
                    System.out.print(ANSI_GREEN +"@"+ANSI_RESET+" ");
                }else System.out.print(ANSI_BLUE+"@"+ANSI_RESET+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
