package com.brainflow.image.operations;

import com.brainflow.image.data.BinaryImageData2D;
import com.brainflow.image.data.BinaryImageData3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 12:28:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IBinaryOperation3D {

    public BinaryImageData3D compute();

    public void clear();

}
