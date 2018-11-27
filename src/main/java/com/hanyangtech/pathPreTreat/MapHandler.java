package com.hanyangtech.pathPreTreat;

import Jama.Matrix;
import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.pathFindingAlgorithm.AStar;
import com.hanyangtech.pathFindingAlgorithm.MapInfo;
import com.hanyangtech.pathFindingAlgorithm.Node;

import java.util.ArrayList;
import java.util.ListIterator;

import static com.hanyangtech.pathPreTreat.MapArray.FILLED_LEVEL.*;

public class MapHandler {

    private static void fillGridByCoord(MapArray mapArray, int x, int y, MapArray.FILLED_LEVEL level){
        mapArray.set(x,y,level);
    }

    private static void interpolationGridBetweenTwoCoord(MapArray mapArray,Node nodeOne, Node nodeTwo){
        //MapArray newMapArray=mapArray.clone();
        int[][] maps=MapArray.mapArrayToIntArray(mapArray);
        MapInfo mapInfo=new MapInfo(maps,maps[0].length,maps.length,nodeOne,nodeTwo);
        ArrayList<Node> arrayList= new AStar().start(mapInfo).getNodesList();
        for(Node node:arrayList){
            fillGridByCoord(mapArray,node.coord.x,node.coord.y, LEVEL_PATH);
        }
        //return newMapArray;

    }

    public static MapArray interpolationGridWithCycleList(MapArray mapArray, CycleLinkedList<Point> points){
        MapArray newMapArray=mapArray.clone();

        Point curPoint=points.getFirstNode().getValue();
        Point prePoint=points.getLastNode().getValue();
        interpolationGridBetweenTwoCoord(newMapArray,new Node(curPoint.intX(),curPoint.intY()),new Node(prePoint.intX(),prePoint.intY()));

        ListIterator<Point> it=points.listIterator(1);
        while (it.hasNext()){
            curPoint=it.next();
            prePoint=it.previous();
            interpolationGridBetweenTwoCoord(newMapArray,new Node(curPoint.intX(),curPoint.intY()),new Node(prePoint.intX(),prePoint.intY()));

        }
        return newMapArray;
    }

    public static MapArray fillGridWithPointList(MapArray mapArray,CycleLinkedList<Point> gridP, MapArray.FILLED_LEVEL level){
        MapArray newMapArray=mapArray.clone();

        gridP.stream().forEach(point -> fillGridByCoord(newMapArray,point.intX(),point.intY(),level));
        return newMapArray;
    }

    public static MapArray fillGridOutsideOfPath(MapArray mapArray){

        MapArray newMapArray=mapArray.clone();
        Matrix matrix=newMapArray.matrix;
        for(int i=0;i<newMapArray.getMaxX()-1;i++){
            fillGridByCoord(newMapArray,i,0,LEVEL_OUTSIDE);
            fillGridByCoord(newMapArray,i,newMapArray.getMaxY()-1,LEVEL_OUTSIDE);
        }
        for(int i=0;i<newMapArray.getMaxY()-1;i++){
            fillGridByCoord(newMapArray,0,i,LEVEL_OUTSIDE);
            fillGridByCoord(newMapArray,newMapArray.getMaxX()-1,i,LEVEL_OUTSIDE);
        }
        for(int x=0;x<newMapArray.getMaxX();x++) {
            for (int y = 0; y < newMapArray.getMaxY(); y++) {
                int value=newMapArray.getValueByCoor(x,y);
                if(value == EMPTY.getValue() && newMapArray.isNeighborHasLevel(x,y,LEVEL_OUTSIDE)){
                    fillGridByCoord(newMapArray,x,y,LEVEL_OUTSIDE);
                }
                int otherSideY=newMapArray.getMaxY()-y-1;
                if(newMapArray.getValueByCoor(x,otherSideY)==EMPTY.getValue() && newMapArray.isNeighborHasLevel(x,otherSideY,LEVEL_OUTSIDE)){
                    fillGridByCoord(newMapArray,x,otherSideY,LEVEL_OUTSIDE);
                }
            }
        }
        return newMapArray;
    }

    public static MapArray fillInnerPathWithInsideLevel(MapArray mapArray){
        MapArray newMapArray=mapArray.clone();
        for(int x=1;x<mapArray.getMaxX();x++){
            for(int y=1;y<mapArray.getMaxY();y++){
                if(mapArray.getValueByCoor(x,y)==LEVEL_PATH.getValue() && mapArray.isNeighborHasLevel(x,y,LEVEL_OUTSIDE)){
                    fillGridByCoord(newMapArray,x,y,LEVEL_INSIDE);
                }
            }
        }
        return newMapArray;
    }

    public static MapArray makeInsideLevelToOutsideLevel(MapArray mapArray){
        MapArray newMapArray=mapArray.clone();
        for(int x=0;x<newMapArray.getMaxX();x++){
            for(int y=0;y<newMapArray.getMaxY();y++){
                if(newMapArray.getValueByCoor(x,y)==LEVEL_INSIDE.getValue()){
                    fillGridByCoord(newMapArray,x,y,LEVEL_OUTSIDE);
                }
            }
        }
        return newMapArray;
    }

    public static MapArray getInnerSidePath(MapArray mapArray){
        MapArray mapEdge=MapArray.newEmptyMapArray(mapArray);
        for(int x=0;x<mapArray.getMaxX();x++){
            for(int y=0;y<mapArray.getMaxY();y++) {
                if(mapArray.getValueByCoor(x,y)==LEVEL_OUTSIDE.getValue() && (mapArray.isNeighborHasLevel(x,y,LEVEL_PATH)||mapArray.isNeighborHasLevel(x,y,EMPTY))){
                    fillGridByCoord(mapEdge,x,y,LEVEL_PATH);
                }
            }
        }
        return mapEdge;
    }

    public static MapArray findTheEdge(GridPoints gridPoints){
        int x=gridPoints.getGridsWidth();
        int y=gridPoints.getGridsLength();
        MapArray mapArray=new MapArray(x,y);
        CycleLinkedList<Point> dataSource=gridPoints.getGridsCoords();
        mapArray=MapHandler.interpolationGridWithCycleList(mapArray,dataSource);
        mapArray=MapHandler.fillGridOutsideOfPath(mapArray);
        mapArray=MapHandler.fillInnerPathWithInsideLevel(mapArray);
        mapArray=MapHandler.makeInsideLevelToOutsideLevel(mapArray);
        mapArray=MapHandler.getInnerSidePath(mapArray);
        return mapArray;

    }

}
