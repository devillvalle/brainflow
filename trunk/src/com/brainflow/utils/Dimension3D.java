package com.brainflow.utils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Dimension3D<T extends Number> implements IDimension, java.io.Serializable {


    public T zero;

    public T one;

    public T two;


    public Dimension3D(Dimension3D<T> dim) {
        zero = dim.zero;
        one = dim.one;
        two = dim.two;
    }


    public Dimension3D(T[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Dimension3D: supplied array must have length = 3!");
        }
        zero = pt[0];
        one = pt[1];
        two = pt[2];
    }

    public Dimension3D(T _zero, T _one, T _two) {
        zero = _zero;
        one = _one;
        two = _two;
    }

    public T getDim(int dimnum) {
        if (dimnum == 0) return zero;
        if (dimnum == 1) return one;
        if (dimnum == 2) return two;

        throw new IllegalArgumentException("illegal dimension number " + dimnum + " for class " + getClass());
    }

    public int product() {
        return zero.intValue() * one.intValue() * two.intValue();
    }

    public int numDim() {
        return 3;
    }

    public String toString() {
        return "[" + zero + ", " + one + ", " + two + "]";
    }


}