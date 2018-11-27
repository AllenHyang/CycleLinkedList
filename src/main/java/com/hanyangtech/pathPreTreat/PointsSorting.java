package com.hanyangtech.pathPreTreat;

import Jama.Matrix;
import com.hanyangtech.CycleLinkedList;

public class PointsSorting {

    static int[][] eightNeighbour={{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}};
    static int nRow = 0;
    static int nLine = 0;
    static Matrix mapEdge;
    public static CycleLinkedList<Point> sorting(Matrix mapE){
        nRow = mapE.getRowDimension();
        nLine = mapE.getColumnDimension();
        mapEdge = mapE.copy();
        CycleLinkedList<Point> path = new CycleLinkedList<>();
        Point point0 = new Point();
        Point tmpPoint = new Point();
        int[] nap=pathPointNum();
        int N = nap[0];
        point0.x = nap[1];
        point0.y = nap[2];
        path.add(point0);
        for (int i=0;i<N-1;i++){
            tmpPoint=pathPoint((int)path.get(i).x,(int)path.get(i).y);
            path.add(tmpPoint);
        }
        path.add(point0);
        //transposePointsArrayList(pathFindingAlgorithm)
        return path ;


        //double a=mapEdge.get(0,0);

    }

    public static int[] pathPointNum(){
        int[] numAndFristpoint={0,0,0};
        int n=0;
        for (int i=0; i<nRow; i++){
            for(int j=0; j<nLine; j++){
                if (mapEdge.get(i,j)==1){
                    n++;
                    if (numAndFristpoint[1]==0){
                        mapEdge.set(i,j,4);
                        numAndFristpoint[1]=i;
                        numAndFristpoint[2]=j;
                    }
                }
            }
        }
        numAndFristpoint[0]=n;
        return numAndFristpoint;
    }

    public static Point pathPoint(int ci, int cj){
        Point point = new Point();
        for (int i=0;i<8;i++){
            point = findPathPoint(ci+eightNeighbour[0][i],cj+eightNeighbour[1][i]);
            if (point.x>0){
                break;
            }
        }
        return point;
    }


    public static Point findPathPoint(int i, int j){
        Point point = new Point();
        if (mapEdge.get(i,j)==1){
            mapEdge.set(i,j,4);
            point.x=i;
            point.y=j;
        }
        return point;
    }
}
