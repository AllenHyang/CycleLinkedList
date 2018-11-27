package com.hanyangtech.pathSegTreat;

import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.pathPreTreat.Point;


public class PathSegHandler {

    public static Point findMaxDisPoint(PathSeg pathSeg){
        Line line=pathSeg.getLine();
        Point maxDisPoint=pathSeg.getPoints().stream().reduce(
                        (p1,p2) -> line.pointToLineDistance(p1)>line.pointToLineDistance(p2)? p1:p2 ).get();
        return maxDisPoint;
    }

    public static PathSeg[] splitSeg(PathSeg pathSeg, Point maxDisPoint) {
        PositionList one=new PositionList();
        PositionList two=new PositionList();
        PositionList origin=pathSeg.getPoints();
        CycleLinkedList<Point>.Node<Point> curNode=origin.getNode(0);

        do{
            one.add(curNode.getValue());
            curNode=curNode.getNext();
        }while (!curNode.getValue().equals(maxDisPoint));

         curNode=origin.getNode(maxDisPoint);
        do{
            two.add(curNode.getValue());
            curNode=curNode.getNext();
        }while (curNode.getValue()!=null);

        return new PathSeg[]{new PathSeg(one),new PathSeg(two)};

    }
}
