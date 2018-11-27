package com.hanyangtech.pathSegTreat;

import com.hanyangtech.pathPreTreat.Point;

public class Line {
    private Double A,B,C;
    public Line(Double A,Double B,Double C){
        this.A=A;
        this.B=B;
        this.C=C;

    }
    public Line(Point one, Point two){
        A=one.y-two.y;
        B=two.x-one.x;
        C=one.x*two.y-one.y*two.x;
    }
    public double pointToLineDistance(Point point){
        double result=0;
        if(A!=0 && B!=0){
            result=Math.abs(A*point.x+B*point.y+C)/Math.hypot(A,B);}
        return result;
    }
}
