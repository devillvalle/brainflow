package com.brainflow.display;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 17, 2007
 * Time: 6:27:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICrosshair {
    

    Viewport3D getViewport();

    AnatomicalVolume getAnatomy();

    double getXValue();

    double getYValue();

    double getZValue();

    AnatomicalPoint1D getValue(AnatomicalAxis axis);

    void setLocation(AnatomicalPoint3D ap);

    AnatomicalPoint3D getLocation();

    void setXValue(double x);

    void setYValue(double y);

    void setZValue(double z);

    void setValue(AnatomicalPoint1D val);
}
