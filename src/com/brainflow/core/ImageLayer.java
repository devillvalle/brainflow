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
import com.brainflow.core.ImageLayerProperties;
import com.brainflow.display.ThresholdRange;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.IImageSpace;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * @author buchs
 */


public abstract class ImageLayer extends AbstractLayer {


    private ILoadableImage limg;

    
    public ImageLayer(ILoadableImage _limg, ImageLayerProperties _properties) {
        super(_properties);
        limg = _limg;
    }

    public IImageData getData() {
        return limg.getData();
    }
    
    public String getLabel() {
        return limg.getData().getImageLabel();
    }

    public IImageSpace getCoordinateSpace() {
        return limg.getData().getImageSpace();
    }

    public double getMaxValue() {
        return limg.getData().getMaxValue();
    }

    public double getMinValue() {
        return limg.getData().getMinValue();
    }


    public String toString() {
        return limg.getStem();
    }


}
