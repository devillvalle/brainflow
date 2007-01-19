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

public class Dimension3D<T extends Number> implements java.io.Serializable {


    public T x;
    public T y;
    public T z;


    public Dimension3D(Dimension3D<T> dim) {
        x = dim.x;
        y = dim.y;
        z = dim.z;
    }


    public Dimension3D(T[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Dimension3D: supplied array must have length = 3!");
        }
        x = pt[0];
        y = pt[1];
        z = pt[2];
    }

    public Dimension3D(T _x, T _y, T _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }


}