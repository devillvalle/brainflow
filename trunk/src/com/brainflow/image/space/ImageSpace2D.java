package com.brainflow.image.space;

import com.brainflow.image.anatomy.Anatomy2D;
import com.brainflow.image.anatomy.AnatomicalPoint;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.utils.IDimension;
import com.brainflow.utils.Dimension2D;

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
        Anatomy2D check = Anatomy2D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis());
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


    public IDimension<Float> getOrigin() {
        //todo fixme
        return null;

    }

    public IDimension<Integer> getDimension() {
        return new Dimension2D<Integer>(getDimension(Axis.X_AXIS), getDimension(Axis.Y_AXIS));
    }

    public IImageSpace union(IImageSpace other) {
        throw new UnsupportedOperationException();
    }

    public AnatomicalPoint getCentroid() {
       throw new UnsupportedOperationException();
    }

    public float[] gridToWorld(int[] gridpos) {
        return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float[] worldToGrid(float[] coord) {
        return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
