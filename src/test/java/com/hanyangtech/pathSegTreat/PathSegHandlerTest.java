package com.hanyangtech.pathSegTreat;

import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.pathPreTreat.ContourMain;
import com.hanyangtech.pathPreTreat.Point;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathSegHandlerTest {

    @Test
    public void splitSeg() {
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        cycleLinkedList.add(new Point(4,1));
        cycleLinkedList= ContourMain.getContour(cycleLinkedList);
        System.out.println(cycleLinkedList);
        System.out.println(cycleLinkedList.size());

        PositionList positionList= new PositionList(cycleLinkedList);
        positionList.sparsePoints(0.5);
        System.out.println(positionList);
        System.out.println(positionList.size());

        Point splitPoint=positionList.get(5);
        PathSeg pathSeg=new PathSeg(positionList);
        PathSeg[] pathSegs=PathSegHandler.splitSeg(pathSeg,splitPoint);
        System.out.println(pathSegs);

    }
}