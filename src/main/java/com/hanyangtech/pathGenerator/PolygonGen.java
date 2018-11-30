package com.hanyangtech.pathGenerator;

import com.hanyangtech.Node;
import com.hanyangtech.pathPreTreat.Point;
import com.hanyangtech.pathSegTreat.PositionList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hanyangtech.LocalMath.compareClosetPoint;
import static com.hanyangtech.LocalMath.manhattanDistance;
import static java.lang.Math.abs;

public class PolygonGen {
    public static void genPolygon(Polygon polygon, List<List<Polygon.EndPoint>> dividedPointsList){
        PositionList afterInsertPoints=sortIntoPolygon(polygon,dividedPointsList);
        findUpperPolygon(afterInsertPoints,dividedPointsList);
        findLowerPolygon(afterInsertPoints,dividedPointsList);
        /*TODO
        分别将多边形分割成上中下三块
        然后对上下两块的顶点遍历，找到对应的多边形
        */
        Set<Polygon.EndPoint> points=dividedPointsList.stream().flatMap(x->x.stream()).collect(Collectors.toSet());

        Map<Double,List> sortedArrayList=new HashMap<>();
        dividedPointsList.stream().forEach(item-> {
            List<Point> list=item.stream().sorted(Comparator.comparing(Point::getX)).collect(Collectors.toList());
                    sortedArrayList.put(list.get(0).getY(),list);
            }
            );

        PositionList positionList=new PositionList();
        Iterator<Point> it=afterInsertPoints.iterator();
        Point firstPoint=it.next();
        positionList.add(firstPoint);
        while (it.hasNext()){
            Point curPoint=it.next();
            positionList.add(curPoint);

        }

    }
    public static PositionList findLowerPolygon(PositionList positionList, List<List<Polygon.EndPoint>> dividedPointsList) {
        Set<Polygon.EndPoint> points=dividedPointsList.stream().flatMap(x->x.stream()).collect(Collectors.toSet());
        double minY=points.stream().map(p->p.getY()).min(Comparator.comparing(Double::doubleValue)).get();
        PositionList lowerList=new PositionList();
        Iterator<Point> it=positionList.iterator();
        while (it.hasNext()){
            Point curp=it.next();
            if(curp.getY()<=minY){
                lowerList.add(curp);
            }
        }

        return lowerList;


    }

        /**
         * 找到上方多边形
         * @param positionList
         * @param dividedPointsList
         * @return
         */
    public static PositionList findUpperPolygon(PositionList positionList, List<List<Polygon.EndPoint>> dividedPointsList){
        Set<Polygon.EndPoint> points=dividedPointsList.stream().flatMap(x->x.stream()).collect(Collectors.toSet());
        double maxY=points.stream().map(p->p.getY()).max(Comparator.comparing(Double::doubleValue)).get();
        //Point minPoint=points.stream().filter(p->p.getY()==maxY).min(Comparator.comparing(Point::getX)).get();

        PositionList upperList=new PositionList();
        Iterator<Point> it=positionList.iterator();
        while (it.hasNext()){
            Point curp=it.next();
            if(curp.getY()>=maxY){
                upperList.add(curp);
            }
        }

        return upperList;
    }

    /**
     * 找到中间的四边形
     * @param polygon
     * @param dividedPointsList
     * @return
     */
    public static Polygon findCenterPolygon(Polygon polygon,List<List<Polygon.EndPoint>> dividedPointsList){
        PositionList positionList=new PositionList();
        if(dividedPointsList.size() == 2){
            Point[] points=findCornerPoints(dividedPointsList);
            for(Point point:points){
                positionList.add(point);
            }
        }
        return new Polygon(positionList);
    }

    /**
     * 计算四边形，四个角的坐标，顺时针，左下角开始计数
     * @param dividedPointsList
     * @return
     */
    public static Point[] findCornerPoints(List<List<Polygon.EndPoint>> dividedPointsList){
        if(dividedPointsList.size()==2) {
            double minXone = dividedPointsList.get(0).stream().map(x -> x.getX()).min(Comparator.comparing(Double::doubleValue)).get();
            double maxXone = dividedPointsList.get(0).stream().map(x -> x.getX()).max(Comparator.comparing(Double::doubleValue)).get();
            double yone = dividedPointsList.get(0).get(0).getY();
            double minXtwo = dividedPointsList.get(1).stream().map(x -> x.getX()).min(Comparator.comparing(Double::doubleValue)).get();
            double maxXtwo = dividedPointsList.get(1).stream().map(x -> x.getX()).max(Comparator.comparing(Double::doubleValue)).get();
            double ytwo = dividedPointsList.get(1).get(0).getY();

            Point[] points=new Point[4];
            points[0]=new Point(minXone,yone);
            points[1]=new Point(minXtwo,ytwo);
            points[2]=new Point(maxXtwo,ytwo);
            points[3]=new Point(maxXone,yone);
            return points;

        }
        return null;
    }


    public static void getNearbyTwoPoints(Set<Point> points,Point curPoint){
        HashMap<Point,Double> pointDoubleHashMap=new HashMap<>();
        points.stream()
                .forEach(point ->
                        pointDoubleHashMap.put(point,manhattanDistance(curPoint,point)));


    }

    public static PositionList sortIntoPolygon(Polygon polygon,  List<List<Polygon.EndPoint>> dividedPointsList){
        Map<Integer, List<Polygon.EndPoint>> m=dividedPointsList.stream().flatMap(x->x.stream()) //按相同线段分组
                .collect(Collectors.groupingBy(Polygon.EndPoint::getBelongToWhichEdge,
                        Collectors.toList()));

        Polygon newPolygon=polygon.clone();
        m.forEach((k,v)->  //线段插值
                {
                        int index=v.get(0).belongToWhichEdge;
                        LineSeg lineSeg=newPolygon.get(index);
                        Point refPoint=lineSeg.get(0);
                        System.out.println(v);
                        v.stream().sorted((p1,p2)->compareClosetPoint(p1,p2,refPoint))
                                .forEach(endPoint -> lineSeg.addAfter(endPoint,0));
                }
        );

        PositionList pointsList=new PositionList();
        newPolygon.stream().forEachOrdered(item->item.stream().forEachOrdered(p->pointsList.add(p)));//展开lineseg，保存

        System.out.println(pointsList);
        return pointsList;
    }
}
