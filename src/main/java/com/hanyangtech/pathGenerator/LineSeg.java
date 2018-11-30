package com.hanyangtech.pathGenerator;

import com.hanyangtech.pathPreTreat.Point;
import com.hanyangtech.pathSegTreat.PositionList;

import java.util.Iterator;

public class LineSeg extends PositionList implements Cloneable{
    public LineSeg(Point one , Point two){
        this.add(one);
        this.add(two);
    }
    public LineSeg(){
        super();
    }

    @Override
    public LineSeg clone() {
        LineSeg lineSeg=new LineSeg();
        Iterator it=this.iterator();
        while (it.hasNext()){
            lineSeg.add((Point) it.next());
        }
        return clone();
    }
}
