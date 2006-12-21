package com.brainflow.image.interpolation;

import com.brainflow.image.data.IImageData2D;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface InterpolationFunction2D {

    public double interpolate(double dx, double dy, IImageData2D data);
}