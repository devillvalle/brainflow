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
public interface IImageData3D extends IImageData {

    public int indexOf(int x, int y, int z);

    public Index3D indexToGrid(int idx, Index3D voxel);

    public double getValue(double x, double y, double z, InterpolationFunction3D interp);

    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp);

    public double getValue(int x, int y, int z);  

    public void setValue(int x, int y, int z, double val);

    public ImageSpace3D getImageSpace();


}
