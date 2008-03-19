package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction3D;
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

    public double getValue(double x, double y, double z, InterpolationFunction3D interp);

    public double getWorldValue(double realx, double realy, double realz, InterpolationFunction3D interp);

    public double getValue(int x, int y, int z);

    //public Dimension3D<Integer> getDimension();


}
