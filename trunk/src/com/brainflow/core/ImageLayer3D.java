package com.brainflow.core;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.application.MemoryImageDataSource;
import com.brainflow.core.rendering.BasicImageSliceRenderer;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.interpolation.NearestNeighborInterpolator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:05:46 PM
 * To change this template use File | Settings | File Templates.
 */




public class ImageLayer3D extends ImageLayer<ImageSpace3D> {



    public ImageLayer3D(IImageData data) {
        super(new MemoryImageDataSource(data));
    }

    public ImageLayer3D(IImageData data, ImageLayerProperties _params) {
        super(new MemoryImageDataSource(data), _params);
    }


    public ImageLayer3D(IImageDataSource dataSource) {
        super(dataSource);
    }

    public ImageLayer3D(IImageDataSource dataSource, ImageLayerProperties _params) {
        super(dataSource, _params);
    }

    public IImageData3D getData() {
        return  (IImageData3D)super.getData();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public double getValue(AnatomicalPoint3D pt) {
        IImageSpace space = getCoordinateSpace();
        float x = (float)pt.getValue(space.getAnatomicalAxis(Axis.X_AXIS)).getX();
        float y = (float)pt.getValue(space.getAnatomicalAxis(Axis.Y_AXIS)).getX();
        float z = (float)pt.getValue(space.getAnatomicalAxis(Axis.Z_AXIS)).getX();

        return getData().worldValue(x, y, z, new NearestNeighborInterpolator());
    }

    private SliceRenderer getSliceRenderer(ImageSpace3D refspace, AnatomicalPoint1D slice) {
        return new BasicImageSliceRenderer(refspace, this, slice);
    }

    public SliceRenderer getSliceRenderer(IImageSpace refspace, AnatomicalPoint1D slice) {
        return getSliceRenderer((ImageSpace3D)refspace, slice);
    }
}
