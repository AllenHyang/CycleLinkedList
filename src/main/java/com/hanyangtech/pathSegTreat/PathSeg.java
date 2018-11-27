package com.hanyangtech.pathSegTreat;


public class PathSeg {
    public Line getLine() {
        return line;
    }

    private Line line;

    public PositionList getPoints() {
        return points;
    }

    public String toString(){
        return getPoints().toString();
    }

    private PositionList points;


    public PathSeg(PositionList points){
        this.line=new Line(points.getFirstNode().getValue(),points.getLastNode().getValue());
        this.points=points;
    }

}
