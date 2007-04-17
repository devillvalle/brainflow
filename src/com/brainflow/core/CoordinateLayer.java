package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.data.CoordinateSet3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 17, 2007
 * Time: 12:44:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateLayer extends AbstractLayer {

    private CoordinateSet3D coordinates;

    public CoordinateLayer(ImageLayerProperties properties, CoordinateSet3D coords) {
        super(properties);
    }

    public double getValue(AnatomicalPoint3D pt) {
        return 0;
    }

    public CoordinateSet3D getData() {
        return coordinates;
    }

    public IImageSpace getImageSpace() {
        // could be such a thing a "coordinate space" which image space extends
        return coordinates.getSpace();
    }

    public double getMinValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getMaxValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getLabel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
