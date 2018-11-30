package com.hanyangtech.pathGenerator;

import com.hanyangtech.pathPreTreat.Point;
import com.hanyangtech.pathSegTreat.PositionList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PolygonGenTest {
    public List list;
    public Polygon polygon;

    @Before
    public void contrucData(){
        PositionList positionList=new PositionList();
        positionList.add(new Point(0,0));
        positionList.add(new Point(0.5,0.5));
        positionList.add(new Point(1.3,0));
        positionList.add(new Point(1.5,0.5));

        positionList.add(new Point(2,0));
        positionList.add(new Point(2,2));

        positionList.add(new Point(1.5,2));
        positionList.add(new Point(1.5,1));

        positionList.add(new Point(1,1));
        positionList.add(new Point(1,2));
        positionList.add(new Point(0,2));


        polygon=new Polygon(positionList);
        list=PolygonOper.getFinalValidDevidedPointList(polygon);

    }


    @Test
    public void sortIntoPolygon() {

        PolygonGen.sortIntoPolygon(polygon,list);

    }

    @Test
    public void testgenPolygon()
    {
        PolygonGen.genPolygon(polygon,list);
    }
}