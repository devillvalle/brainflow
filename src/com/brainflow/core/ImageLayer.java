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
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, evt.getPropertyName(),
                        evt.getOldValue(), evt.getNewValue()));
            }
        });

        properties.getImageOpList().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, evt.getPropertyName(),
                        evt.getOldValue(), evt.getNewValue()));
            }
        });


        properties.getResampleInterpolation().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, evt.getPropertyName(),
                        evt.getOldValue(), evt.getNewValue()));
            }
        });

        properties.getVisible().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                support.firePropertyChange(new PropertyChangeEvent(ImageLayer.this, evt.getPropertyName(),
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

    public ImageLayerProperties getImageLayerParameters() {
        return properties;
    }

    public String toString() {
        return limg.getStem();
    }


}
