/*
 * ImageLayer.java
 *
 * Created on June 29, 2006, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import com.brainflow.application.ILoadableImage;
import com.brainflow.display.ImageLayerProperties;
import com.brainflow.display.ThresholdRange;
import com.brainflow.image.data.IImageData;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * @author buchs
 */


public class ImageLayer {

    private final ImageLayerProperties properties;

    private ILoadableImage limg;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);


    public ImageLayer(ILoadableImage _limg, ImageLayerProperties _properties) {
        limg = _limg;
        properties = _properties;
        init();
    }

    public boolean isVisible() {
        return properties.getVisible().getProperty().isVisible();
    }

     public double getOpacity() {
        return properties.getOpacity().getProperty().getOpacity();
    }

    public ThresholdRange getThreshold() {
        return properties.getThresholdRange().getProperty();

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


    private void init() {
        properties.getColorMap().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("firing color map changed : " + evt.getPropertyName());
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, ImageLayerProperties.COLOR_MAP_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));
            }
        });

        properties.getImageOpList().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, ImageLayerProperties.IMAGEOP_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));
            }
        });


        properties.getResampleInterpolation().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this,ImageLayerProperties.RESAMPLE_PROPERTY ,
                        evt.getOldValue(), evt.getNewValue()));
            }
        });

        properties.getVisible().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, ImageLayerProperties.VISIBLE_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));

            }
        });

        properties.getOpacity().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, ImageLayerProperties.OPACITY_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));

            }
        });

        properties.getThresholdRange().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, ImageLayerProperties.THRESHOLD_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));

            }
        });


    }


    public IImageData getImageData() {
        return limg.getData();
    }

    public ILoadableImage getLoadableImage() {
        return limg;
    }

    public ImageLayerProperties getImageLayerProperties() {
        return properties;
    }

    public String toString() {
        return limg.getStem();
    }


}
