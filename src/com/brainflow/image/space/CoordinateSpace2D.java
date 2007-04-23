package com.brainflow.image.space;

import com.brainflow.image.axis.CoordinateAxis;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.Anatomy2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 10:13:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateSpace2D extends AbstractCoordinateSpace {

    private IImageOrigin origin;

    private Anatomy2D anatomy = null;

    private CoordinateAxis[] axes;


    public CoordinateSpace2D(Anatomy2D anatomy) {

        this.anatomy = anatomy;

        createImageAxes(2);

        initAxis(new CoordinateAxis(anatomy.XAXIS, new AxisRange(anatomy.XAXIS, 0, 100)), Axis.X_AXIS);
        initAxis(new CoordinateAxis(anatomy.YAXIS, new AxisRange(anatomy.YAXIS, 0, 100)), Axis.Y_AXIS);

        origin = new ImageOrigin2D(anatomy.XAXIS.getMinDirection(), anatomy.YAXIS.getMinDirection(), 0, 0);

    }


    public CoordinateSpace2D(CoordinateAxis xaxis, CoordinateAxis yaxis) {
        Anatomy2D check = Anatomy2D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis());
        assert check != null;

        this.anatomy = check;

        createImageAxes(2);

        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);

        origin = new ImageOrigin2D(xaxis.getAnatomicalAxis().getMinDirection(), yaxis.getAnatomicalAxis().getMinDirection(),
                xaxis.getRange().getBeginning().getX(),
                yaxis.getRange().getBeginning().getX());

    }


    protected CoordinateAxis[] getAxes() {
        return axes;
    }

    protected void createImageAxes(int num) {
        axes = new CoordinateAxis[num];
    }

    protected void initAxis(CoordinateAxis iaxis, Axis aaxis) {
        axes[aaxis.getId()] = iaxis;
    }

    public ICoordinateSpace union(ICoordinateSpace other) {
        assert sameAxes(other) : "cannot perform union for ImageSpaces with different axis orientations";
        if (!sameAxes(other)) {
            throw new IllegalArgumentException("cannot perform union for ImageSpaces with different axis orientations");
        }


        CoordinateAxis[] axes = new ImageAxis[getNumDimensions()];

        for (int i = 0; i < axes.length; i++) {
            AxisRange range1 = other.getImageAxis(Axis.getAxis(i)).getRange();
            AxisRange range2 = getImageAxis(Axis.getAxis(i)).getRange();


            AxisRange nrange = range1.union(range2);
            axes[i] = new CoordinateAxis(nrange.getAnatomicalAxis(), nrange);


        }

        return SpaceFactory.createCoordinateSpace(axes);


    }

    public Anatomy2D getAnatomy() {
        return anatomy;
    }

    public IImageOrigin getImageOrigin() {
        return origin;
    }
}
