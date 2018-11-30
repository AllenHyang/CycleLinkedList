package com.hanyangtech.pathSegTreat;

import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.Node;
import com.hanyangtech.pathPreTreat.Point;

import java.util.Iterator;
import java.util.ListIterator;

import static java.lang.Math.abs;

public class PositionList extends CycleLinkedList<Point> implements Cloneable {


    public PositionList(){
        super();
    }

    @Override
    public PositionList clone()  {
        PositionList positionList=new PositionList();
        ListIterator<Point> it=this.listIterator(0);
        while(it.hasNext()){
            positionList.add(it.next());
        }
        return positionList;
    }

    public PositionList(CycleLinkedList<Point> cycleLinkedList) {
        super();
        Iterator<Point> it = cycleLinkedList.iterator();
        while (it.hasNext()){
            this.add(it.next());
        }
    }

    public void sparsePoints(double disTorlorance){
        ListIterator<Point> it=listIterator(1);
        while (it.hasNext()){
            Point curPoint=it.next();
            Point prePoint=it.previous();
            if(abs(curPoint.x-prePoint.x)<disTorlorance && abs(curPoint.y-prePoint.y)<disTorlorance){
                it.remove();
            }
        }
    }

    public void removeDuplicatePoints(){
        Node curNode=this.getFirstNode();
        while (curNode.getNext().getValue() != null){
            if(curNode.getValue().equals(curNode.getNext().getValue())){
                removeNode(curNode.getNext());
                continue;
            }
            curNode=curNode.getNext();
        }
    }


    public boolean addAfter(Point e, int index){
        Node curNode=getNode(index);
        if(curNode == this.getLastNode())
            {add(e);return true;}

        Node nexNode=curNode.getNext();

        if(e.equals(curNode.getValue()) || e.equals(nexNode.getValue())){  //相同点 不添加
            return false;
        }
        Node<Point> node=new Node<>(e,nexNode,curNode);
        curNode.setNextNode(node);
        nexNode.setPreviousNode(node);
        increaseSize();

        return true;
    }

    @Override
    public boolean add(Point e) {
        if(size()!=0 && this.getLastNode().getValue().equals(e)){return false;};
        super.add(e);
        return true;
    }
    public Point getPreviousYNotEqualPoint(Node<Point> curNode){
        Point curPoint=curNode.getValue();
        Node<Point> preNode=curNode.getPrevious();
        if(preNode.getValue() == null){
            throw new RuntimeException("item not exist");
        }
        Point prePoint=preNode.getValue();
        if(prePoint.y == curPoint.y){
            prePoint=getPreviousYNotEqualPoint(preNode);
        }
        return prePoint;
    }
    public Point getNextYNotEqualPoint(Node<Point> curNode){
        Point curPoint=curNode.getValue();
        Node<Point> nextNode=curNode.getNext();
        if(nextNode.getValue() == null){
            throw new RuntimeException("item not exist");
        }
        Point nextPoint=nextNode.getValue();
        if(nextPoint.y == curPoint.y){
            nextPoint=getNextYNotEqualPoint(nextNode);
        }
        return nextPoint;
    }

}
