package com.hanyangtech.pathPreTreat;


import Jama.Matrix;

import static com.hanyangtech.pathPreTreat.MapArray.FILLED_LEVEL.*;

public class MapArray implements Cloneable{
    @Override
    protected MapArray clone()  {
        MapArray mapArray=null;
        try {
            mapArray= (MapArray) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mapArray.matrix= (Matrix) this.matrix.clone();
        return mapArray;
    }

    public enum FILLED_LEVEL{
        EMPTY(0), LEVEL_PATH(1), LEVEL_OUTSIDE(2), LEVEL_INSIDE(3);
        private int value;
        FILLED_LEVEL(int value){
            this.value=value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
        public int getValue(){
            return value;
        }
    }
    public Matrix matrix;
    private final int maxX;
    private final int maxY;

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }


    public MapArray(int mapX , int mapY){
        matrix=new Matrix(mapY,mapX, EMPTY.value);
        maxX=mapX;
        maxY=mapY;
    }

    public static MapArray newEmptyMapArray(MapArray mapArray){
        return new MapArray(mapArray.getMaxX(),mapArray.getMaxY());
    }

    public void set(int x, int y, FILLED_LEVEL level){
        matrix.set(y,x,level.getValue());
    }


    public int getValueByCoor(int x,int y){
        return (int)matrix.get(y,x);
    }


    public static int[][] mapArrayToIntArray(MapArray mapArray){
        Matrix matrix=mapArray.matrix;
        int xLength=matrix.getColumnDimension();
        int yLength=matrix.getRowDimension();
        int[][] maps=new int[yLength][xLength];
        for(int x=0;x<xLength;x++){
            for(int y=0;y<yLength;y++){
                maps[y][x]=(int)matrix.get(y,x);
            }
        }
        return maps;

    }

    public boolean isNeighborHasLevel(int x,int y,FILLED_LEVEL level){
        int left=x>0?(int)getValueByCoor(x-1,y):999;
        int right=x<maxX-1?(int)getValueByCoor(x+1,y):999;
        int up=y>0?(int)getValueByCoor(x,y-1):999;
        int down=y<maxY-1?(int)getValueByCoor(x,y+1):999;
        if(left==level.value || right==level.value || up==level.value || down==level.value){
            return true;
        }
        else{
            return false;
        }

    }
}
