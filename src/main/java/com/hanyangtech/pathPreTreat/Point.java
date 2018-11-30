package com.hanyangtech.pathPreTreat;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Point implements Cloneable {
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double x;
    public double y;

    public int intX(){return (int)x;}
    public int intY(){return (int)y;}
    public Point(){}
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString(){
        return String.format("x:%s,y:%s",x,y);
    }
    public double distanceToPoint(Point point){
        return sqrt(pow((point.x-x),2)+pow((point.y-y),2));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Point){
            return ((Point) obj).x==x && ((Point) obj).y==y;
        }
        return false;
    }

    @Override
    public Point clone()  {
        Point point=null;
        try {
            point= (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return point;
    }

    @Override
    public int hashCode(){

        return intX()+intY();
    }

}
