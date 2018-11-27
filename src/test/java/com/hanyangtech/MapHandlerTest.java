package com.hanyangtech;

import com.hanyangtech.pathPreTreat.GridPoints;
import com.hanyangtech.pathPreTreat.MapArray;
import com.hanyangtech.pathPreTreat.MapHandler;
import com.hanyangtech.pathPreTreat.Point;
import org.junit.Test;

public class MapHandlerTest {

    public GridPoints consturcData(){
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        cycleLinkedList.add(new Point(4,1));

        GridPoints gridPoints=new GridPoints(cycleLinkedList);
        return gridPoints;
    }

    @Test
    public void fillGridOutsideOfPath() {
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        cycleLinkedList.add(new Point(4,1));

        GridPoints gridPoints=new GridPoints(cycleLinkedList);
        gridPoints.initGridsSizeByMaxGridLength(20).translateMapPointsByPoint().expandGridSize(2).mappingPointsIntoGridCoor();;

        CycleLinkedList<Point> gridP=new CycleLinkedList<>();
        gridP=gridPoints.getGridsCoords();

        int x=gridPoints.getGridsWidth();
        int y=gridPoints.getGridsLength();
        MapArray mapArray=null;

        mapArray=new MapArray(x,y);
        mapArray.matrix.print(5,0);

        mapArray= MapHandler.interpolationGridWithCycleList(mapArray,gridP);
        mapArray.matrix.print(5,0);

        mapArray=MapHandler.fillGridOutsideOfPath(mapArray);
        mapArray.matrix.print(5,0);

    }

    @Test
    public void fillInnerPathWithInsideLevel(){
        GridPoints gridPoints=consturcData();
        gridPoints.initGridsSizeByMaxGridLength(20).translateMapPointsByPoint().expandGridSize(2).mappingPointsIntoGridCoor();;
        CycleLinkedList<Point> gridP=gridPoints.getGridsCoords();

        int x=gridPoints.getGridsWidth();
        int y=gridPoints.getGridsLength();
        MapArray mapArray=new MapArray(x,y);
        mapArray=MapHandler.interpolationGridWithCycleList(mapArray,gridP);
        mapArray=MapHandler.fillGridOutsideOfPath(mapArray);
        mapArray=MapHandler.fillInnerPathWithInsideLevel(mapArray);
        mapArray.matrix.print(5,0);

    }


    @Test
    public void makeInsideLevelToOutsideLevel(){
        GridPoints gridPoints=consturcData();
        gridPoints.initGridsSizeByMaxGridLength(20).translateMapPointsByPoint().expandGridSize(2).mappingPointsIntoGridCoor();
        CycleLinkedList<Point> gridP=gridPoints.getGridsCoords();

        int x=gridPoints.getGridsWidth();
        int y=gridPoints.getGridsLength();
        MapArray mapArray=new MapArray(x,y);
        mapArray=MapHandler.interpolationGridWithCycleList(mapArray,gridP);
        mapArray=MapHandler.fillGridOutsideOfPath(mapArray);
        mapArray=MapHandler.fillInnerPathWithInsideLevel(mapArray);
        mapArray=MapHandler.makeInsideLevelToOutsideLevel(mapArray);
        mapArray.matrix.print(5,0);

    }

    @Test
    public void findTheEdge(){
        GridPoints gridPoints=consturcData();
        gridPoints.initGridsSizeByMaxGridLength(20).translateMapPointsByPoint().expandGridSize(2).mappingPointsIntoGridCoor();

        MapArray mapArray=MapHandler.findTheEdge(gridPoints);
        mapArray.matrix.print(5,0);

    }
}