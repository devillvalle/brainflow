package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalPlane;
import com.brainflow.image.anatomy.AnatomicalPoint;
import com.brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 18, 2004
 * Time: 9:49:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpace2D extends AbstractImageSpace {

    private ImageOrigin2D origin;

    public ImageSpace2D(ImageAxis xaxis, ImageAxis yaxis) {
        AnatomicalPlane check = AnatomicalPlane.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis());
        assert check != null;

        setAnatomy(check);
        createImageAxes(2);
        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);

        origin = new ImageOrigin2D(xaxis.getAnatomicalAxis().getMinDirection(),
                yaxis.getAnatomicalAxis().getMinDirection(),
                xaxis.getRange().getBeginning().getX(), yaxis.getRange().getBeginning().getX());
    }

    public ImageSpace2D(ImageSpace2D space) {
        setAnatomy(space.getAnatomy());
        createImageAxes(2);
        initAxis(space.getImageAxis(Axis.X_AXIS), Axis.X_AXIS);
        initAxis(space.getImageAxis(Axis.Y_AXIS), Axis.Y_AXIS);
    }


    public IImageOrigin getImageOrigin() {
        return origin;

    }

    public IImageSpace union(IImageSpace other) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AnatomicalPoint getCentroid() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
