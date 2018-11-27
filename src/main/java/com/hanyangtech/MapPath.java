package com.hanyangtech;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Jama.Matrix;

import static com.hanyangtech.MapPath.Vertex.findCornersPoints;
import static com.hanyangtech.MapPath.Vertex.sortCornerPointIntoPath;
import static com.hanyangtech.MatrixFunc.*;
import static com.hanyangtech.MatrixFunc.DIRECTION.CLOCWISE;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MapPath {
    private double width;
    public MapPath setWidth(double width){this.width=width;return this;}
    public Vertex vertex=new Vertex();
    public static class Point{
        public double x;
        public double y;
        public int indexBefore;
        public int indexAfter;

        public double distanceToPoint(Point point){
            return sqrt(pow((point.x-x),2)+pow((point.y-y),2));
        }

        @Override
        public String toString(){
            return "x:"+String.valueOf(x)+" y:"+String.valueOf(y);
        }

        public Point() {
            x=(double)0.0;
            y=(double)0.0;
        }

        public Point(Object x, Object y) {
            this.x=Double.valueOf(x.toString()).doubleValue();
            this.y=Double.valueOf(y.toString()).doubleValue();
        }
    }

    public static double[] getAxisOfArrayListPoints(ArrayList<Point> points,String axis) throws NoSuchFieldException, IllegalAccessException {
        double[] axisList = new double[points.size()];
        Field idF = Point.class.getDeclaredField(axis);
        idF.setAccessible(true);
        for (int i = 0; i < points.size(); i++) {
            axisList[i] = idF.getDouble(points.get(i));

        }
        return axisList;
    }

    class Line{
        private Double A,B,C;
        public Line(Double A,Double B,Double C){
            this.A=A;
            this.B=B;
            this.C=C;

        }
        public Line(Point one,Point two){
            A=one.y-two.y;
            B=two.x-one.x;
            C=one.x*two.y-one.y*two.x;
        }
        public double pointToLineDistance(Point point){
            double result=0;
            if(A!=0 && B!=0){
            result=Math.abs(A*point.x+B*point.y+C)/Math.hypot(A,B);}
            return result;
        }
    }

    private ArrayList<Integer> indexList=new ArrayList<>();
    private double TRIMTolorance=0.1;
    private ArrayList<Point> rawPoints=new ArrayList<>();
    private ArrayList<PathSeg> pathSegs=new ArrayList<>();
    private double minDisTolorance=1;
    private int maxAcceptSegsNumber=3;
    private enum STATUS{
        CLOSE,OPEN;
    }
    private class PathResult{
        public Double value;
        public Integer index;
    }
    private ArrayList<Point> trimedPoints;

    public MapPath setTRIMTolorance(double tor){
        this.TRIMTolorance=tor;
        return this;
    }
    public MapPath setMinDisTolorance(double minD){
        this.minDisTolorance=minD;
        return this;
    }
    public MapPath setMaxAcceptSegsNumber(int segsNumber){
        this.maxAcceptSegsNumber=segsNumber;
        return this;
    }
    public ArrayList<Point> getRawPoints(){
        return this.rawPoints;
    }
    public void setRawPoints(ArrayList<Point> points){
        this.rawPoints=points;
    }
    public <T> MapPath(ArrayList<T> points) throws NoSuchFieldException, IllegalAccessException {
        Field fldX=points.get(0).getClass().getField("x");
        Field fldY=points.get(0).getClass().getField("y");

        for(T t:points){
            rawPoints.add(new Point(fldX.get(t),fldY.get(t)));

        }
    }
    private PathResult findMaxDPointResult(PathSeg pathSeg){
        List<Double> dis=new ArrayList<>();
        for(Point point:pathSeg.points){
            dis.add(pathSeg.line.pointToLineDistance(point));
        }
        PathResult pathResult=new PathResult();
        pathResult.value=Collections.max(dis);
        pathResult.index=dis.indexOf(pathResult.value);
        pathSeg.setMaxDisPointResult(pathResult);
        return pathResult;
    }

    private class PathSeg{
        public SegInfo segInfo;
        private ArrayList<Point> points;
        private Line line;
        public PathResult maxDisPointResult;
        public void setMaxDisPointResult(PathResult pathResult){
            this.maxDisPointResult=pathResult;
        }
        public PathSeg(ArrayList<Point> points){
            this.segInfo=new SegInfo(0,points.size(),STATUS.OPEN);
            this.points=points;
            this.line=new Line(points.get(0),points.get(points.size()-1));
        }
        public PathSeg(ArrayList<Point> points,Integer startIndex,Integer endIndex,STATUS status){
            this.segInfo=new SegInfo(startIndex,endIndex,status);
            this.points=points;
            this.line=new Line(points.get(0),points.get(points.size()-1));
        }
        public PathSeg(ArrayList<Point> points,SegInfo segInfo){
            this.points=points;
            this.segInfo=segInfo;
            this.line=new Line(points.get(0),points.get(points.size()-1));
        }
        public Integer getPathIndexByValue(Point data){
            return this.points.indexOf(data)+segInfo.startIndex;
        }
        public PathSeg[] splitBySegIndex(Integer index){
            PathSeg[] pathSegs=new PathSeg[2];
            ArrayList<Point> nPointOne= new ArrayList<>(this.points.subList(0,index));
            ArrayList<Point> nPointTwo= new ArrayList<>(this.points.subList(index,this.points.size()));

            pathSegs[0]=new PathSeg(nPointOne,this.segInfo.startIndex,this.segInfo.startIndex+index,STATUS.OPEN);
            pathSegs[1]=new PathSeg(nPointTwo,this.segInfo.startIndex+index,this.segInfo.endIndex,STATUS.OPEN);

            return pathSegs;
        }
    }

    private class SegInfo{
        public Integer startIndex;
        public Integer endIndex;
        STATUS status=STATUS.OPEN;
        public SegInfo(Integer startIndex,Integer endIndex,STATUS status){
            this.startIndex=startIndex;
            this.endIndex=endIndex;
            this.status=status;
        }


    }


    private ArrayList<Point> trimPoints(ArrayList<Point> points){
        ArrayList<Point> newPoints=new ArrayList<>();
        newPoints.add(points.get(0));
        for(int i=1;i<points.size();i++){
            if((Math.abs(points.get(i).x-points.get(i-1).x)>TRIMTolorance) || (Math.abs(points.get(i).y-points.get(i-1).y)>TRIMTolorance)){
                newPoints.add(points.get(i));
                //System.out.println(points.get(i).x+" "+points.get(i).y);
            }
        }
        return newPoints;
    }
    private void recordIndex(SegInfo seginfo){
        if(seginfo.status == STATUS.CLOSE){
            this.indexList.add(seginfo.startIndex);
            this.indexList.add(seginfo.endIndex);
        }
    }
    private void recordPathSeg(PathSeg pathSeg){
        this.pathSegs.add(pathSeg);
    }

    private void iterate(PathSeg[] pathSegs){
        for(PathSeg pathSeg:pathSegs){
            System.out.println("In the loop:"+pathSeg.points.size());
            PathResult pathResult=findMaxDPointResult(pathSeg);
            if(pathResult.value>minDisTolorance || pathSeg.points.size()>maxAcceptSegsNumber){
                if(pathResult.value==0){pathResult.index=round(pathSeg.points.size()/2);}
                PathSeg[] newPathSegs=pathSeg.splitBySegIndex(pathResult.index);
                iterate(newPathSegs);
            }
            else {
                recordPathSeg(pathSeg);
            }
        }
    }

    private void execute(){
        if(!this.pathSegs.isEmpty()){
            return;
        }
         //trimedPoints=trimPoints(this.rawPoints);

        PathSeg[] pathSegslocal={new PathSeg(this.rawPoints)};
        iterate(pathSegslocal);

    }
    public ArrayList<Point> getVertexPoints(){
        this.execute();
        ArrayList<Point> newPoints=new ArrayList<>();

        for(PathSeg pathSeg:this.pathSegs){
            Integer index=pathSeg.segInfo.endIndex-1;
            newPoints.add(this.rawPoints.get(index));
        }
        return newPoints;
    }

    public static class Vertex{
        private static Matrix getRotMatrix(double angle){
            double[][] temp={{cos(angle),-sin(angle)},{sin(angle),cos(angle)}};
            return new Matrix(temp);
        }
        //旋转平移，消除al


        public static Matrix rotate2DMatrix(Matrix matrix,double angle){

            return getRotMatrix(-1*angle).times(matrix);
        }

        private static double interPolation(double x1,double y1,double x2,double y2,double valueY){
            return (x2-x1)/(y2-y1)*(valueY-y1)+x1;
        }
        public static Matrix translate2DMatrix(Matrix matrix,Matrix transM){
            Matrix copyMatrix=matrix.copy();
            int colD=copyMatrix.getColumnDimension();
            for(int i=0;i<colD;i++){
                Matrix curM=copyMatrix.getMatrix(0,1,i,i);
                Matrix newCurM=curM.transpose().plus(transM).transpose();

                copyMatrix.setMatrix(0,1,i,i,newCurM);
            }
        return copyMatrix;
        }

        private static enum COMPARE{
            SMALL,EQUAL,BIG
        }
        private static COMPARE compareTwoNumber(double one,double two){
            COMPARE compare;
            if(one == two){compare=COMPARE.EQUAL;}
            else if(one >two){
                compare=COMPARE.BIG;
            }
            else{
                compare=COMPARE.SMALL;
            }
            return compare;
        }
        public static ArrayList<MapPath.Point> findInterSecPolygon(Matrix matrix, double line){
            COMPARE compareInit;
            ArrayList<MapPath.Point> items=new ArrayList<>();
            if(matrix.getColumnDimension()>1) {
                compareInit = compareTwoNumber(matrix.get(1, 0) ,line);
                for (int j = 0; j < matrix.getColumnDimension(); j++) {
                    double item = matrix.get(1, j);
                    COMPARE compareCur = compareTwoNumber(item ,line);

                    if(item == line){
                        if(j!=matrix.getColumnDimension()-1){compareInit = compareTwoNumber(matrix.get(1,j+1) ,line);}
                        double findX=matrix.get(0,j);
                        Point point=new Point(findX,line);
                        point.indexBefore=j-1;
                        point.indexAfter=j+1;
                        items.add(point);
                        continue;
                    }
                    if ( compareInit != compareCur) {
                        compareInit = compareCur;
                        double findX = interPolation(matrix.get(0, j - 1), matrix.get(1, j - 1), matrix.get(0, j), matrix.get(1, j), line);
                        Point point=new Point(findX,line);
                        point.indexBefore=j-1;
                        point.indexAfter=j;
                        items.add(point);
                        continue;

                    }

                }
            }
            removeDuplicatePoints(items);
            sortArrayListPointsByXvalue(items);
            return items;
        }

        public static  Matrix sortMatric(Matrix matrix){
            int minIndex=MatrixFunc.minValueIndexInMatrix(matrix);
            Matrix newMatrix=matrix.copy();
            if(minIndex!=0) {
                ArrayList<Point> points = new ArrayList<>();
                for (int j = minIndex; j < matrix.getColumnDimension(); j++) {
                    points.add(new Point(matrix.get(0, j), matrix.get(1, j)));
                }

                for (int j = 0; j < minIndex; j++) {
                    points.add(new Point(matrix.get(0, j), matrix.get(1, j)));
                }

            points.add(points.get(0));
                newMatrix=arrayListPointToMatrix(points);
            }
            return newMatrix;
        }

        public static void removeDuplicatePoints(ArrayList<Point> points){

            for(int i=0;i<points.size()-1;i++){
                Point iniPoint=points.get(i);
                for(int j=i+1;j<points.size();j++){
                    if(points.get(j).y==iniPoint.y && points.get(j).x==iniPoint.x){
                        points.remove(i);
                    }
                }
            }
        }

        public static boolean isDividePoint(Matrix matrix,double line,ArrayList<Point> points){
            if(points.size()<=1 ){return false;}
            boolean isDivided=false;
                for(int i=0;i<points.size();i++){
                    Point point=points.get(i);
                    if(point.indexBefore<0 || point.indexAfter>=points.size()){
                        isDivided=true;
                        continue;  //TODO : need to check the index 0 and index end
                    }
                    boolean status=isSameSideOfTwoPoints(line,matrix.get(1,point.indexAfter),matrix.get(1,point.indexBefore));
                    if(i<points.size() && status){isDivided=true;}
                    else if(i==points.size()-1 && !status) {isDivided=true;}
                    else{isDivided=false;break;}

                }
            return isDivided;
        }

        private static boolean isSameSideOfTwoPoints(double curValue,double oneValue,double otherValue){
            boolean status=(oneValue>curValue && otherValue >curValue) || (oneValue <curValue && otherValue <curValue)?true:false;
            return status;
        }

        public static class DivideResult{
            public boolean canBeDivided=false;
            public boolean leftS=false;
            public boolean rightS=false;
            public int curIndex;
            ArrayList<Point>[] leftAndRightIntersectionPoints;
        }

        public static ArrayList<Point>[] splitIntersectionPointsIntoLeftAndRight(ArrayList<Point> intersectionPoints,double x){
            ArrayList<Point>[] leftAndRight=new ArrayList[2];
            leftAndRight[0]=new ArrayList<>();
            leftAndRight[1]=new ArrayList<>();
            for(Point point:intersectionPoints){
                if(point.x<x){
                    leftAndRight[0].add(point);
                }
                else if(point.x>x){
                    leftAndRight[1].add(point);
                }
            }
            return leftAndRight;
        }

        public static double getCurPointBeforeValue(Point point,Matrix matrix){
            int beforeindex=point.indexBefore;
            double pointValueBefore=matrix.get(1, beforeindex);
                while (beforeindex >= 0) {
                    pointValueBefore = matrix.get(1, beforeindex);
                    if(pointValueBefore!=point.y){return pointValueBefore;}
                    beforeindex--;
                    if(beforeindex<0){beforeindex=beforeindex+matrix.getColumnDimension();}
                }

            return pointValueBefore;
        }
        public static double getCurPointAfterValue(Point point,Matrix matrix){
            int afterindex=point.indexAfter;
            if(afterindex>=matrix.getColumnDimension()){afterindex=afterindex-matrix.getColumnDimension();}
            double pointValueAfter=matrix.get(1, afterindex);
            while (afterindex < matrix.getColumnDimension()) {
                pointValueAfter = matrix.get(1, afterindex);
                if(pointValueAfter!=point.y){return pointValueAfter;}
                afterindex++;
                if(afterindex>matrix.getColumnDimension()){afterindex=afterindex-matrix.getColumnDimension();}

            }

            return pointValueAfter;
        }

        public static boolean canDivide(ArrayList<Point>[] leftAndRightIntersectionPoints,Matrix matrix,double line,ArrayList<Point> intersectionPoints ){
            int leftsideNum=leftAndRightIntersectionPoints[0].size();
            int rightsideNum=leftAndRightIntersectionPoints[1].size();
            ArrayList<Point> leftPoints=leftAndRightIntersectionPoints[0];
            ArrayList<Point> rightPoints=leftAndRightIntersectionPoints[1];
            if(leftsideNum<1 || rightsideNum <1){
                return false;
            }
            else{
                Point endPointLeft=leftPoints.get(0);
                Point endPointRight=rightPoints.get(rightPoints.size()-1);
                double leftValueBefore=getCurPointBeforeValue(endPointLeft,matrix);
                double leftValueAfter=getCurPointAfterValue(endPointLeft,matrix);
                double rightValueBefore=getCurPointBeforeValue(endPointRight,matrix);
                double rightValueAfter=getCurPointAfterValue(endPointRight,matrix);
                boolean isSameSideEndPointLeft=isSameSideOfTwoPoints(endPointLeft.y,leftValueBefore,leftValueAfter);
                boolean isSameSideEndPointRight=isSameSideOfTwoPoints(endPointRight.y,rightValueBefore,rightValueAfter);
                if(isSameSideEndPointLeft || isSameSideEndPointRight){return false;}
                boolean isSameSideLeftInnerPoints=false;
                boolean isSameSideRightInnerPoints=false;
                sortArrayListPointsByXvalue(intersectionPoints);
                for(int i=1;i<intersectionPoints.size()-1;i++){
                    Point curPoint=intersectionPoints.get(i);
                    double pointValueBefore=getCurPointBeforeValue(curPoint,matrix);
                    double pointValueAfter=getCurPointAfterValue(curPoint,matrix);

                    boolean isSameSide=isSameSideOfTwoPoints(curPoint.y,pointValueBefore,pointValueAfter);
                    if(!isSameSide){return false;}

                }
                /*
                for(int i=1;i<leftPoints.size();i++){
                    Point curPoint=leftPoints.get(i);
                    isSameSideLeftInnerPoints=isSameSideOfTwoPoints(curPoint.y,matrix.get(1,curPoint.indexBefore),matrix.get(i,curPoint.indexAfter));
                    if(!isSameSideLeftInnerPoints){return false;}
                }
                for(int i=0;i<rightPoints.size()-1;i++){
                    Point curPoint=rightPoints.get(i);
                    isSameSideRightInnerPoints=isSameSideOfTwoPoints(curPoint.y,matrix.get(1,curPoint.indexBefore),matrix.get(i,curPoint.indexAfter));
                    if(!isSameSideRightInnerPoints){return false;}
                }*/


            }
            return true;
        }

        public static DivideResult getDivideResultForMatrix(Matrix matrix){
            DivideResult divideResult=new DivideResult();

            for(int i=0;i<matrix.getColumnDimension();i++) {
                ArrayList<Point> intersectionPoints=findInterSecPolygon(matrix,matrix.get(1,i));
                ArrayList<Point>[] leftAndRightIntersectionPoints=splitIntersectionPointsIntoLeftAndRight(intersectionPoints,matrix.get(0,i));
                boolean candivide=canDivide(leftAndRightIntersectionPoints,matrix,matrix.get(1,i), intersectionPoints);

                if(candivide){
                    divideResult.curIndex=i;
                    divideResult.canBeDivided=true;
                    divideResult.leftAndRightIntersectionPoints=leftAndRightIntersectionPoints;
                    //divideResult.indexLeft=getLeftSideUpperIndex(matrix,divideResult);
                    //divideResult.indexRight=getRightSideUpperIndex(matrix,divideResult);
                    break;
                }
            }
            return divideResult;

        }
        private static boolean isSameSideOfSecPoints(ArrayList<Point> left,ArrayList<Point> right,double curPointX){
            double leftDifValue=0.,rightDifValue=0.;
            for(Point point:left){
                leftDifValue=(point.x-curPointX)+leftDifValue;
            }
            for(Point point:right) {
                rightDifValue = (point.x - curPointX) + rightDifValue;
            }
            if(leftDifValue*rightDifValue>0){
                return true;
            }
            else{
                return false;
            }
        }
        private static void extendFirstArrayBySecond(ArrayList<Point> one,ArrayList<Point> two){
            for(Point point:two){
                one.add(point);
            }
        }

        public static int getStartIndexExcludeOntheSameLinePoint(Matrix matrix,int curIndex){
            int startPointIndex=curIndex;
            double startValue = matrix.get(1, curIndex);

            while(startPointIndex<matrix.getColumnDimension()-1) {
                double nextV = matrix.get(1, startPointIndex+1);
                if (startValue == nextV) {
                    startPointIndex++;
                }
                else{
                    break;
                }
            }
            return startPointIndex;
        }
        public static ArrayList<Matrix> splitMatrixByValueOneTheDividLine(Matrix matrix,double curValue){
            ArrayList<Matrix> matrices=new ArrayList<>();
            int startIndex=getStartIndexExcludeOntheSameLinePoint(matrix,0);

            for(int i=1;i<matrix.getColumnDimension()-1;i++){
                double value=matrix.get(1,i);
                if(curValue==value){
                    matrices.add(matrix.getMatrix(0,1,startIndex,i));
                    //startIndex=i;
                    startIndex=getStartIndexExcludeOntheSameLinePoint(matrix,i);
                    i=startIndex+1;
                }
            }
            matrices.add(matrix.getMatrix(0,1,startIndex,matrix.getColumnDimension()-1));
            return matrices;
        }
        public static ArrayList<Matrix> assembleMatrics(DivideResult divideResult,Matrix matrix){
            ArrayList<Matrix> matrices=new ArrayList<>();
            Matrix[] resultMatrics=new Matrix[2];
            ArrayList<Point> interSecPointsLeft=divideResult.leftAndRightIntersectionPoints[0];
            ArrayList<Point> interSecPointsRight=divideResult.leftAndRightIntersectionPoints[1];
            Point leftSecPoint=interSecPointsLeft.get(0);
            Point rightSecPoint=interSecPointsRight.get(interSecPointsRight.size()-1);
            resultMatrics[0]=getMatrixByIndexRange(matrix,leftSecPoint.indexAfter,rightSecPoint.indexBefore);
            resultMatrics[0]=addStartEndPointForMatrix(resultMatrics[0],leftSecPoint,rightSecPoint);

            resultMatrics[1]=getMatrixByIndexRange(matrix,rightSecPoint.indexAfter,leftSecPoint.indexBefore);
            resultMatrics[1]=addStartEndPointForMatrix(resultMatrics[1],rightSecPoint,leftSecPoint);

            double curValue=matrix.get(1,divideResult.curIndex);
            ArrayList<Matrix> matrixOne=splitMatrixByValueOneTheDividLine(resultMatrics[0],curValue);
            ArrayList<Matrix> matrixTwo=splitMatrixByValueOneTheDividLine(resultMatrics[1],curValue);
            extendFirstArrayBySecondObject(matrices,matrixOne);
            extendFirstArrayBySecondObject(matrices,matrixTwo);

            return matrices;

        }

        public  ArrayList<Matrix> matrixArrayListPath=new ArrayList<>();

        public  void divideTheMatrix(Matrix matrix){
            Matrix newMatrix=makeMatrixClose(matrix);


            //draw(newMatrix);
            DivideResult d=getDivideResultForMatrix(newMatrix);
            if(!d.canBeDivided){
                matrixArrayListPath.add(newMatrix);
                return;
            }
            ArrayList<Matrix> matrices=assembleMatrics(d,newMatrix);
            for(Matrix matrix1:matrices){
                divideTheMatrix(matrix1);
            }
        }

        public static ArrayList<MapPath.Point> findCornersPoints(Matrix matrix,double width){
            Matrix ky=matrix.getMatrix(1,1,0,matrix.getColumnDimension()-1); //y
            double yMax=MatrixFunc.maxInMatrix(ky);
            double yMin=MatrixFunc.minInMatrix(ky);

            int number=(int)ceil((yMax-yMin)/width)+2;
            double y0=(ceil(yMin/width)-1)*width-width*0.5;

            ArrayList<MapPath.Point> cornerPoints=new ArrayList<>(); //find the cornerPoints
            for(int i=0;i<number;i++){
                double ytmp=y0+i*width;
                ArrayList<MapPath.Point> kk=findInterSecPolygon(matrix,ytmp);
                for(MapPath.Point point:kk){
                    cornerPoints.add(point);
                }

            }
            return  cornerPoints;
        }

        public static void findTheDividePoint(Matrix matrix){

            for(int i=0;i<matrix.getColumnDimension();i++) {
                ArrayList<Matrix> matrices = MatrixFunc.splitMatrixByIndex(matrix, i);

                ArrayList<Point> pointsLeft=findInterSecPolygon(matrices.get(0),matrix.get(1,i));
                boolean leftS=isDividePoint(matrices.get(0),matrix.get(1,i),pointsLeft);

                ArrayList<Point> pointsRight=findInterSecPolygon(matrices.get(1),matrix.get(1,i));
                boolean rightS=isDividePoint(matrices.get(1),matrix.get(1,i),pointsRight);
                if(leftS && rightS){
                    //log("FIND IT!!!");
                    matrix.getMatrix(0,1,i,i).print(5,2);
                    ArrayList<Point> onePoints=new ArrayList<>();
                    int indexLeft=0;
                    for(int item=0;item<matrix.getColumnDimension();item++){
                        if(pointsLeft.get(0).x>=matrix.get(0,item)){
                            onePoints.add(makeAPointByIndex(matrix,item));
                        }
                        else{
                            indexLeft=item;
                            break;
                        }
                    }
                    for(Point point:pointsLeft){
                        onePoints.add(point);
                    }
                    int indexRight=0;
                    ArrayList<Point> rightPoints=new ArrayList<>();
                    for(int item2=matrix.getColumnDimension()-1;item2>=0;item2--){
                        if(pointsRight.get(0).x<=matrix.get(0,item2)){
                            rightPoints.add(makeAPointByIndex(matrix,item2));
                        }
                        else{
                            indexRight=item2;
                            break;
                        }
                    }
                    for(Point point:pointsRight){
                        onePoints.add(point);
                    }
                    for(int j=rightPoints.size()-1;j>=0;j--){
                        onePoints.add(rightPoints.get(j));
                    }
                    for(Point point:onePoints){
                        //log(point.x);
                    }
                    //part two
                    ArrayList<Point> secondPoints=new ArrayList<>();
                    secondPoints.add(pointsLeft.get(0));
                    for(int s2=indexLeft;s2<=i;s2++){
                        secondPoints.add(makeAPointByIndex(matrix,s2));
                        }
                    for(Point point:secondPoints){
                        //log(point.x);
                    }
                    //third part
                    ArrayList<Point> thirdPoints=new ArrayList<>();
                    for(int s3=i;s3<=indexRight;s3++){
                        thirdPoints.add(makeAPointByIndex(matrix,s3));
                    }
                    thirdPoints.add(pointsRight.get(pointsRight.size()-1));
                    for(Point point:thirdPoints){
                       // log(point.x);
                    }

                    break;
                }


            }
        }

        public static ArrayList<MapPath.Point>  sortCornerPointIntoPath(ArrayList<Point> points1Corner){
            ArrayList<MapPath.Point> points1AfterSort=new ArrayList<>();

            boolean isReverse=false;
            for(int i=0;i<points1Corner.size();i=i+2){
                int[] indexOfLine=reverseTwoNumber(i,i+1,isReverse);
                isReverse=!isReverse;
                for(int j:indexOfLine) {
                    points1AfterSort.add(points1Corner.get(j));
                }
            }
            return points1AfterSort;
        }
        private static int[] reverseTwoNumber(int one,int two,boolean yes){

            int[] t=new int[2];
            if(yes==true){
                t[0] = two;
                t[1] = one;}
            else{
                t[0] = one;
                t[1] = two;
            }
            return t;
        }

    }

    public static class PointsTreat{
        public ArrayList<Point> rawPoints;
        private double gridWidth;
        private double[] minXY,maxXY;
        private Point refPoint=new Point(0,0);
        private Matrix initEmptyMatrix;

        public PointsTreat(ArrayList<Point> points){
            this.rawPoints=points;
            minXY=minXYInArrayListPoints(points);
            maxXY=maxXYInArrayListPoints(points);
            gridWidth=max(maxXY[0]-minXY[0],maxXY[1]-minXY[1])/100;

        }
        public  ArrayList<Point> storePointsMovePointsToZeroPlusSomeWidthPosition(ArrayList<Point> points){
            refPoint.x=-minXY[0]+gridWidth*1.5+2;
            refPoint.y=-minXY[1]+gridWidth*1.5+2;

            return tranlateArrayListPoints(points,refPoint);

        }
        public ArrayList<Point> restorePointsMovePointsToZeroPlusSomeWidthPosition(ArrayList<Point> points){
            Point newRefPoint=new Point();
            newRefPoint.x=-this.refPoint.x;
            newRefPoint.y=-this.refPoint.y;
            return pointsArrayListMakeInt(tranlateArrayListPoints(points,newRefPoint));
        }
        public ArrayList<Point> restorePointsFromMapArry(MapArray mapArray){
           // ArrayList<MapPath.Point> mappoints=PointsSorting.sorting(mapArray.matrix.copy());
            //ArrayList<MapPath.Point> newMappoints=(pointsArraryListTimes(mappoints,this.gridWidth));
            return null;//restorePointsMovePointsToZeroPlusSomeWidthPosition(newMappoints);
        }

        public  class MapArray{
            public Matrix matrix;
            public final static int EMPTY=0;
            public final static int FILLED=1;
            public final static int FILLED_LEVEL2=2;
            public final static int FILLED_LEVEL3=3;

            public MapArray(){
            double xmax=maxXY[0];
            double xmin=minXY[0];
            double ymax=maxXY[1];
            double ymin=minXY[1];
            int mapRow=(int)ceil((xmax-xmin)/gridWidth+5);
            int mapLine=(int)ceil((ymax-ymin)/gridWidth+5);
            this.matrix= new Matrix(mapLine,mapRow, EMPTY);
            }
            public void fillMapArrayByCoor(int x,int y,int level){
                this.matrix.set(y,x,level);
            }
            public void fillMapArrayByIndex(int x,int y,int level){
                this.matrix.set(x,y,level);
            }
            public int getRow(){
                return this.matrix.getRowDimension();
            }
            public int getCol(){
                return this.matrix.getColumnDimension();
            }
            public int getMaxX(){
                return this.matrix.getColumnDimension();
            }
            public int getMaxY(){
                return this.matrix.getRowDimension();
            }
            public double getValueByIndex(int indexRow,int indexCol){
                return this.matrix.get(indexRow,indexCol);
            }
            public double getValueByCoor(int x,int y){
                return this.matrix.get(y,x);
        }

            public boolean isNeighborHasLevel(int x,int y,int level){
                int left=x>0?(int)getValueByCoor(x-1,y):999;
                int right=x<getMaxX()-1?(int)getValueByCoor(x+1,y):999;
                int up=y>0?(int)getValueByCoor(x,y-1):999;
                int down=y<getMaxY()-1?(int)getValueByCoor(x,y+1):999;

                if(left==level || right==level || up==level || down==level){
                    return true;
                }
                else{
                    return false;
                }

            }
            public MapArray copyMapArray(){
                MapArray mapArray=new MapArray();
                mapArray.matrix=this.matrix.copy();
                return mapArray;

            }

        }

        public MapArray run(){
            ArrayList<Point> initPoints=storePointsMovePointsToZeroPlusSomeWidthPosition(rawPoints);
            MapArray mapArray=interpolationPointsGetMapArry(initPoints);
            //mapArray.matrix.print(5,0);
            //System.out.println("=========================");
            //draw(this.rawPoints);
            MapArray filledOutsideMapArray=fillOutsideOfMapArrayWithLevel(mapArray,MapArray.FILLED_LEVEL2);
            //filledOutsideMapArray.matrix.print(5,0);

            fillTheInnerPathWithLevel3(filledOutsideMapArray,mapArray);

            makeLevel3ToLevel2(filledOutsideMapArray);
            MapArray mapEdge=findTheEdge(filledOutsideMapArray);
            return mapEdge;

        }

        public MapArray findTheEdge(MapArray mapArray){
            MapArray mapEdge=new MapArray();
            for(int x=0;x<mapArray.getMaxX();x++){
                for(int y=0;y<mapArray.getMaxY();y++) {
                    if(mapArray.getValueByCoor(x,y)==MapArray.FILLED_LEVEL2 && (mapArray.isNeighborHasLevel(x,y,MapArray.FILLED)||mapArray.isNeighborHasLevel(x,y,MapArray.EMPTY))){
                        mapEdge.fillMapArrayByCoor(x,y,MapArray.FILLED);
                    }
                    }
                }
                return mapEdge;
        }

        public void makeLevel3ToLevel2(MapArray mapArray){
            for(int x=0;x<mapArray.getMaxX();x++){
                for(int y=0;y<mapArray.getMaxY();y++){
                    if(mapArray.getValueByCoor(x,y)==MapArray.FILLED_LEVEL3){
                        mapArray.fillMapArrayByCoor(x,y,MapArray.FILLED_LEVEL2);
                    }
                }
            }
            }


        public void fillTheInnerPathWithLevel3(MapArray mapArrayWithLevel2,MapArray refMapArrayWithLevel1){
            for(int x=1;x<mapArrayWithLevel2.getMaxX();x++){
                for(int y=1;y<mapArrayWithLevel2.getMaxY();y++){
                    if(refMapArrayWithLevel1.getValueByCoor(x,y)==MapArray.FILLED && mapArrayWithLevel2.isNeighborHasLevel(x,y,MapArray.FILLED_LEVEL2)){
                        mapArrayWithLevel2.fillMapArrayByCoor(x,y,MapArray.FILLED_LEVEL3);
                    }
                }
            }
        }

        public MapArray fillOutsideOfMapArrayWithLevel(MapArray mapArray,int level){

            for(int i=0;i<mapArray.getRow();i++){
                mapArray.fillMapArrayByIndex(i,0,level);
                mapArray.fillMapArrayByIndex(i,mapArray.getCol()-1,level);
            }
            for(int i=0;i<mapArray.getCol();i++){
                mapArray.fillMapArrayByIndex(0,i,level);
                mapArray.fillMapArrayByIndex(mapArray.getRow()-1,i,level);
            }
            MapArray newMapArray=mapArray.copyMapArray();

            for(int x=0;x<mapArray.getMaxX();x++){
                for(int y=0;y<mapArray.getMaxY();y++){
                    if(newMapArray.getValueByCoor(x,y)==MapArray.EMPTY && newMapArray.isNeighborHasLevel(x,y,level)){
                        newMapArray.fillMapArrayByCoor(x,y,level);
                    }
                    int otherSideY=newMapArray.getMaxY()-y-1;
                    if(newMapArray.getValueByCoor(x,otherSideY)==MapArray.EMPTY && newMapArray.isNeighborHasLevel(x,otherSideY,level)){
                        newMapArray.fillMapArrayByCoor(x,otherSideY,level);
                    }


                }
            }
            return newMapArray;
        }
        public  MapArray interpolationPointsGetMapArry(ArrayList<Point> points){
            MapArray mapArray=new MapArray();
            int x=(int)round(points.get(0).x/gridWidth);
            int y=(int)round(points.get(0).y/gridWidth);
            mapArray.fillMapArrayByCoor(x,y,MapArray.FILLED);
            for(int i =1;i<points.size();i++){
                double dis=points.get(i).distanceToPoint(points.get(i-1));
                int n=(int)ceil(dis/this.gridWidth);
                for(int j=0;j<n;j++){
                    double xtmp=(points.get(i).x-points.get(i-1).x)*j/n+points.get(i-1).x;
                    double ytmp=(points.get(i).y-points.get(i-1).y)*j/n+points.get(i-1).y;
                    int coorX=(int)round(xtmp/gridWidth);
                    int coorY=(int)round(ytmp/gridWidth);
                    mapArray.fillMapArrayByCoor(coorX,coorY,MapArray.FILLED);
                }
            }

            return mapArray;
        }



    }

    public static ArrayList<Point> getSortedCornerPointsFromSimpleMatrix(Matrix matrix,double width){
        ArrayList<MapPath.Point> p2 = findCornersPoints(matrix, width);

        //排序
        ArrayList<MapPath.Point> p2c = sortCornerPointIntoPath(p2);
        return p2c;

    }
    public  ArrayList<ArrayList> getSortedCornerPointsList(double width){
        ArrayList<Matrix> matrices=getSimleMatricesAfterSeg();
        ArrayList<ArrayList> points=new ArrayList<>();
        for(Matrix matrix:matrices){
            ArrayList<MapPath.Point> p=getSortedCornerPointsFromSimpleMatrix(matrix,width);
            points.add(p);
        }
        return points;
    }
    public  ArrayList<Matrix> getSimleMatricesAfterSeg(){
        return vertex.matrixArrayListPath;
    }

    public  ArrayList<ArrayList> runMethodGetCornerList() throws NoSuchFieldException, IllegalAccessException {

        ArrayList<MapPath.Point> points1Corner2= this.getVertexPoints();
        Matrix newMatrix=arrayListPointToMatrix(points1Corner2);
        newMatrix=changeMatrixDirection(newMatrix,CLOCWISE);
        vertex.divideTheMatrix(newMatrix);
        ArrayList<ArrayList> cornerpoints=getSortedCornerPointsList(this.width);
        return cornerpoints;

    }
    public static ArrayList<Point> fineTheRawInputPoints(ArrayList<Point> points){
        MapPath.PointsTreat pt=new MapPath.PointsTreat(points);
        MapPath.PointsTreat.MapArray mapArray=pt.run();
        ArrayList<MapPath.Point> newMappoints=pt.restorePointsFromMapArry(mapArray);
        return newMappoints;
    }


    }

