package com.hanyangtech.pathSegTreat;

import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.pathPreTreat.ContourMain;
import com.hanyangtech.pathPreTreat.Point;
import org.junit.Test;

public class PathSegsExecMainTest {
    @Test
    public void main(){
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1.2,1.2));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        cycleLinkedList.add(new Point(4,1));
        cycleLinkedList=ContourMain.getContour(cycleLinkedList);
        System.out.println(cycleLinkedList);
        System.out.println(cycleLinkedList.size());  //构造数据

        PositionList positionList= new PositionList(cycleLinkedList);
        //positionList.sparsePoints(0.5);  //稀疏化

        System.out.println(positionList);
        System.out.println(positionList.size());


        PositionList positionList1=PathSegsExecMain.newInstance().setMaxAcceptSegsNumber(5)
                .setMinDisTolorance(0.5).start(positionList,0.5);
        System.out.println(positionList1);
        System.out.println(positionList1.size());
    }


}