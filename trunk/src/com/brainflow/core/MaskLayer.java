package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.Axis;
import com.brainflow.image.data.MaskedData3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.interpolation.NearestNeighborInterpolator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 10:16:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskLayer extends ImageLayer {


    private MaskedData3D mask;



    public MaskLayer(MaskedData3D _mask, ImageLayerProperties _properties) {
        super(_mask, _properties);
        mask = _mask;


    }

    public double getValue(AnatomicalPoint3D pt) {
        IImageSpace space = getCoordinateSpace();
        double x = pt.getValue(space.getAnatomicalAxis(Axis.X_AXIS)).getX();
        double y = pt.getValue(space.getAnatomicalAxis(Axis.Y_AXIS)).getX();
        double z = pt.getValue(space.getAnatomicalAxis(Axis.Z_AXIS)).getX();

        return mask.getValue(x, y, z, new NearestNeighborInterpolator());
    }

    public SliceRenderer getSliceRenderer(AnatomicalPoint1D slice) {
        return new BasicImageSliceRenderer(this, slice);
    }

    public MaskedData3D getData() {
        return mask;
    }

    public IImageSpace getCoordinateSpace() {
        return mask.getImageSpace();
    }

    public double getMinValue() {
        return mask.getMinValue();
    }

    public double getMaxValue() {
        return mask.getMaxValue();
    }

    public String getLabel() {
        return mask.getImageLabel();
    }
}
