package com.hanyangtech;


import java.util.ArrayList;
import Jama.Matrix;

import static com.hanyangtech.MatrixFunc.DIRECTION.*;
import static java.lang.Math.round;

public class MatrixFunc {
    public static double maxInMatrix(Matrix matrix) {
        double maxData = matrix.get(0, 0);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double data = matrix.get(i, j);
                maxData = maxData > data ? maxData : data;
            }
        }
        return maxData;
    }

    public static int minValueIndexInMatrix(Matrix matrix) {
        double minData = matrix.get(1, 0);
        int index = 0;
        for (int j = 0; j < matrix.getColumnDimension(); j++) {
            double data = matrix.get(1, j);
            if (data < minData) {
                minData = data;
                index = j;
            }

        }

        return index;
    }

    public static double minInMatrix(Matrix matrix) {
        double minData = matrix.get(0, 0);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double data = matrix.get(i, j);
                minData = minData < data ? minData : data;
            }
        }
        return minData;
    }

    public static double[] maxXYInArrayListPoints(ArrayList<MapPath.Point> points) {
        double maxDataX = points.get(0).x;
        double maxDataY = points.get(0).y;
        for (MapPath.Point point : points) {
            maxDataX = maxDataX > point.x ? maxDataX : point.x;
            maxDataY = maxDataY > point.y ? maxDataY : point.y;
        }
        return new double[]{maxDataX, maxDataY};
    }

    public static ArrayList<MapPath.Point> transposePointsArrayList(ArrayList<MapPath.Point> points) {
        ArrayList<MapPath.Point> newPoints = new ArrayList<>();
        for (MapPath.Point point : points) {
            newPoints.add(new MapPath.Point(point.y, point.x));

        }
        return newPoints;
    }

    public static ArrayList<MapPath.Point> pointsArraryListTimes(ArrayList<MapPath.Point> points, double num) {
        ArrayList<MapPath.Point> newPoints = new ArrayList<>();
        for (MapPath.Point point : points) {
            newPoints.add(new MapPath.Point(point.y * num, point.x * num));
        }
        return newPoints;
    }

    public static ArrayList<MapPath.Point> pointsArrayListMakeInt(ArrayList<MapPath.Point> points) {
        ArrayList<MapPath.Point> newPoints = new ArrayList<>();
        for (MapPath.Point point : points) {
            newPoints.add(new MapPath.Point((int) round(point.x), (int) round(point.y)));
        }
        return newPoints;
    }

    public static double[] minXYInArrayListPoints(ArrayList<MapPath.Point> points) {
        double minDataX = points.get(0).x;
        double minDataY = points.get(0).y;
        for (MapPath.Point point : points) {
            minDataX = minDataX < point.x ? minDataX : point.x;
            minDataY = minDataY < point.y ? minDataY : point.y;

        }
        return new double[]{minDataX, minDataY};
    }

    public static double minYInArrayListPoints(ArrayList<MapPath.Point> points) {
        double minData = points.get(0).x;
        for (MapPath.Point point : points) {
            minData = minData < point.x ? minData : point.x;
        }
        return minData;
    }

    public static void makePointsListClose(ArrayList<MapPath.Point> points) {
        if (points.get(0).x != points.get(points.size() - 1).x || points.get(0).y != points.get(points.size() - 1).y) {
            points.add(points.get(0));
        }
    }

    public static Matrix makeMatrixClose(Matrix matrix) {
        ArrayList<MapPath.Point> points = matrixToArrayList(matrix);
        makePointsListClose(points);
        return arrayListPointToMatrix(points);

    }


    public static MapPath.Point makeAPointByIndex(Matrix matrix, int index) {
        return new MapPath.Point(matrix.get(0, index), matrix.get(1, index));
    }

    public static Matrix arrayListPointToMatrix(ArrayList<MapPath.Point> points) {
        double[][] pointsArray = new double[2][points.size()];
        for (int i = 0; i < points.size(); i++) {
            pointsArray[0][i] = points.get(i).x;
            pointsArray[1][i] = points.get(i).y;
        }
        return new Matrix(pointsArray);
    }

    public static ArrayList<MapPath.Point> matrixToArrayList(Matrix matrix) {
        ArrayList<MapPath.Point> temp = new ArrayList<>();
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            temp.add(makeAPointByIndex(matrix, i));
        }
        return temp;
    }

    public static ArrayList<Matrix> splitMatrixByIndex(Matrix matrix, int index) {
        ArrayList<Matrix> returnMatrix = new ArrayList<>();
        Matrix leftPart = new Matrix(0, 0);
        Matrix rightPart = new Matrix(0, 0);

        double minX = minInMatrix(matrix.getMatrix(0, 0, 0, matrix.getColumnDimension() - 1));
        double maxX = maxInMatrix(matrix.getMatrix(0, 0, 0, matrix.getColumnDimension() - 1));
        double curX = matrix.get(0, index);
        if (minX == curX) {
            rightPart = matrix;
        } else if (maxX == curX) {
            leftPart = matrix;
        } else {
            leftPart = matrix.getMatrix(0, 1, 0, index);
            rightPart = matrix.getMatrix(0, 1, index, matrix.getColumnDimension() - 1);
        }

        returnMatrix.add(leftPart);
        returnMatrix.add(rightPart);
        return returnMatrix;

    }

    public static <T> void extendFirstArrayBySecondObject(ArrayList<T> one, ArrayList<T> two) {
        if (two.size() > 0) {
            for (T t : two) {
                one.add(t);
            }
        }
    }

    public static double[][] matrixToArray(Matrix matrix) {
        double[][] data = new double[matrix.getRowDimension()][matrix.getColumnDimension()];
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                data[i][j] = matrix.get(i, j);
            }
        }
        return data;
    }

    public static ArrayList<MapPath.Point> tranlateArrayListPoints(ArrayList<MapPath.Point> points, MapPath.Point refPoint) {
        ArrayList<MapPath.Point> newPoints = new ArrayList<>();
        for (MapPath.Point point : points) {
            newPoints.add(new MapPath.Point(point.x + refPoint.x, point.y + refPoint.y));
        }
        return newPoints;
    }

    public static void refineArrayListPoints(ArrayList<MapPath.Point> points) { //remove nearby same point
        if (points.size() > 2) {
            int curIndex = 1;
            while (curIndex < points.size())
                if (points.get(curIndex).x == points.get(curIndex - 1).x && points.get(curIndex).y == points.get(curIndex - 1).y) {
                    points.remove(curIndex);
                } else {
                    curIndex++;
                }
        }
    }

    public static Matrix sortMatricMakeCurrentPointAsFirst(Matrix matrix, int index) {
        Matrix newMatrix;
        ArrayList<MapPath.Point> points = new ArrayList<>();
        for (int j = index; j < matrix.getColumnDimension(); j++) {
            points.add(new MapPath.Point(matrix.get(0, j), matrix.get(1, j)));
        }

        for (int j = 0; j < index; j++) {
            points.add(new MapPath.Point(matrix.get(0, j), matrix.get(1, j)));
        }

        points.add(points.get(0));
        refineArrayListPoints(points);
        newMatrix = arrayListPointToMatrix(points);
        return newMatrix;
    }

    public enum COMPARE {
        BIGGER, EQUAL, SMALLER
    }

    public static Matrix[] splitMatrixByCurrentPointAsLeftRightPart(Matrix matrix, int index) {
        Matrix sortMatrix = sortMatricMakeCurrentPointAsFirst(matrix, index);
        Matrix[] matrices = new Matrix[2]; //0 is left.1 is right
        COMPARE compare = COMPARE.EQUAL;
        int getIndex = 0;
        for (int i = 1; i < sortMatrix.getColumnDimension(); i++) {
            double result = sortMatrix.get(0, i) - sortMatrix.get(0, i - 1);
            if (result == 0) {
                compare = COMPARE.EQUAL;
                continue;
            } else if (result > 0) {
                compare = COMPARE.BIGGER;
                for (int j = i + 1; j < sortMatrix.getColumnDimension(); j++) {
                    double result2 = sortMatrix.get(0, j) - sortMatrix.get(0, j - 1);
                    if (result2 < 0) {
                        getIndex = j;
                        break;
                    }
                }
                break;
            } else {
                compare = COMPARE.SMALLER;
                for (int j = i + 1; j < sortMatrix.getColumnDimension(); j++) {
                    double result2 = sortMatrix.get(0, j) - sortMatrix.get(0, j - 1);
                    if (result2 > 0) {
                        getIndex = j;
                        break;
                    }
                }
                break;
            }

        }

        Matrix one = matrix.getMatrix(0, 1, 0, index);
        Matrix two = matrix.getMatrix(0, 1, index, matrix.getColumnDimension() - 1);

        if (compare == COMPARE.SMALLER) {
            matrices[0] = two;
            matrices[1] = one;
        } else {
            matrices[0] = one;
            matrices[1] = two;
        }
        return matrices;

    }

    public enum DIRECTION {
        CLOCWISE, ANTICLOCK, OTHER
    }

    public static DIRECTION getMatrixDirection(Matrix matrix) {
        double initValue = matrix.get(1, 0);
        DIRECTION direction = OTHER;
        int minIndex = minValueIndexInMatrix(matrix);
        double curX = matrix.get(0, minIndex);
        if (minIndex < matrix.getColumnDimension() - 1) {
            double nextX = matrix.get(0, minIndex + 1);
            if (nextX > curX) {
                direction = ANTICLOCK;
            } else {
                direction = CLOCWISE;
            }
        } else {
            double preX = matrix.get(0, minIndex - 1);
            direction = preX < curX ? CLOCWISE : ANTICLOCK;
        }
        return direction;
    }

    public static ArrayList<MapPath.Point> reverseArrayList(ArrayList<MapPath.Point> points) {
        ArrayList<MapPath.Point> newPoints = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            newPoints.add(points.get(i));
        }
        return newPoints;
    }

    public static Matrix changeMatrixDirection(Matrix matrix, DIRECTION direction) {
        DIRECTION curDirection = getMatrixDirection(matrix);
        Matrix result = matrix.copy();
        if (curDirection != direction) {
            ArrayList<MapPath.Point> rp = reverseArrayList(matrixToArrayList(matrix));
            result = arrayListPointToMatrix(rp);
        }
        return result;

    }

    public static Matrix getMatrixByIndexRange(Matrix matrix, int start, int end) {
        if (end >= start) {
            return matrix.getMatrix(0, 1, start, end);
        } else {
            Matrix newM = sortMatricMakeCurrentPointAsFirst(matrix, start);
            return newM.getMatrix(0, 1, 0, matrix.getColumnDimension() - start + end - 1);
        }
    }

    public static Matrix addStartEndPointForMatrix(Matrix matrix, MapPath.Point startPoint, MapPath.Point endPoint) {
        ArrayList<MapPath.Point> result = new ArrayList<>();
        ArrayList<MapPath.Point> origin = matrixToArrayList(matrix);
        result.add(startPoint);
        extendFirstArrayBySecondObject(result, origin);
        result.add(endPoint);
        return arrayListPointToMatrix(result);
    }

    public static void draw(Matrix matrix) {
        /*
        ArrayList<MapPath.Point> points=matrixToArrayList(matrix);
        Plot2DPanel plot2 = new Plot2DPanel();
        double[] ax= new double[0];
        double[] ay=new double[0];
        try {
            ax = getOneAxisFromPoints(points,"x");
            ay=getOneAxisFromPoints(points,"y");
            plot2.addLinePlot("my plot", ax, ay);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        JFrame frame2 = new JFrame("a plot panel");
        frame2.setContentPane(plot2);
        frame2.setVisible(true);
*/
    }

    public static void draw(ArrayList<MapPath.Point> points) {
        draw(arrayListPointToMatrix(points));
    }

    private static void swapArrayListElement(ArrayList<MapPath.Point> points, int i, int j) {
        MapPath.Point temp = points.get(i);
        points.set(i, points.get(j));
        points.set(j, temp);
    }

    public static void sortArrayListPointsByXvalue(ArrayList<MapPath.Point> points) { //bubblesort
        for (int j = 0; j < points.size(); j++) {
            for (int i = 0; i < points.size() - 1 - j; i++) {
                if (points.get(i).x > points.get(i + 1).x) {
                    swapArrayListElement(points, i, i + 1);
                }
            }
        }

    }

    public static void draw(MapPath.PointsTreat.MapArray mapArray) {
       /*
        ArrayList<MapPath.Point> points=matrixToArrayList(mapArray.matrix);
        Plot2DPanel plot2 = new Plot2DPanel();

        //plot2.addScatterPlot("a",)

        JFrame frame2 = new JFrame("a plot panel");
        frame2.setContentPane(plot2);
        frame2.setVisible(true);
    }*/
    }
}