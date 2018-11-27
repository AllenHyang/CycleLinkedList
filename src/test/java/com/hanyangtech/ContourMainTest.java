package com.hanyangtech;

import com.hanyangtech.pathPreTreat.ContourMain;
import com.hanyangtech.pathPreTreat.Point;
import org.junit.Test;

public class ContourMainTest {

    public CycleLinkedList<Point> consturcData(){
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        cycleLinkedList.add(new Point(4,1));

        return cycleLinkedList;
    }

    @Test
    public void testContour(){
        ContourMain.getContour(consturcData());
    }

}