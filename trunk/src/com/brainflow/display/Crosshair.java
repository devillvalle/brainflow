package com.brainflow.display;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 15, 2006
 * Time: 5:21:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Crosshair extends Model implements ICrosshair {


    private Viewport3D viewport;

    private AnatomicalPoint3D location;

    public Crosshair(Viewport3D _viewport) {
        viewport = _viewport;
        location = new AnatomicalPoint3D((Anatomy3D) viewport.getBounds().getAnatomy(),
                viewport.getXAxisMin() + (viewport.getXAxisExtent() / 2),
                viewport.getYAxisMin() + (viewport.getYAxisExtent() / 2),
                viewport.getZAxisMin() + (viewport.getZAxisExtent() / 2));



    }



    /*private void makeLegal() {
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

    }*/

    public Viewport3D getViewport() {
        return viewport;
    }

    public Anatomy3D getAnatomy() {
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
        if (!ap.equals(location)) {
            double x = ap.getValue(location.getAnatomy().XAXIS).getX();
            double y = ap.getValue(location.getAnatomy().YAXIS).getX();
            double z = ap.getValue(location.getAnatomy().ZAXIS).getX();

            setLocation(x, y, z);
        }

    }

    public AnatomicalPoint3D getLocation() {
        return new AnatomicalPoint3D(location.getAnatomy(),
                location.getX(),
                location.getY(),
                location.getZ());
    }

    private void setLocation(double x, double y, double z) {

        location.setX(x);
        location.setY(y);
        location.setZ(z);
        //todo something better than null?
        this.firePropertyChange(LOCATION_PROPERTY, null, location);
    }

    public void setXValue(double x) {
        if (Double.compare(getXValue(), x) != 0) {
            double oldValue = location.getX();
            location.setX(x);
            this.firePropertyChange(X_VALUE_PROPERTY, oldValue, x);
        }
    }

    public void setYValue(double y) {
        if (Double.compare(getYValue(), y) != 0) {
            double oldValue = location.getY();
            location.setY(y);
            this.firePropertyChange(Y_VALUE_PROPERTY, oldValue, y);
        }
    }

    public void setZValue(double z) {
        if (Double.compare(getZValue(), z) != 0) {
            double oldValue = location.getZ();
            location.setZ(z);
            this.firePropertyChange(Z_VALUE_PROPERTY, oldValue, z);
        }
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
