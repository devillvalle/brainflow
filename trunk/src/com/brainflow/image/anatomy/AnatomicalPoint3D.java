package com.brainflow.image.anatomy;

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


    private Anatomy3D anatomy;

    private double x;
    private double y;
    private double z;

    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public AnatomicalPoint3D(Anatomy3D _anatomy, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        anatomy = _anatomy;
    }

    public Anatomy3D getAnatomy() {
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
  
        if (axisNum == 0) {
            return getX();
        } else if (axisNum == 1) {
            return getY();
        } else if (axisNum == 2) {
            return getZ();
        } else throw new IllegalArgumentException("illegal axis number " + axisNum);

    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnatomicalPoint3D that = (AnatomicalPoint3D) o;

        if (Double.compare(that.x, x) != 0) return false;
        if (Double.compare(that.y, y) != 0) return false;
        if (Double.compare(that.z, z) != 0) return false;
        if (anatomy != null ? !anatomy.equals(that.anatomy) : that.anatomy != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        long temp;
        result = (anatomy != null ? anatomy.hashCode() : 0);
        temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = z != +0.0d ? Double.doubleToLongBits(z) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(anatomy.XAXIS).append("-").append(anatomy.YAXIS).append("-").append(anatomy.ZAXIS);
        sb.append("X: ").append(getX()).append(" Y: ").append(getY()).append(" Z: ").append(getZ());
        return sb.toString();
    }
}
