package com.brainflow.core;

import com.brainflow.application.ILoadableImage;
import com.brainflow.core.ImageLayerProperties;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.interpolation.NearestNeighborInterpolator;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.IImageData;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:05:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayer3D extends ImageLayer {


    public ImageLayer3D(ILoadableImage limg, ImageLayerProperties _params) {
        super(limg, _params);
    }

    public ImageLayer3D(IImageData data, ImageLayerProperties _properties) {
        super(data, _properties);
    }

    public IImageData3D getData() {
        return (IImageData3D) super.getData();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public double getValue(AnatomicalPoint3D pt) {
        IImageSpace space = getCoordinateSpace();
        double x = pt.getValue(space.getAnatomicalAxis(Axis.X_AXIS)).getX();
        double y = pt.getValue(space.getAnatomicalAxis(Axis.Y_AXIS)).getX();
        double z = pt.getValue(space.getAnatomicalAxis(Axis.Z_AXIS)).getX();

        return getData().getRealValue(x, y, z, new NearestNeighborInterpolator());
    }

    public SliceRenderer getSliceRenderer(AnatomicalPoint1D slice) {
        return new BasicImageSliceRenderer(this, slice);
    }
}
