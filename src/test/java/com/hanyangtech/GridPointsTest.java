package com.hanyangtech;

import com.hanyangtech.pathPreTreat.GridPoints;
import com.hanyangtech.pathPreTreat.MapArray;
import com.hanyangtech.pathPreTreat.MapHandler;
import com.hanyangtech.pathPreTreat.Point;
import org.junit.Test;

public class GridPointsTest {

    @Test
    public void initGridsSizeByMaxGridLength() {
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        GridPoints gridPoints=new GridPoints(cycleLinkedList);
        gridPoints.initGridsSizeByMaxGridLength(20);

    }

    @Test
    public void testGridPoints(){
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        GridPoints gridPoints=new GridPoints(cycleLinkedList);
        gridPoints.initGridsSizeByMaxGridLength(20).translateMapPointsByPoint().expandGridSize(2).mappingPointsIntoGridCoor();
        CycleLinkedList<Point> gridP=new CycleLinkedList<>();
        gridP=gridPoints.getGridsCoords();

    }

    @Test
    public void testWithMapArray(){
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        cycleLinkedList.add(new Point(4,1));

        GridPoints gridPoints=new GridPoints(cycleLinkedList);
        gridPoints.initGridsSizeByMaxGridLength(20).translateMapPointsByPoint().expandGridSize(2).mappingPointsIntoGridCoor();

        CycleLinkedList<Point> gridP=new CycleLinkedList<>();
        gridP=gridPoints.getGridsCoords();

        int x=gridPoints.getGridsWidth();
        int y=gridPoints.getGridsLength();
        MapArray mapArray=new MapArray(x,y);
        mapArray.matrix.print(5,0);

        mapArray= MapHandler.interpolationGridWithCycleList(mapArray,gridP);
        mapArray.matrix.print(5,0);


    }


}