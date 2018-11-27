package com.hanyangtech.pathPreTreat;

import com.hanyangtech.CycleLinkedList;

public class ContourMain {
    public static CycleLinkedList<Point> getContour(CycleLinkedList<Point> points){
        GridPoints gridPoints=new GridPoints(points);
        gridPoints.initGridsSizeByMaxGridLength(20).translateMapPointsByPoint().expandGridSize(2).mappingPointsIntoGridCoor();

        MapArray mapArray=MapHandler.findTheEdge(gridPoints);

        CycleLinkedList<Point> mappoints=PointsSorting.sorting(mapArray.matrix);

        CycleLinkedList<Point> points1=gridPoints.restorePoints(mappoints);

        return points1;
    }


}
