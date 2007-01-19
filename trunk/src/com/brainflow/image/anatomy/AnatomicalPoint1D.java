package com.brainflow.image.anatomy;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.brainflow.image.axis.ImageAxis;

import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 12:31:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnatomicalPoint1D implements AnatomicalPoint {


    private AnatomicalAxis anatomy;
    private double x;
    private PropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public AnatomicalPoint1D(AnatomicalAxis _anatomy, double x) {
        this.x=x;
        anatomy=_anatomy;
    }

    public AnatomicalAxis getAnatomy() {
        return anatomy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        double oldProperty = this.x;
        this.x = x;
        changeSupport.firePropertyChange("x", oldProperty, this.x);
    }


    public int getNumDimensions() {
        return 1;
    }

    public AnatomicalPoint1D mirrorPoint(ImageAxis otherAxis) {
        assert otherAxis.getAnatomicalAxis().sameAxis(anatomy) : "other axis cannot be orthogonal to this point";

        if (otherAxis.getAnatomicalAxis() == anatomy) {
            //a copy
            return new AnatomicalPoint1D(anatomy, getX());
        } else {
            double nvalue = otherAxis.getRange().getEnd().getX() - getX()
                    +  otherAxis.getRange().getBeginning().getX();

            return new AnatomicalPoint1D(otherAxis.getAnatomicalAxis(), nvalue);
        }


    }

    public double getValue(int axisNum) {
        assert axisNum == 0;

        if (axisNum == 0) {
            return getX();
        }

        throw new AssertionError();
    }


    public String toString() {
        return "[" + anatomy.toString() + "]" + getX();
    }
}
