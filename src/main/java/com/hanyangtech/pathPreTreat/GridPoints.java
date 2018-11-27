package com.hanyangtech.pathPreTreat;

import com.hanyangtech.CycleLinkedList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.*;

public class GridPoints {
    CycleLinkedList<Point> rawPoints;
    CycleLinkedList<Point> translatePoints;
    private  CycleLinkedList<Point> translateGridCoords;
    private final Double[] minXY;    private final Double[] maxXY;

    public int getGridsWidth() {
        return gridsWidth;
    }

    public int getGridsLength() {
        return gridsLength;
    }

    private int gridsWidth, gridsLength;
    private double singleGridLength;
    public  Point translatePoint;

    public GridPoints(CycleLinkedList<Point> points){
        this.rawPoints=points;
        List<Function<Point, Double>> functions = Arrays.asList(point -> point.x, point -> point.y);
        minXY = functions.stream().map(func -> points.min(func, Double::compareTo)).toArray(Double[]::new);
        maxXY = functions.stream().map(func -> points.max(func, Double::compareTo)).toArray(Double[]::new);
    }

    public GridPoints initGridsSizeByMaxGridLength(int maxGrids){
        double xLength=maxXY[0]-minXY[0];
        double yLength=maxXY[1]-minXY[1];
        double maxLength=max(xLength,yLength);
        this.singleGridLength= (double) (maxLength/(maxGrids));
        this.gridsWidth = (int) ceil (xLength/singleGridLength);
        this.gridsLength = (int)ceil (yLength/singleGridLength);
        return this;
    }

    public GridPoints translateMapPointsByPoint(){
        Point refPoint=getTranslatePoint(minXY,maxXY,singleGridLength);
        translatePoints= translateMapPointsByPoint(rawPoints,refPoint);
        return this;
    }

    public GridPoints expandGridSize(int numOfGrids){
        this.gridsWidth+=(numOfGrids*2);
        this.gridsLength+=(numOfGrids*2);
        Point newRefPoint=new Point();
        newRefPoint.x=numOfGrids*this.singleGridLength+translatePoint.x;
        newRefPoint.y=numOfGrids*this.singleGridLength+translatePoint.y;

        this.translatePoints= translateMapPointsByPoint(rawPoints,newRefPoint);
        return this;
    }

    public void mappingPointsIntoGridCoor(){
        CycleLinkedList<Point> gridsCoor=new CycleLinkedList<>();
        translatePoints.stream().forEach(point ->
                {Point gridPoint=new Point();
                gridPoint.x=(int)round(point.x/singleGridLength);
                gridPoint.y=(int)round(point.y/singleGridLength);
                gridsCoor.add(gridPoint);
                }
                );
        this.translateGridCoords=gridsCoor;

    }

    private CycleLinkedList<Point> translateMapPointsByPoint(CycleLinkedList<Point> points, Point refPoint) {
        CycleLinkedList<Point> cycleLinkedList = new CycleLinkedList<>();
        points.stream().forEach(point -> {
            Point newpoint=new Point();
            newpoint.x = point.x + refPoint.x;
            newpoint.y = point.y + refPoint.y;
            cycleLinkedList.add(newpoint);

        });

        setTranslatePoint(refPoint);

        return cycleLinkedList;
    }

    private Point getTranslatePoint(Double[] minXY, Double[] maxXY, double singleGridLength) {
        return new Point(-minXY[0] , -minXY[1] );
    }

    private void setTranslatePoint(Point point){
        this.translatePoint=point;
    }

    public CycleLinkedList<Point> getGridsCoords() {
        return translateGridCoords;
    }

    public CycleLinkedList<Point> restorePoints(CycleLinkedList<Point> coords){
        CycleLinkedList<Point> newPoints=new CycleLinkedList<>();
        coords.stream().forEach(point -> {
            Point newPoint=new Point();
            newPoint.x=point.x*this.singleGridLength-translatePoint.x;
            newPoint.y=point.y*this.singleGridLength-translatePoint.y;
            newPoints.add(newPoint);

        });
        return newPoints;
    }

}
