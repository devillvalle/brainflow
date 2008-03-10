package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.utils.Index3D;
import com.brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 9:03:48 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageData3D extends AbstractImageData implements IImageData3D {

    private int planeSize;

    private int dim0;

    protected AbstractImageData3D(ImageSpace3D space) {
        super(space);
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
    }

    protected AbstractImageData3D(ImageSpace3D space, DataType dtype) {
        super(space, dtype);
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
    }

    public ImageSpace3D getImageSpace() {
        return (ImageSpace3D) space;
    }

    public int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
    }

    public Index3D indexToGrid(int idx, Index3D voxel) {
        voxel.setZ(idx / planeSize);
        int remainder = (idx % planeSize);
        voxel.setY(remainder / space.getDimension(Axis.X_AXIS));
        voxel.setX(remainder % space.getDimension(Axis.X_AXIS));

        return voxel;
    }
}


