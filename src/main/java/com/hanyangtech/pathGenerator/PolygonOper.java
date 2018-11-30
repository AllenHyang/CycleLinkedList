package com.hanyangtech.pathGenerator;

import com.hanyangtech.LocalMath;
import com.hanyangtech.pathPreTreat.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 *获取有效切分点集合
 *  List<Set> getFinalValidDevidedPointList(Polygon polygon)
 *
 */
public class PolygonOper {

    /**
     *计算交点
     * @param polygon
     * @param line 寻找交点的Y轴直线
     * @return Map<交点坐标,Polygon中的线段序号>
     */
    public static HashSet<Polygon.EndPoint> findInterSecWithPolygon(Polygon polygon, double line){

        List<Double> xCoorList=polygon.stream().map(lineSeg ->
                LocalMath.interPolation(lineSeg,line)).collect(Collectors.toList());

        HashSet<Polygon.EndPoint> pointsSet= new HashSet<>();
        for(int i=0;i<xCoorList.size();i++){
            if(xCoorList.get(i)!=null){
            Polygon.EndPoint endPoint=polygon.new EndPoint(xCoorList.get(i),line);
            endPoint.belongToWhichEdge=i;
                pointsSet.add(endPoint);
            }
        }
        return pointsSet;
    }

    /**
     * 判断输入点集合是否为同一侧，并排序输出
     * @param secPoint
     * @param polygon
     * @return
     */
    public static List checkSecPointsIsSameSide(List<Set<Polygon.EndPoint>>  secPoint,Polygon polygon){
        List<List> secSameAtSide = secPoint.stream().map(m ->
                markListPointsIsSameSide(polygon, m))
                .collect(Collectors.toList());
        return secSameAtSide;
    }

    /**
     * 获取所有交点集合
     * @param polygon
     * @return [交点，线段序号]
     */
    public static List  getAllCornerInterSecPointsInPolygon(Polygon polygon){
        List<HashSet<Polygon.EndPoint>> secPointsList = polygon.stream().map(lineSeg -> lineSeg.getFirstNode().getValue().y)
                .distinct()
                .map(y -> findInterSecWithPolygon(polygon, y))
                .filter(pmap -> pmap.size() > 2) //交点个数大于2的所有交点集合
                .collect(Collectors.toList());
        return secPointsList;
    }

    /**
     * 过滤无效点，判断是否为切分点
     * @param secPointsIsSameSide 输入排序后的切分点集合
     * @return
     */
    public static List filtOutNotDevidedPointList(List<List> secPointsIsSameSide){
        List filtedSecPointsList=new ArrayList<>();
        secPointsIsSameSide.stream().forEach(list->{
            List sortedList = (List) list.stream().sorted(Comparator.comparing(Polygon.EndPoint::getX)).collect(Collectors.toList());
            Boolean status= isDevidedLineBySameSideResult(sortedList);
            if(status){filtedSecPointsList.add(sortedList);}
        });
        return filtedSecPointsList;
    }

    /**
     * 获取有效切分点集合
     * @param polygon 输入多边形
     * @return List<Set>
     */
    public static List getFinalValidDevidedPointList(Polygon polygon){
        List<Set<Polygon.EndPoint>>  secPointsList= getAllCornerInterSecPointsInPolygon(polygon);
        List secPointsIsSameSide=checkSecPointsIsSameSide(secPointsList,polygon);

        List filtedSecPointsList= filtOutNotDevidedPointList(secPointsIsSameSide);
        //System.out.println(filtedSecPointsList);

        return filtedSecPointsList;
    }

    /**
     * 判断点集合中的点的同侧性，判断是否为有效分割点
     * @param m
     * @return
     */
    public static boolean isDevidedLineBySameSideResult(List<Polygon.EndPoint> m){
            for(int i=0;i<m.size();i++){
                Polygon.EndPoint endPoint=m.get(i);
                boolean status=endPoint.isSameSideAroundPoints;
                if((i==0 && status) || (i==m.size()-1 && status)){return false;}
                else if(!status && i>0 && i<m.size()-1){return false;}
            }
            return true;
             }

    /**
     * 判断一组点，是否为同侧
     * @param polygon
     * @param m
     * @return
     */
    public static List markListPointsIsSameSide(Polygon polygon, Set<Polygon.EndPoint> m){
        List result=m.stream().map(endPoint ->{
            int index=endPoint.belongToWhichEdge;
            Point firstP = polygon.getPreviousYNotEqualPoint(index, endPoint.y);
            Point lastP=polygon.getNextYNotEqualPoint(index,endPoint.y);
            boolean sameside=(firstP.y>endPoint.y && lastP.y>endPoint.y) || (firstP.y<endPoint.y && lastP.y<endPoint.y);
            endPoint.isSameSideAroundPoints=sameside;
            return endPoint;
        } ).collect(Collectors.toList());

        return result;
    }

    /**
     * 排序map
     * @param m
     * @param comparator
     * @param <k>
     * @param <v>
     * @return
     */
    public  static <k,v> LinkedHashMap<k,v> sortHashMap(HashMap<k,v> m,Comparator comparator){
        LinkedHashMap<k, v> finalMap = new LinkedHashMap<>();
        m.keySet().stream()
                .sorted(comparator)
                .forEachOrdered(e -> finalMap.put((k) e,m.get(e)));
        return finalMap;
    }

}
