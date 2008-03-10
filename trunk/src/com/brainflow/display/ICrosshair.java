package com.brainflow.display;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.core.Viewport3D;

import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 17, 2007
 * Time: 6:27:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICrosshair {
    
    public static final String LOCATION_PROPERTY = "location";

    public static final String X_VALUE_PROPERTY = "XValue";

    public static final String Y_VALUE_PROPERTY = "YValue";
    
    public static final String Z_VALUE_PROPERTY = "ZValue";

    public Viewport3D getViewport();

    public Anatomy3D getAnatomy();

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public double getXValue();

    public double getYValue();

    public double getZValue();

    public AnatomicalPoint1D getValue(AnatomicalAxis axis);

    public void setLocation(AnatomicalPoint3D ap);

    public AnatomicalPoint3D getLocation();

    public void setXValue(double x);

    public void setYValue(double y);

    public void setZValue(double z);

    public void setValue(AnatomicalPoint1D val);
}
