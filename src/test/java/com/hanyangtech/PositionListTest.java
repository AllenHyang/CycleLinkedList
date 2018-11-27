package com.hanyangtech;

import com.hanyangtech.pathPreTreat.Point;
import com.hanyangtech.pathSegTreat.PositionList;
import org.junit.Test;

public class PositionListTest {

    @Test
    public void sparsePoints() {
        PositionList points = new PositionList();
        points.add(new Point(1, 2));
        points.add(new Point(2, 3));
        points.add(new Point(3, 4));
        points.add(new Point(4, 5));
        points.add(new Point(5, 6));
        points.add(new Point(5.1, 6));
        points.add(new Point(5.11, 6));
        points.add(new Point(5.13, 6));

        System.out.println(points);
        points.sparsePoints(0.5);
        System.out.println(points);

    }

    @Test
    public void removeDuplicatePoints() {
        PositionList points = new PositionList();
        points.add(new Point(1, 2));
        points.add(new Point(2, 3));
        points.add(new Point(2, 3));
        points.add(new Point(2, 3));
        points.add(new Point(5, 6));
        points.add(new Point(5, 6));
        points.add(new Point(5.11, 6));
        points.add(new Point(5.13, 6));

        points.removeDuplicatePoints();
        System.out.println(points);

    }
}