package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.Dimension2D;
import com.brainflow.utils.Dimension3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:07:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataAccessor3D extends DataAccessor {

    public double value(float x, float y, float z, InterpolationFunction3D interp);

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp);

    public double value(int x, int y, int z);

    public ImageSpace3D getImageSpace();



}
