package com.brainflow.image.anatomy;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 22, 2004
 * Time: 2:48:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnatomicalPoint3D implements AnatomicalPoint {

    public static final String X_PROPERTY = "x";
    public static final String Y_PROPERTY = "y";
    public static final String Z_PROPERTY = "z";


    private AnatomicalVolume anatomy;

    private double x;
    private double y;
    private double z;

    private PropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public AnatomicalPoint3D(AnatomicalVolume _anatomy, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        anatomy = _anatomy;
    }

    public AnatomicalVolume getAnatomy() {
        return anatomy;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }


    public AnatomicalPoint1D getValue(AnatomicalAxis axis) {
        if (axis.sameAxis(anatomy.XAXIS)) {
            return new AnatomicalPoint1D(axis, anatomy.XAXIS.convertValue(axis, x));
        } else if (axis.sameAxis(anatomy.YAXIS)) {
            return new AnatomicalPoint1D(axis, anatomy.YAXIS.convertValue(axis, y));
        } else if (axis.sameAxis(anatomy.ZAXIS)) {
            return new AnatomicalPoint1D(axis, anatomy.ZAXIS.convertValue(axis, z));
        } else {
            throw new AssertionError();
        }


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

    public void setX(double value) {
        double oldValue = getX();
        x = value;
        changeSupport.firePropertyChange("x", oldValue, value);
    }

    public void setY(double value) {
        double oldValue = getY();
        y = value;
        changeSupport.firePropertyChange("y", oldValue, value);
    }

    public void setZ(double value) {
        double oldValue = getZ();
        z = value;
        changeSupport.firePropertyChange("z", oldValue, value);
    }


    public int getNumDimensions() {
        return 3;
    }

    public double getValue(int axisNum) {
        assert axisNum == 0 | axisNum == 1;

        if (axisNum == 0) {
            return getX();
        } else if (axisNum == 1) {
            return getY();
        } else if (axisNum == 2) {
            return getZ();
        } else throw new AssertionError();

    }


    public String toString() {
        return "AnatomicalPoint3D{" +
                "anatomy=" + anatomy +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
