package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.space.ICoordinateSpace;
import com.brainflow.image.data.CoordinateSet3D;
import com.brainflow.core.rendering.BasicCoordinateSliceRenderer;

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
        coordinates = coords;
    }

    public double getValue(AnatomicalPoint3D pt) {
        return 0;
    }

    public CoordinateSet3D getData() {
        return coordinates;
    }

    public ICoordinateSpace getCoordinateSpace() {
        // could be such a thing a "coordinate space" which image space extends
        return coordinates.getSpace();
    }

    public double getMinValue() {
        return coordinates.getMinValue();
    }

    public double getMaxValue() {
        return coordinates.getMaxValue();
    }

    public String getLabel() {
        return "coordinates";
    }

    public SliceRenderer getSliceRenderer(AnatomicalPoint1D slice) {
        return new BasicCoordinateSliceRenderer(this, slice);
    }
}
