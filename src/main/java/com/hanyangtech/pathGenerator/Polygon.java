package com.hanyangtech.pathGenerator;

import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.Node;
import com.hanyangtech.pathPreTreat.Point;
import com.hanyangtech.pathSegTreat.PositionList;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 数据类型
 * 描述路径所构成的图形
 * 存放LineSeg
 */
public class Polygon extends CycleLinkedList<LineSeg> implements Cloneable{

    private Boolean canBeDivided=null;

    public PositionList points;

    /**
     * @param positionList 多边形的端点集合
     */
    public Polygon(PositionList positionList){
        super();
        points=positionList;
        this.initEdgeList(positionList);
    }

    public Polygon() {
        super();
    }

    private void initEdgeList(PositionList positionList){
        ArrayList<Node> nodes = positionList.getAllNodes();
        for(int i=0;i<nodes.size()-1;i++){
            Node<Point> curNode=nodes.get(i);
            Node<Point> nextNode=curNode.getNext();
            this.add(new LineSeg(curNode.getValue(),nextNode.getValue()));
        }
        this.add(new LineSeg(positionList.getLastNode().getValue(),positionList.getFirstNode().getValue()));
    }

    public Point getPreviousYNotEqualPoint(int index,double secValueY){
        PositionList newPoints=this.points.clone();
        if(index<newPoints.size()-1){
        newPoints.moveHeader(index+1);}

        Node<Point> node=newPoints.getLastNode();
        if(node.getValue().y != secValueY){return node.getValue();}

        Point point=newPoints.getPreviousYNotEqualPoint(node);
        return point;
    }
    public Point getNextYNotEqualPoint(int index,double secValueY){
        PositionList newPoints=this.points.clone();
        if(index<newPoints.size()-1){
            newPoints.moveHeader(index+1);}

        Node<Point> node=newPoints.getFirstNode();
        if(node.getValue().y != secValueY){return node.getValue();}
        Point point=newPoints.getNextYNotEqualPoint(node);
        return point;

    }

    @Override
    protected Polygon clone()  {
        Polygon polygon=new Polygon();
        Iterator it=this.iterator();
        while (it.hasNext()){
            polygon.add((LineSeg) it.next());
        }
        polygon.points=this.points.clone();
        return polygon;
    }

    public class EndPoint extends Point{
        public boolean isSameSideAroundPoints;

        public int getBelongToWhichEdge() {
            return belongToWhichEdge;
        }

        public int belongToWhichEdge;

        public EndPoint(double x, double y) {
            this.x=x;
            this.y=y;
        }
        public String toString(){
            String data=super.toString();
            data=String.format("%s isSameSideAroundPoints: %b, belongToWhichEdge: %d ",data,isSameSideAroundPoints,belongToWhichEdge);
            return data;
        }
    }


}
