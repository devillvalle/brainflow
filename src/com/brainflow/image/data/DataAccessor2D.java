package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.utils.IDimension;
import com.brainflow.utils.Dimension2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:05:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataAccessor2D extends DataAccessor {

    public double value(double x, double y, InterpolationFunction2D interp);

    public double worldValue(double realx, double realy, InterpolationFunction2D interp);

    public double value(int x, int y);

    public ImageSpace2D getImageSpace();
}