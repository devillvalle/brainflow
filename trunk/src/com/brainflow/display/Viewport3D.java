package com.brainflow.display;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace3D;
import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 3:16:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Viewport3D extends Model {


    public static final String X_AXIS_MIN_PROPERTY = "XAxisMin";
    public static final String Y_AXIS_MIN_PROPERTY = "YAxisMin";
    public static final String Z_AXIS_MIN_PROPERTY = "ZAxisMin";

    public static final String X_AXIS_MAX_PROPERTY = "XAxisMax";
    public static final String Y_AXIS_MAX_PROPERTY = "YAxisMax";
    public static final String Z_AXIS_MAX_PROPERTY = "ZAxisMax";


    public static final String X_AXIS_WIDTH_PROPERTY = "XAxisWidth";
    public static final String Y_AXIS_WIDTH_PROPERTY = "YAxisWidth";
    public static final String Z_AXIS_WIDTH_PROPERTY = "ZAxisWidth";

    public static final String BOUNDS_PROPERTY = "bounds";


    private ImageSpace3D bounds;

    private double XAxisMin;
    private double YAxisMin;
    private double ZAxisMin;

    private double XAxisMax;
    private double YAxisMax;
    private double ZAxisMax;

    private double XAxisWidth;
    private double YAxisWidth;
    private double ZAxisWidth;


    public Viewport3D(ImageSpace3D _bounds) {
        bounds = _bounds;
        init();
    }

    public void setBounds(ImageSpace3D _bounds) {
        ImageSpace3D oldValue = this.bounds;
        bounds = _bounds;
        init();

        firePropertyChange(Viewport3D.BOUNDS_PROPERTY, oldValue, bounds);
    }

    public ImageSpace3D getBounds() {
        return bounds;
    }

    public boolean inBounds(AnatomicalPoint3D pt) {

        if (!bounds.getImageAxis(pt.getAnatomy().XAXIS, true).getRange().contains(pt.getX())) return false;
        if (!bounds.getImageAxis(pt.getAnatomy().YAXIS, true).getRange().contains(pt.getY())) return false;
        if (!bounds.getImageAxis(pt.getAnatomy().ZAXIS, true).getRange().contains(pt.getZ())) return false;

        return true;
    }

    public double getXAxisMin() {
        return XAxisMin;
    }

    private void init() {

        setXAxisMin(bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum());
        setYAxisMin(bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum());
        setZAxisMin(bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum());
        setXAxisWidth(bounds.getImageAxis(Axis.X_AXIS).getRange().getInterval());
        setYAxisWidth(bounds.getImageAxis(Axis.Y_AXIS).getRange().getInterval());
        setZAxisWidth(bounds.getImageAxis(Axis.Z_AXIS).getRange().getInterval());

    }

    public AnatomicalAxis getXAxis() {
        return bounds.getAnatomicalAxis(Axis.X_AXIS);
    }

    public AnatomicalAxis getYAxis() {
        return bounds.getAnatomicalAxis(Axis.Y_AXIS);
    }

    public AnatomicalAxis getZAxis() {
        return bounds.getAnatomicalAxis(Axis.Z_AXIS);
    }

    public AxisRange getRange(AnatomicalAxis axis) {
        if (axis == getXAxis()) {
            return new AxisRange(axis, getXAxisMin(), getXAxisMax());
        } else if (axis == getYAxis()) {
            return new AxisRange(axis, getYAxisMin(), getYAxisMax());
        } else if (axis == getZAxis()) {
            return new AxisRange(axis, getZAxisMin(), getZAxisMax());
        }

        throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
    }

    public void setXAxisMin(double xAxisMin) {
        if (xAxisMin < bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + xAxisMin + "outside of image bounds");
        }

        double oldValue = this.XAxisMin;
        this.XAxisMin = xAxisMin;
        this.XAxisMax = XAxisMin + XAxisWidth;
        this.firePropertyChange(Viewport3D.X_AXIS_MIN_PROPERTY, oldValue, xAxisMin);
    }

    public double getYAxisMin() {
        return YAxisMin;
    }

    public void setYAxisMin(double yAxisMin) {
        if (yAxisMin < bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + yAxisMin + "outside of image bounds");
        }

        double oldValue = this.YAxisMin;
        this.YAxisMin = yAxisMin;
        this.YAxisMax = YAxisMin + YAxisWidth;
        this.firePropertyChange(Viewport3D.Y_AXIS_MIN_PROPERTY, oldValue, yAxisMin);
    }

    public double getZAxisMin() {
        return ZAxisMin;
    }

    public void setZAxisMin(double zAxisMin) {
        if (zAxisMin < bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + zAxisMin + "outside of image bounds");
        }
        double oldValue = this.ZAxisMin;
        this.ZAxisMin = zAxisMin;
        this.ZAxisMax = ZAxisMin + ZAxisWidth;
        this.firePropertyChange(Viewport3D.Z_AXIS_MIN_PROPERTY, oldValue, zAxisMin);
    }


    public double getXAxisMax() {
        return XAxisMax;
    }

    public double getYAxisMax() {
        return YAxisMax;
    }

    public double getZAxisMax() {
        return ZAxisMax;
    }

    public double getXAxisWidth() {
        return XAxisWidth;
    }

    public void setXAxisWidth(double xAxisWidth) {
        double oldValue = this.XAxisWidth;
        this.XAxisWidth = xAxisWidth;
        this.XAxisMax = XAxisMin + XAxisWidth;
        this.firePropertyChange(Viewport3D.X_AXIS_WIDTH_PROPERTY, oldValue, xAxisWidth);
    }

    public double getYAxisWidth() {
        return YAxisWidth;
    }

    public void setYAxisWidth(double yAxisWidth) {
        double oldValue = this.YAxisWidth;
        this.YAxisWidth = yAxisWidth;
        this.YAxisMax = YAxisMin + YAxisWidth;
        this.firePropertyChange(Viewport3D.Y_AXIS_WIDTH_PROPERTY, oldValue, yAxisWidth);
    }

    public double getZAxisWidth() {
        return ZAxisWidth;
    }

    public void setZAxisWidth(double zAxisWidth) {
        double oldValue = this.ZAxisWidth;
        this.ZAxisWidth = zAxisWidth;
        this.ZAxisMax = ZAxisMin + ZAxisWidth;
        this.firePropertyChange(Viewport3D.Z_AXIS_WIDTH_PROPERTY, oldValue, zAxisWidth);
    }


}
