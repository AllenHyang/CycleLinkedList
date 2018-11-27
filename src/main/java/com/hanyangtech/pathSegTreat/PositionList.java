package com.hanyangtech.pathSegTreat;

import com.hanyangtech.CycleLinkedList;
import com.hanyangtech.pathPreTreat.Point;

import java.util.Iterator;
import java.util.ListIterator;

import static java.lang.Math.abs;

public class PositionList extends CycleLinkedList<Point> {

    public PositionList(){
        super();

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


}
