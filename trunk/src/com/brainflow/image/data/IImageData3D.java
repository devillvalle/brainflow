package com.brainflow.image.data;

import com.brainflow.utils.Index3D;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.interpolation.InterpolationFunction3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData3D extends IImageData, DataAccessor3D {

    public int indexOf(int x, int y, int z);

    public Index3D indexToGrid(int idx, Index3D voxel);

    public void setValue(int x, int y, int z, double val);

    

}
