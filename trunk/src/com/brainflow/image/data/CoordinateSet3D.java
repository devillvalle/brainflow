package com.brainflow.image.data;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.space.ImageSpace3D;

import java.util.Arrays;

import cern.colt.list.DoubleArrayList;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 14, 2007
 * Time: 5:22:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateSet3D {

    private DoubleMatrix2D points;

    private DoubleArrayList values;

    private DoubleArrayList pointSize;

    private ImageSpace3D space;


    public CoordinateSet3D(double[][] points) {
        if (points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (x, y, z)");
        }

    }


    public CoordinateSet3D(ImageSpace3D _space, double[][] _points) {
        space = _space;

        if (_points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (x, y, z)");
        }

        init(_points);
    }

    public CoordinateSet3D(ImageSpace3D _space, double[][] _points, double fillValue, double fillSize) {
        space = _space;

        if (_points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (x, y, z)");
        }

        init(_points, fillValue, fillSize);
    }

    public CoordinateSet3D(ImageSpace3D _space, double[][] _points, double[] _values, double[] _sizes) {
        space = _space;

        if (_points[0].length != 3) {
            throw new IllegalArgumentException("points array must have 3 columns (x, y, z)");
        }

        if ( (_sizes.length != _points.length) || (_values.length != _points.length) ) {
            throw new IllegalArgumentException("values and size arrays must have same number of rows as points array");

        }


        init(_points, _values, _sizes);
    }

    private void init(double[][] _points, double fillValue, double fillSize) {
        points = new DenseDoubleMatrix2D(_points);
        double[] v = new double[_points.length];
        double[] s = new double[_points.length];
        Arrays.fill(v, fillValue);
        Arrays.fill(s, fillSize);
        values = new DoubleArrayList(v);
        pointSize = new DoubleArrayList(s);
    }

    private void init(double[][] _points) {
        points = new DenseDoubleMatrix2D(_points);
        double[] v = new double[_points.length];
        double[] s = new double[_points.length];
        Arrays.fill(v, 1);
        Arrays.fill(s, 1);
        values = new DoubleArrayList(v);
        pointSize = new DoubleArrayList(s);
    }

    private void init(double[][] _points, double[] _values, double[] _sizes) {
        points = new DenseDoubleMatrix2D(_points);
        values = new DoubleArrayList(_values);
        pointSize = new DoubleArrayList(_sizes);
    }


    public int getRows() {
        return points.rows();
    }

    public int getColumns() {
        return 5;
    }

    public double getMinValue() {
        return cern.jet.stat.Descriptive.min(values);
    }

    public double getMaxValue() {
        return cern.jet.stat.Descriptive.max(values);
    }


    public void setCoordinate(int index, double x, double y, double z) {
        points.set(index, 0, x);
        points.set(index, 1, y);
        points.set(index, 2, z);

    }

     public void setXCoordinate(int index, double x) {
        points.set(index, 0, x);

    }

     public void setYCoordinate(int index,double y) {
        points.set(index, 1, y);
    }

     public void setZCoordinate(int index, double z) {
        points.set(index, 2, z);

    }

    public void setValue(int index, double value) {
        values.set(index, value);
    }

    public void setRadius(int index, double value) {
        pointSize.set(index, value);
    }

    public double getValue(int index) {
        return values.get(index);
    }

    public double getRadius(int index) {
        return pointSize.get(index);
    }

    public double getXCoordinate(int index) {
        return points.get(index, 0);

    }

     public double getYCoordinate(int index) {
        return points.get(index, 1);
    }

     public double getZCoordinate(int index) {
        return points.get(index, 2);

    }

    public ImageSpace3D getSpace() {
        return space;
    }

    public double[] getCoordinate(int index) {
        if (index < 0 || index >= getRows()) {
            throw new IndexOutOfBoundsException("illegal index " + index);
        }

        double[] ret = new double[3];
        ret[0] = points.get(index,0);
        ret[1] = points.get(index,1);
        ret[2] = points.get(index,2);

        return ret;

    }

    public AnatomicalPoint3D getAnatomicalPoint(int index) {
        if (index < 0 || index >= getRows()) {
            throw new IndexOutOfBoundsException("illegal index " + index);
        }


        return new AnatomicalPoint3D((Anatomy3D)space.getAnatomy(), points.get(index,0), points.get(index,1), points.get(index,2));

    }

   


}