package com.brainflow.utils;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Point3D implements java.io.Serializable {

    public double x;
    public double y;
    public double z;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);


    public Point3D() {
    }

    public Point3D(Point3D pt) {
        x = pt.x;
        y = pt.y;
        z = pt.z;
    }


    public Point3D(float[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Point3D: supplied array must have length = 3!");
        }
        x = pt[0];
        y = pt[1];
        z = pt[2];
    }

    public Point3D(int[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Point3D: supplied array must have length = 3!");
        }
        x = pt[0];
        y = pt[1];
        z = pt[2];
    }

    public Point3D(double[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Point3D: supplied array must have length = 3!");
        }
        x = pt[0];
        y = pt[1];
        z = pt[2];
    }

    public Point3D(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }


    public static double distance(Point3D one, Point3D two) {
        return (one.x - two.x) * (one.x - two.x) + (one.y - two.y) * (one.y - two.y) +
                (one.z - two.z) * (one.z - two.z);
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        double oldProperty = this.z;
        this.z = z;
        changeSupport.firePropertyChange("one", oldProperty, this.z);

    }

    public void setY(double y) {
        double oldProperty = this.y;
        this.y = y;
        changeSupport.firePropertyChange("zero", oldProperty, this.y);


    }

    public void setX(double x) {
        double oldProperty = this.x;
        this.x = x;
        changeSupport.firePropertyChange("zero", oldProperty, this.x);


    }

    public String toString() {
        return "x = " + x + " y = " + y + " z = " + z;
    }


}