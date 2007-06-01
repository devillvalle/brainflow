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
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.space.IImageSpace;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * @author buchs
 */


public abstract class ImageLayer extends AbstractLayer {


    private IImageData data;

    private MaskList maskList;

    
    public ImageLayer(ILoadableImage _limg, ImageLayerProperties _properties) {
        super(_properties);
        data = _limg.getData();
        initMaskList();
    }

    public ImageLayer(IImageData data, ImageLayerProperties _properties) {
        super(_properties);
        this.data = data;
        initMaskList();
    }

    private void initMaskList() {
        MaskItem root = new MaskItem((IImageData3D)getData(),
                getImageLayerProperties().getThresholdRange().getProperty(), 1);
        maskList = new MaskList(root);

    }

    public MaskList getMaskList() {
        return maskList;
    }

    public IImageData getData() {
        return data;
    }
    
    public String getLabel() {
        return data.getImageLabel();
    }

    public IImageSpace getCoordinateSpace() {
        return data.getImageSpace();
    }

    public double getMaxValue() {
        return data.getMaxValue();
    }

    public double getMinValue() {
        return data.getMinValue();
    }


    public String toString() {
        return data.getImageLabel();
    }


}
