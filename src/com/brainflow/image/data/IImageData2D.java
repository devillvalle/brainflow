package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.iterators.ImageIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData2D extends IImageData, DataAccessor2D {


    public int indexOf(int x, int y);

    
    public void setValue(int x, int y, double val);

}
