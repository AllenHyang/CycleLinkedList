package com.hanyangtech.pathPreTreat;

import org.junit.Test;

import static org.junit.Assert.*;

public class PointTest {

    @Test
    public void equals() {
        Point one=new Point(1,3);
        Point two=new Point(1,3);
        System.out.println(one.equals(two));
    }
}