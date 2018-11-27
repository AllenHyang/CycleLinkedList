package com.hanyangtech;

import com.hanyangtech.pathFindingAlgorithm.AStar;
import com.hanyangtech.pathFindingAlgorithm.MapInfo;
import com.hanyangtech.pathFindingAlgorithm.Node;
import com.hanyangtech.pathPreTreat.MapArray;
import org.junit.Test;

public class MapArrayTest {

    @Test
    public void mapArrayToIntArray() {
        MapArray mapArray=new MapArray(10,5);
        mapArray.matrix.set(0,2,5);
        int[][] maps=MapArray.mapArrayToIntArray(mapArray);
        MapInfo.printMap(maps);
        mapArray.matrix.print(5,0);
        System.out.println(maps[0][2]);
        System.out.println(maps[0].length);
        System.out.println(maps.length);

    }
    @Test
    public void Astar(){
        MapArray mapArray=new MapArray(10,4);
        int[][] maps=MapArray.mapArrayToIntArray(mapArray);

        MapInfo.printMap(maps);

        System.out.println("=====");
        MapInfo mapInfo=new MapInfo(maps,maps[0].length,maps.length,new Node(1,1),new Node(3,3));
        new AStar().start(mapInfo);
        MapInfo.printMap(maps);



    }

    @Test
    public void testfillIntoMapBetweenTwo(){
        MapArray mapArray=new MapArray(13,24);
        //MapHandler.interpolationGridBetweenTwoCoord(mapArray,new Node(4,13),new Node(1,2));
        mapArray.matrix.print(5,0);
    }
}