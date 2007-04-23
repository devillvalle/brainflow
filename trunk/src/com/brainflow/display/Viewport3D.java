package com.brainflow.display;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModelListener;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ICoordinateSpace;
import com.jgoodies.binding.beans.Model;

import javax.swing.event.ListDataEvent;
import java.beans.PropertyChangeEvent;

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


    public static final String X_AXIS_EXTENT_PROPERTY = "XAxisExtent";
    public static final String Y_AXIS_EXTENT_PROPERTY = "YAxisExtent";
    public static final String Z_AXIS_EXTENT_PROPERTY = "ZAxisExtent";

    public static final String BOUNDS_PROPERTY = "bounds";


    private IImageDisplayModel displayModel;
    private IImageSpace bounds;

    private double XAxisMin;
    private double YAxisMin;
    private double ZAxisMin;

    private double XAxisMax;
    private double YAxisMax;
    private double ZAxisMax;

    private double XAxisExtent;
    private double YAxisExtent;
    private double ZAxisExtent;


    public Viewport3D(IImageDisplayModel _displayModel) {
        displayModel = _displayModel;
        bounds = _displayModel.getImageSpace();
        displayModel.addImageDisplayModelListener(new ImageDisplayModelListener() {


            public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
                if (!bounds.equals(space)) {
                    model.getImageSpace();
                    init();
                }

            }

            public void intervalAdded(ListDataEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void intervalRemoved(ListDataEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void contentsChanged(ListDataEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void propertyChange(PropertyChangeEvent evt) {

            }

        });


        init();
    }


    public IImageSpace getBounds() {
        return bounds;
    }

    public boolean inBounds(AnatomicalPoint3D pt) {

        if (!bounds.getImageAxis(pt.getAnatomy().XAXIS, true).getRange().contains(pt.getX())) return false;
        if (!bounds.getImageAxis(pt.getAnatomy().YAXIS, true).getRange().contains(pt.getY())) return false;
        if (!bounds.getImageAxis(pt.getAnatomy().ZAXIS, true).getRange().contains(pt.getZ())) return false;

        return true;
    }

    public boolean inBounds(AnatomicalAxis axis, double val) {
        return bounds.getImageAxis(axis,true).getRange().contains(val);       
    }

    public double getXAxisMin() {
        return XAxisMin;
    }

    private void init() {
        setXAxisMin(bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum());
        setYAxisMin(bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum());
        setZAxisMin(bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum());
        setXAxisExtent(bounds.getImageAxis(Axis.X_AXIS).getRange().getInterval());
        setYAxisExtent(bounds.getImageAxis(Axis.Y_AXIS).getRange().getInterval());
        setZAxisExtent(bounds.getImageAxis(Axis.Z_AXIS).getRange().getInterval());

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

    public String getMinPropertyName(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return Viewport3D.X_AXIS_MIN_PROPERTY;
        } else if (axis.sameAxis(getYAxis())) {
            return Viewport3D.Y_AXIS_MIN_PROPERTY;
        } else if (axis.sameAxis(getZAxis())) {
            return Viewport3D.Z_AXIS_MIN_PROPERTY;
        }

        throw new IllegalArgumentException("Invalid axis " + axis + " for viewport");

    }

    public String getMaxPropertyName(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return Viewport3D.X_AXIS_MAX_PROPERTY;
        } else if (axis.sameAxis(getYAxis())) {
            return Viewport3D.Y_AXIS_MAX_PROPERTY;
        } else if (axis.sameAxis(getZAxis())) {
            return Viewport3D.Z_AXIS_MAX_PROPERTY;
        }

        throw new IllegalArgumentException("Invalid axis " + axis + " for viewport");

    }

    public String getExtentPropertyName(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return Viewport3D.X_AXIS_EXTENT_PROPERTY;
        } else if (axis.sameAxis(getYAxis())) {
            return Viewport3D.Y_AXIS_EXTENT_PROPERTY;
        } else if (axis.sameAxis(getZAxis())) {
            return Viewport3D.Z_AXIS_EXTENT_PROPERTY;
        }

        throw new IllegalArgumentException("Invalid axis " + axis + " for viewport");

    }


    public AxisRange getRange(AnatomicalAxis axis) {
        if (axis.sameAxis(getXAxis())) {
            return new AxisRange(axis, getXAxisMin(), getXAxisMax());
        } else if (axis.sameAxis(getYAxis())) {
            return new AxisRange(axis, getYAxisMin(), getYAxisMax());
        } else if (axis.sameAxis(getZAxis())) {
            return new AxisRange(axis, getZAxisMin(), getZAxisMax());
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }
    }

    public void setAxesRange(AnatomicalAxis axis1, double min1, double max1,
                             AnatomicalAxis axis2, double min2, double max2) {
        setAxisRange(axis1, min1, max1-min1);
        setAxisRange(axis2, min2, max2-min2);


    }


    private void setAxisRange0(AnatomicalAxis axis, double min, double extent) {
        if (axis.sameAxis(getXAxis())) {
            setXAxisMin0(min);
            setXAxisExtent0(extent);
        } else if (axis.sameAxis(getYAxis())) {
            setYAxisMin0(min);
            setYAxisExtent0(extent);
        } else if (axis.sameAxis(getZAxis())) {
            setZAxisMin0(min);
            setZAxisExtent0(extent);
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }
    }

    public void setAxisRange(AnatomicalAxis axis, double min, double extent) {
        if (axis.sameAxis(getXAxis())) {
            setXAxisMin(min);
            setXAxisExtent(extent);
        } else if (axis.sameAxis(getYAxis())) {
            setYAxisMin(min);
            setYAxisExtent(extent);
        } else if (axis.sameAxis(getZAxis())) {
            setZAxisMin(min);
            setZAxisExtent(extent);
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }
    }

    public void setAxisMin(AnatomicalAxis axis, double min) {
        if (axis.sameAxis(getXAxis())) {
            setXAxisMin(min);
        } else if (axis.sameAxis(getYAxis())) {
            setYAxisMin(min);
        } else if (axis.sameAxis(getZAxis())) {
            setZAxisMin(min);
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }


    }

    public void setAxisExtent(AnatomicalAxis axis, double extent) {
        if (axis.sameAxis(getXAxis())) {
            setXAxisExtent(extent);
        } else if (axis.sameAxis(getYAxis())) {
            setYAxisExtent(extent);
        } else if (axis.sameAxis(getZAxis())) {
            setZAxisExtent(extent);
        } else {
            throw new IllegalArgumentException("Invalid axis for viewport: " + axis);
        }


    }

    private void setXAxisMin0(double xAxisMin) {
        if (xAxisMin < bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + xAxisMin + "outside of image bounds");
            //xAxisMin = bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
        }


        double oldValue = this.XAxisMin;
        this.XAxisMin = xAxisMin;
        this.XAxisMax = XAxisMin + XAxisExtent;
        this.firePropertyChange(Viewport3D.X_AXIS_MIN_PROPERTY, oldValue, xAxisMin);
    }


    public void setXAxisMin(double xAxisMin) {
        if (xAxisMin < bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + xAxisMin + "outside of image bounds");
            //xAxisMin = bounds.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
        }


        double oldValue = this.XAxisMin;
        this.XAxisMin = xAxisMin;
        this.XAxisMax = XAxisMin + XAxisExtent;
        this.firePropertyChange(Viewport3D.X_AXIS_MIN_PROPERTY, oldValue, xAxisMin);
    }

    public double getYAxisMin() {
        return YAxisMin;
    }

    private void setYAxisMin0(double yAxisMin) {
        if (yAxisMin < bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + yAxisMin + " is outside of image bounds");

            //yAxisMin = bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();
        }


        double oldValue = this.YAxisMin;
        this.YAxisMin = yAxisMin;
        this.YAxisMax = YAxisMin + YAxisExtent;

    }

    public void setYAxisMin(double yAxisMin) {
        if (yAxisMin < bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + yAxisMin + " is outside of image bounds");

            //yAxisMin = bounds.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();
        }


        double oldValue = this.YAxisMin;
        this.YAxisMin = yAxisMin;
        this.YAxisMax = YAxisMin + YAxisExtent;
        this.firePropertyChange(Viewport3D.Y_AXIS_MIN_PROPERTY, oldValue, yAxisMin);
    }

    public double getZAxisMin() {
        return ZAxisMin;
    }

    private void setZAxisMin0(double zAxisMin) {
        if (zAxisMin < bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + zAxisMin + "outside of image bounds");
            //zAxisMin = bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum();
        }


        double oldValue = this.ZAxisMin;
        this.ZAxisMin = zAxisMin;
        this.ZAxisMax = ZAxisMin + ZAxisExtent;

    }

    public void setZAxisMin(double zAxisMin) {
        if (zAxisMin < bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum()) {
            throw new IllegalArgumentException("value " + zAxisMin + "outside of image bounds");
            //zAxisMin = bounds.getImageAxis(Axis.Z_AXIS).getRange().getMinimum();
        }


        double oldValue = this.ZAxisMin;
        this.ZAxisMin = zAxisMin;
        this.ZAxisMax = ZAxisMin + ZAxisExtent;
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

    public double getXAxisExtent() {
        return XAxisExtent;
    }

    public void setXAxisExtent(double xAxisExtent) {
        double oldValue = this.XAxisExtent;
        this.XAxisExtent = xAxisExtent;
        this.XAxisMax = XAxisMin + XAxisExtent;
        this.firePropertyChange(Viewport3D.X_AXIS_EXTENT_PROPERTY, oldValue, xAxisExtent);
    }

    private void setXAxisExtent0(double xAxisExtent) {
        this.XAxisExtent = xAxisExtent;
        this.XAxisMax = XAxisMin + XAxisExtent;
    }

    public double getYAxisExtent() {
        return YAxisExtent;
    }

    public void setYAxisExtent(double yAxisExtent) {
        double oldValue = this.YAxisExtent;
        this.YAxisExtent = yAxisExtent;
        this.YAxisMax = YAxisMin + YAxisExtent;
        this.firePropertyChange(Viewport3D.Y_AXIS_EXTENT_PROPERTY, oldValue, yAxisExtent);
    }

    private void setYAxisExtent0(double yAxisExtent) {
        this.YAxisExtent = yAxisExtent;
        this.YAxisMax = YAxisMin + YAxisExtent;

    }

    public double getZAxisExtent() {
        return ZAxisExtent;
    }

    public void setZAxisExtent(double zAxisExtent) {
        double oldValue = this.ZAxisExtent;
        this.ZAxisExtent = zAxisExtent;
        this.ZAxisMax = ZAxisMin + ZAxisExtent;
        this.firePropertyChange(Viewport3D.Z_AXIS_EXTENT_PROPERTY, oldValue, zAxisExtent);
    }

    private void setZAxisExtent0(double zAxisExtent) {
        this.ZAxisExtent = zAxisExtent;
        this.ZAxisMax = ZAxisMin + ZAxisExtent;

    }


}
