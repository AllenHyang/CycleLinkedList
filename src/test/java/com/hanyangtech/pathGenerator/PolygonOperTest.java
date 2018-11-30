package com.hanyangtech.pathGenerator;

import com.hanyangtech.pathPreTreat.Point;
import com.hanyangtech.pathSegTreat.PositionList;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.hanyangtech.pathGenerator.PolygonOper.sortHashMap;

public class PolygonOperTest {
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

    }
    @Test
    public void findInterSecPolygon() {
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


        Polygon polygon=new Polygon(positionList);

        System.out.println(polygon);

        HashSet<Polygon.EndPoint> m = PolygonOper.findInterSecWithPolygon(polygon, 0.5);
        System.out.println(m);
    }
    @Test
    public void testgetAllCornerInterSecPointsInPolygon(){
        List<Set> data=PolygonOper.getAllCornerInterSecPointsInPolygon(polygon);
        data.stream().forEach(item->
        {System.out.println(item.size());item.stream().forEach(System.out::println);});
    }
    @Test
    public void testcheckSecPointsIsSameSide(){
        List<Set<Polygon.EndPoint>> data=PolygonOper.getAllCornerInterSecPointsInPolygon(polygon);
        List<Set<Polygon.EndPoint>> secPointsIsSameSide =PolygonOper.checkSecPointsIsSameSide(data,polygon);
        System.out.println(secPointsIsSameSide);
        System.out.println(secPointsIsSameSide.size());
    }
    @Test
    public void testfiltOutNotDevidedPointList(){
        List<Set<Polygon.EndPoint>> data=PolygonOper.getAllCornerInterSecPointsInPolygon(polygon);
        List secPointsIsSameSide =PolygonOper.checkSecPointsIsSameSide(data,polygon);


        List resutl=PolygonOper.filtOutNotDevidedPointList(secPointsIsSameSide);
        System.out.println(resutl);
        System.out.println(resutl.size());
        }

    @Test
    public void testcheckPolygonCanDivided(){
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

        Polygon polygon=new Polygon(positionList);

        PolygonOper.getFinalValidDevidedPointList(polygon).stream().forEach(System.out::println);

    }

    @Test
    public void testSort(){
        HashMap<Point, Boolean> m=new HashMap<>();
        m.put(new Point(1,2),true);
        m.put(new Point(21,2),true);
        m.put(new Point(3,2),true);
        m.put(new Point(42,2),true);
        m.put(new Point(5,2),true);
        m.put(new Point(61,2),true);
        System.out.println(m);

        LinkedHashMap<Point,Boolean> s=sortHashMap(m, Comparator.comparing(Point::getX));
        System.out.println(s);
    }
}