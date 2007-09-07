package com.brainflow.image.axis;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalDirection;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.space.Axis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 17, 2007
 * Time: 1:13:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateAxis {


    private AnatomicalAxis axis;

    private AxisRange range;

    public CoordinateAxis() {
        axis = AnatomicalAxis.LEFT_RIGHT;
        range = new AxisRange(axis, 0, 100);
    }

    public CoordinateAxis(AxisRange range) {
        this.range = range;
        axis = range.getAnatomicalAxis();
    }

    public CoordinateAxis(AnatomicalAxis axis) {
        this.axis = axis;
        range = new AxisRange(axis, 0, 100);
    }

    public CoordinateAxis(AnatomicalAxis axis, AxisRange range) {
        this.axis = axis;
        this.range = range;
    }

    public AnatomicalAxis getAnatomicalAxis() {
        return axis;
    }

    public AnatomicalPoint1D getCenter() {
        return range.getCenter();              
    }

    public AxisRange getRange() {
        return range;
    }

    protected void setAnatomicalAxis(AnatomicalAxis axis) {
        this.axis = axis;
    }

    protected void setRange(AxisRange range) {
        this.range = range;
    }

    public double getExtent() {
        return range.getInterval();
    }

    public AnatomicalPoint1D getEdgePoint(AnatomicalDirection adir) {
        if (adir != axis.getMinDirection() && adir != axis.getMaxDirection()) {
            throw new ImageAxis.IncompatibleAxisException("ImageAxis.getEndPoint: supplied Axis Direction " + adir + " is incorrect");
        }

        return range.getEdgePoint(adir);

    }


}
