package com.hanyangtech.pathGenerator;

import com.hanyangtech.Node;
import com.hanyangtech.pathPreTreat.Point;
import com.hanyangtech.pathSegTreat.PositionList;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PolygonTest {
    @Test
    public void polygon(){
        PositionList positionList=new PositionList();
        positionList.add(new Point(1,4));
        positionList.add(new Point(2,4));
        positionList.add(new Point(3,4));
        positionList.add(new Point(4,4));
        positionList.add(new Point(7,4));
        positionList.add(new Point(8,3));
        positionList.add(new Point(5,3));
        System.out.println(positionList);
        Polygon polygon=new Polygon(positionList);
        System.out.println(polygon);



    }

    @Test
    public void getPre(){
        PositionList positionList=new PositionList();
        positionList.add(new Point(0,0));
        positionList.add(new Point(2,0));
        positionList.add(new Point(2,2));

        positionList.add(new Point(1.5,2));
        positionList.add(new Point(1.5,1));

        positionList.add(new Point(1,1));
        positionList.add(new Point(0.5,2));
        positionList.add(new Point(0,2));

        Polygon polygon=new Polygon(positionList);
        //Point point=polygon.getPreviousYNotEqualPoint(0);
        //System.out.println(point);
       System.out.println(polygon.get(1));
        Point point6=polygon.getPreviousYNotEqualPoint(1,1.75);
        System.out.println(point6);

        Point point5=polygon.getNextYNotEqualPoint(1,1.75);
        System.out.println(point5);

    }

}