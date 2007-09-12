package com.brainflow.core;

import com.brainflow.display.ThresholdRange;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.space.ICoordinateSpace;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 15, 2007
 * Time: 9:51:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLayer {

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    // instantiate to a NullMaskList or something to that effect.
    private IMaskList maskList;

    private ImageLayerProperties properties;

    protected AbstractLayer(ImageLayerProperties properties) {
        this.properties = properties;
        init();
    }

    public ImageLayerProperties getImageLayerProperties() {
        return properties;
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    protected void setMaskList(IMaskList list) {
        maskList = list;
    }

    public IMaskList getMaskList() {
        return maskList;
    }

    public abstract double getValue(AnatomicalPoint3D pt);

    public abstract SliceRenderer getSliceRenderer(AnatomicalPoint1D slice);


    public boolean isVisible() {
        return properties.getVisible().isVisible();
    }

    public double getOpacity() {
        return properties.getOpacity().getOpacity();
    }

    public ThresholdRange getThreshold() {
        return properties.getThresholdRange();
    }

    public abstract Object getDataSource();

    private void init() {
        properties.getColorMap().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {

                support.firePropertyChange(new PropertyChangeEvent(AbstractLayer.this, ImageLayerProperties.COLOR_MAP_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));
            }
        });



        properties.getResampleInterpolation().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(AbstractLayer.this, ImageLayerProperties.RESAMPLE_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));
            }
        });

        properties.getVisible().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(AbstractLayer.this, ImageLayerProperties.VISIBLE_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));

            }
        });

        properties.getOpacity().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(AbstractLayer.this, ImageLayerProperties.OPACITY_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));

            }
        });

        properties.getSmoothing().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(AbstractLayer.this, ImageLayerProperties.SMOOTHING_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));

            }
        });

        properties.getThresholdRange().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(AbstractLayer.this, ImageLayerProperties.THRESHOLD_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));

            }
        });


    }


    public abstract ICoordinateSpace getCoordinateSpace();

    public abstract double getMinValue();

    public abstract double getMaxValue();

    public abstract String getLabel();


}
