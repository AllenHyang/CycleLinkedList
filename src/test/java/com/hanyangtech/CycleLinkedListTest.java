package com.hanyangtech;

import com.hanyangtech.pathPreTreat.Point;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class CycleLinkedListTest {

    @Test
    public void map() {
        CycleLinkedList<Integer> cycleLinkedList=new CycleLinkedList<>();

    }

    @Test
    public void add() {
        CycleLinkedList<Integer> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(1);
        cycleLinkedList.add(5);
        cycleLinkedList.add(566);
        System.out.println("start");
        Iterator<Integer> it = cycleLinkedList.listIterator(1);
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }

    @Test
    public void moveHeader() {
        CycleLinkedList<Integer> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(1);
        cycleLinkedList.add(5);
        cycleLinkedList.add(566);
        cycleLinkedList.add(5669);


        cycleLinkedList.moveHeader(3);
        System.out.println(cycleLinkedList);
    }

    @Test
    public void min() {
        CycleLinkedList<Integer> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(1);
        cycleLinkedList.add(5);
        cycleLinkedList.add(566);
        cycleLinkedList.add(566);
        int result=cycleLinkedList.min(x->x,Integer::compareTo);
        System.out.println(result);
    }
    @Test
    public void max() {
        CycleLinkedList<Point> cycleLinkedList=new CycleLinkedList<>();
        cycleLinkedList.add(new Point(1,5));
        cycleLinkedList.add(new Point(2,6));
        cycleLinkedList.add(new Point(3,7));
        cycleLinkedList.add(new Point(4,8));
        double result=cycleLinkedList.max(x->x.y,Double::compareTo);
        Assert.assertEquals(result,8.0,0.001);
        double result2=cycleLinkedList.max(x->x.x,Double::compareTo);
        Assert.assertEquals(result2,4.0,0.001);

    }
}