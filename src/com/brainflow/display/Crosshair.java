package com.brainflow.display;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.beans.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 15, 2006
 * Time: 5:21:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Crosshair extends Model implements ICrosshair {

    public static final String LOCATION_PROPERTY = "location";
    public static final String X_VALUE_PROPERTY = "XValue";
    public static final String Y_VALUE_PROPERTY = "YValue";
    public static final String Z_VALUE_PROPERTY = "ZValue";

    private Viewport3D viewport;

    private AnatomicalPoint3D location;

    public Crosshair(Viewport3D _viewport) {
        viewport = _viewport;
        location = new AnatomicalPoint3D((AnatomicalVolume) viewport.getBounds().getAnatomy(),
                viewport.getXAxisMin() + (viewport.getXAxisWidth() / 2),
                viewport.getYAxisMin() + (viewport.getYAxisWidth() / 2),
                viewport.getZAxisMin() + (viewport.getZAxisWidth() / 2));


        viewport.addPropertyChangeListener(Viewport3D.BOUNDS_PROPERTY, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                viewportChanged();

            }

        });

        viewport.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                //makeLegal();


            }
        });

    }

    private void viewportChanged() {
        IImageSpace space = viewport.getBounds();

        // danger danger
        //AnatomicalPoint3D loc = ((ImageSpace3D)space).getCentroid();
        //setXValue(loc.getX());
        //setYValue(loc.getY());
        //setZValue(loc.getZ());

    }


    private void makeLegal() {
        if (getXValue() < viewport.getXAxisMin()) {
            setXValue(viewport.getXAxisMin());
        }
        if (getYValue() < viewport.getYAxisMin()) {
            setYValue(viewport.getYAxisMin());
        }
        if (getZValue() < viewport.getZAxisMin()) {
            setZValue(viewport.getZAxisMin());
        }
        if (getXValue() > viewport.getXAxisMax()) {
            setXValue(viewport.getXAxisMax());
        }
        if (getYValue() > viewport.getYAxisMax()) {
            setYValue(viewport.getYAxisMax());
        }

        if (getZValue() > viewport.getZAxisMax()) {
            setZValue(viewport.getZAxisMax());
        }

    }

    public Viewport3D getViewport() {
        return viewport;
    }

    public AnatomicalVolume getAnatomy() {
        return location.getAnatomy();
    }

    public double getXValue() {
        return location.getX();
    }

    public double getYValue() {
        return location.getY();
    }

    public double getZValue() {
        return location.getZ();
    }

    public AnatomicalPoint1D getValue(AnatomicalAxis axis) {
        return location.getValue(axis);
    }

    public void setLocation(AnatomicalPoint3D ap) {

        double x = ap.getValue(location.getAnatomy().XAXIS).getX();
        double y = ap.getValue(location.getAnatomy().YAXIS).getX();
        double z = ap.getValue(location.getAnatomy().ZAXIS).getX();

        setXValue(x);
        setYValue(y);
        setZValue(z);

    }

    public AnatomicalPoint3D getLocation() {
        return new AnatomicalPoint3D(location.getAnatomy(),
                location.getX(),
                location.getY(),
                location.getZ());
    }


    public void setXValue(double x) {
        double oldValue = location.getX();
        location.setX(x);
        this.firePropertyChange(X_VALUE_PROPERTY, oldValue, x);
    }

    public void setYValue(double y) {
        double oldValue = location.getY();
        location.setY(y);
        this.firePropertyChange(Y_VALUE_PROPERTY, oldValue, y);
    }

    public void setZValue(double z) {
        double oldValue = location.getZ();
        location.setZ(z);
        this.firePropertyChange(Z_VALUE_PROPERTY, oldValue, z);
    }

    public void setValue(AnatomicalPoint1D val) {
        if (val.getAnatomy() == location.getAnatomy().XAXIS) {
            setXValue(val.getX());
        } else if (val.getAnatomy() == location.getAnatomy().YAXIS) {
            setYValue(val.getX());
        } else if (val.getAnatomy() == location.getAnatomy().ZAXIS) {
            setZValue(val.getX());
        } else {
            throw new IllegalArgumentException("illegal axis for current cross hair: " + val.getAnatomy());
        }

    }


}
