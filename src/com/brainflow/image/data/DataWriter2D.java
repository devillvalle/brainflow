package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Nov 29, 2008
 * Time: 12:37:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataWriter2D extends DataWriter, DataAccessor2D {


    public void setValue(int x, int y, double val);

    public IImageData2D asImageData();




}
