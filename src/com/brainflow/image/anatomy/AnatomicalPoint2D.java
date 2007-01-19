package com.brainflow.image.anatomy;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 12:29:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnatomicalPoint2D implements AnatomicalPoint {

    private AnatomicalPlane anatomy;

    private double x;
    private double y;


    private PropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public AnatomicalPoint2D(AnatomicalPlane _anatomy, double x, double y) {
        this.x = x;
        this.y = y;

        anatomy = _anatomy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        double oldProperty = this.x;
        this.x = x;
        changeSupport.firePropertyChange("x", oldProperty, this.x);

    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        double oldProperty = this.y;
        this.y = y;
        changeSupport.firePropertyChange("y", oldProperty, this.y);
    }

    public AnatomicalPlane getAnatomy() {
        return anatomy;
    }

    public int getNumDimensions() {
        return 2;
    }

    public double getValue(int axisNum) {
        assert axisNum == 0 | axisNum == 1;

        if (axisNum == 0) {
            return getX();
        } else if (axisNum == 1) {
            return getY();
        }

        else throw new AssertionError();

    }


}
