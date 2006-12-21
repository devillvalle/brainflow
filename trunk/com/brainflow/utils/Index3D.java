package com.brainflow.utils;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import java.beans.PropertyChangeListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Index3D {

    private int x;
    private int y;
    private int z;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public Index3D() {
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public Index3D(Index3D idx) {

        x = idx.getX();
        y = idx.getY();
        z = idx.getZ();
    }

    public Index3D(int[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Index3D: supplied array must have length = 3!");
        }
        x = pt[0];
        y = pt[1];
        z = pt[2];
    }

    public Index3D(double[] pt) {
        if (pt.length != 3) {
            throw new IllegalArgumentException("Index3D: supplied array must have length = 3!");
        }
        x = (int) pt[0];
        y = (int) pt[1];
        z = (int) pt[2];
    }

    public Index3D(int _x, int _y, int _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        int oldProperty = this.x;
        this.x = x;
        changeSupport.firePropertyChange("x", oldProperty, this.x);

    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        int oldProperty = this.y;
        this.y = y;
        changeSupport.firePropertyChange("y", oldProperty, this.y);

    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        int oldProperty = this.z;
        this.z = z;
        changeSupport.firePropertyChange("z", oldProperty, this.z);

    }


    public int[] toArray() {
        return new int[]{x, y, z};
    }


    public String toString() {
        return "x = " + x + " y = " + y + " z = " + z;
    }


}