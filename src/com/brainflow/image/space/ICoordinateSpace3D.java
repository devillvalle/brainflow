package com.brainflow.image.space;

import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.utils.IDimension;
import com.brainflow.utils.Dimension3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 20, 2008
 * Time: 9:35:23 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ICoordinateSpace3D extends ICoordinateSpace {


    Anatomy3D getAnatomy();

    Dimension3D<Float> getOrigin();

    AnatomicalPoint3D getCentroid();
}
