package com.hanyangtech.pathSegTreat;

import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.pathPreTreat.Point;

public class PathSegsExecMain {

    public  PositionList start(PositionList positionList,double nearByCoordsTolorance){
        positionList.sparsePoints(nearByCoordsTolorance);
        PathSeg pathSeg=new PathSeg(positionList);
        iterate(new PathSeg[]{pathSeg});
        return getFinedPath();
    }

    public static PathSegsExecMain newInstance(){
        return new PathSegsExecMain();
    }

    private double minDisTolorance=5;
    private int maxAcceptSegsNumber=3;
    public PathSegsExecMain setMinDisTolorance(double minDisTolorance){
        this.minDisTolorance=minDisTolorance;
        return this;
    }
    public PathSegsExecMain setMaxAcceptSegsNumber(int number){
        this.maxAcceptSegsNumber=number;
        return this;
    }
    private CycleLinkedList<PathSeg> pathSegs=new CycleLinkedList<>();

    public  void iterate(PathSeg[] pathSegs){
        for(PathSeg pathSeg:pathSegs){
            Point maxDisPoint=PathSegHandler.findMaxDisPoint(pathSeg);
            double dis=pathSeg.getLine().pointToLineDistance(maxDisPoint);
            if(dis>minDisTolorance || pathSeg.getPoints().size()>maxAcceptSegsNumber){

                PathSeg[] newPathSegs=PathSegHandler.splitSeg(pathSeg,maxDisPoint);
                iterate(newPathSegs);
            }
            else{
                recordPathSeg(pathSeg);
            }
        }
    }

    private void recordPathSeg(PathSeg pathSeg) {
        this.pathSegs.add(pathSeg);
    }

    public CycleLinkedList<PathSeg> getPathSegs(){
        return this.pathSegs;
    }
    public PositionList getFinedPath(){
        PositionList positionList=new PositionList();
        getPathSegs().stream().forEach(pathSeg1 -> {
            positionList.add(pathSeg1.getPoints().getFirstNode().getValue());
            positionList.add(pathSeg1.getPoints().getLastNode().getValue());
        });

        positionList.removeDuplicatePoints();
        return positionList;
    }



}
