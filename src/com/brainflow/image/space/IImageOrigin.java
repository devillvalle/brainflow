package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalDirection;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalPoint;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 12, 2004
 * Time: 2:29:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageOrigin {


    public AnatomicalDirection[] getOriginalDirection();
    public AnatomicalPoint getOrigin();



}
