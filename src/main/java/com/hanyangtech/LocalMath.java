package com.hanyangtech;

import com.hanyangtech.pathGenerator.LineSeg;
import com.hanyangtech.pathPreTreat.Point;

import java.util.function.Function;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class LocalMath {
    public static double interPolation(double x1, double y1, double x2, double y2, double valueY) {
        return (x2 - x1) / (y2 - y1) * (valueY - y1) + x1;
    }

    public static Double interPolation(LineSeg lineSeg, double valueY) {
        boolean firstBiggerThanValue = (lineSeg.getFirstNode().getValue().y) > valueY;
        boolean lastBiggerThanValue = lineSeg.getLastNode().getValue().y > valueY;
        boolean equalOntheLine=lineSeg.getLastNode().getValue().y == valueY && lineSeg.getFirstNode().getValue().y ==valueY;
        boolean onTheEnd=lineSeg.getLastNode().getValue().y == valueY ^ lineSeg.getFirstNode().getValue().y ==valueY;
        //都在直线上方，在直线下方，在直线上，返回null
        if (!onTheEnd && ((firstBiggerThanValue && lastBiggerThanValue) || (!firstBiggerThanValue && !lastBiggerThanValue) || equalOntheLine)){
            return null;
        }
        return interPolation(lineSeg.getFirstNode().getValue().x, lineSeg.getFirstNode().getValue().y,
                lineSeg.getLastNode().getValue().x, lineSeg.getLastNode().getValue().y, valueY);

    }

    public static double manhattanDistance(Point one, Point two){
        return abs(one.x-two.x)+abs(one.y-two.y);
    }

    public static int compareClosetPoint(Point one,Point two, Point refpoint){
        double m_one=manhattanDistance(one,refpoint);
        double m_two=manhattanDistance(two,refpoint);

        if(m_one< m_two){return 1;}    //two is closer
        if(m_one>m_two){return -1;}
        return 0;



    }



}
