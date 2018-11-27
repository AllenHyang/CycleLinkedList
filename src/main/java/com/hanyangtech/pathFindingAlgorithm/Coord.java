package com.hanyangtech.pathFindingAlgorithm;

public class Coord implements Cloneable
{
    public int x;
    public int y;

    public Coord(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Coord)
        {
            Coord c = (Coord) obj;
            return x == c.x && y == c.y;
        }
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args){
        Coord coord=new Coord(1,1);
        System.out.println(coord.equals(null));
    }
}
