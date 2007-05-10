package com.brainflow.image.operations;

import com.brainflow.image.data.BinaryImageData2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 10, 2007
 * Time: 1:00:35 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IBinaryOperation2D {

    public BinaryImageData2D compute();

    public void clear();




}
