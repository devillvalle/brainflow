package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalDirection;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.axis.AxisRange;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 17, 2004
 * Time: 4:50:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageSpace implements IImageSpace {


    private ImageAxis[] axes = new ImageAxis[0];

    private Anatomy anatomy = null;


    void createImageAxes(int num) {
        axes = new ImageAxis[num];
    }

    void initAxis(ImageAxis iaxis, Axis aaxis) {
        axes[aaxis.getId()] = iaxis;
    }


    public void anchorAxis(AnatomicalAxis aaxis, AnatomicalDirection adir, double value) {
        ImageAxis iaxis = getImageAxis(aaxis, true);
        iaxis.anchorAxis(adir, value);
    }

    public int[] getDimensionVector() {
        int[] dvec = new int[axes.length];
        for (int i = 0; i < dvec.length; i++) {
            dvec[i] = axes[i].getNumSamples();
        }

        return dvec;
    }

    public int getDimension(Axis axis) {
        assert axis.getId() < getNumDimensions();
        return axes[axis.getId()].getNumSamples();

    }

    public double getSpacing(Axis axis) {
        assert axis.getId() < getNumDimensions();
        return axes[axis.getId()].getSpacing();
    }

    public double getExtent(Axis axis) {
        assert axis.getId() < getNumDimensions();
        return axes[axis.getId()].getExtent();
    }

    private ImageAxis whichAxis(AnatomicalAxis aaxis) {
        for (int i = 0; i < axes.length; i++) {
            if (aaxis == axes[i].getAnatomicalAxis()) {
                return axes[i];
            }
        }

        return null;
    }

    private ImageAxis whichAxisIgnoreDirection(AnatomicalAxis aaxis) {

        for (int i = 0; i < axes.length; i++) {

            if (aaxis == axes[i].getAnatomicalAxis()) {

                return axes[i];
            } else if (aaxis == axes[i].getAnatomicalAxis().getFlippedAxis()) {

                return axes[i];
            }
        }

        return null;
    }

    public int getDimension(AnatomicalAxis axis) {
        ImageAxis iaxis = whichAxisIgnoreDirection(axis);
        if (iaxis == null) {
            throw new IllegalArgumentException("AbstractImageSpace.getDimension(...): request for dimension" +
                    " of AnatomicalAxis that is not represented in this space.");
        }

        return iaxis.getNumSamples();


    }

    public double getSpacing(AnatomicalAxis axis) {
        ImageAxis iaxis = whichAxis(axis);
        if (iaxis == null) {
            throw new IllegalArgumentException("AbstractImageSpace.getSpacing(...): request for spacing" +
                    " of AnatomicalAxis that is not represented in this space.");
        }

        return iaxis.getSpacing();

    }

    public double getExtent(AnatomicalAxis axis) {
        ImageAxis iaxis = whichAxis(axis);
        if (iaxis == null) {
            throw new IllegalArgumentException("AbstractImageSpace.getExtent(...): request for extent" +
                    " of AnatomicalAxis that is not represented in this space.");
        }

        return iaxis.getExtent();

    }


    public ImageAxis getImageAxis(Axis axis) {
        assert axis.getId() < getNumDimensions();
        return axes[axis.getId()];
    }

    public ImageAxis getImageAxis(AnatomicalAxis axis, boolean ignoreDirection) {
        ImageAxis iaxis = null;
        if (ignoreDirection)
            iaxis = whichAxisIgnoreDirection(axis);
        else
            iaxis = whichAxis(axis);

        if (iaxis == null) {
            throw new IllegalArgumentException("AbstractImageSpace.getImageAxis(...): request for ImageAxis for " +
                    "AnatomicalAxis that is not represented in this space.");
        }

        return iaxis;

    }

    public AnatomicalAxis getAnatomicalAxis(Axis axis) {
        return axes[axis.getId()].getAnatomicalAxis();
    }

    public Axis findAxis(AnatomicalAxis axis) {
        if ((axes[0].getAnatomicalAxis() == axis) || (axes[0].getAnatomicalAxis().getFlippedAxis() == axis))
            return Axis.X_AXIS;
        else if (axes[1].getAnatomicalAxis() == axis || (axes[1].getAnatomicalAxis().getFlippedAxis() == axis))
            return Axis.Y_AXIS;
        else if (axes[2].getAnatomicalAxis() == axis || (axes[2].getAnatomicalAxis().getFlippedAxis() == axis))
            return Axis.Z_AXIS;

        return null;
    }

    public boolean sameAxes(IImageSpace other) {
        if (other.getNumDimensions() != getNumDimensions()) {
            return false;
        }
        for (int i = 0; i < getNumDimensions(); i++) {
            System.out.println("checking dim : " + i);
            if (getAnatomicalAxis(Axis.getAxis(i)) != other.getAnatomicalAxis(Axis.getAxis(i))) {
                return false;
            }

        }

        return true;
    }

    public IImageSpace union(IImageSpace other) {
        assert sameAxes(other) : "cannot perform union for ImageSpaces with different axis orientations";
        if (!sameAxes(other)) {
            throw new IllegalArgumentException("cannot perform union for ImageSpaces with different axis orientations");
        }

        System.out.println("these are same axes, proceeding");

        ImageAxis[] axes = new ImageAxis[getNumDimensions()];

        for (int i = 0; i < axes.length; i++) {
            AxisRange range1 = other.getImageAxis(Axis.getAxis(i)).getRange();
            AxisRange range2 = getImageAxis(Axis.getAxis(i)).getRange();


            AxisRange nrange = range1.union(range2);
            axes[i] = new ImageAxis(nrange,
                    Math.min(other.getSpacing(Axis.getAxis(i)), getSpacing(Axis.getAxis(i))));

         
        }

        return SpaceFactory.createImageSpace(axes);

    }

    void setAnatomy(Anatomy _anatomy) {
        anatomy = _anatomy;
    }

    public Anatomy getAnatomy() {
        return anatomy;
    }

    public int getNumDimensions() {
        return axes.length;
    }

    public int getNumSamples() {
        int tot = 1;
        for (int i = 0; i < axes.length; i++) {
            tot = tot * axes[i].getNumSamples();
        }

        if (tot == 1) return 0;

        return tot;

    }
}