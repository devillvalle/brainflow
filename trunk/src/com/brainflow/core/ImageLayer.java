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
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.colormap.ColorTable;
import com.brainflow.utils.Range;


/**
 * @author buchs
 */


public abstract class ImageLayer extends AbstractLayer {


    private IImageData data;



    public ImageLayer(IImageData data) {
        super(new ImageLayerProperties(ColorTable.GRAYSCALE, new Range(data.getMinValue(), data.getMaxValue())));
        this.data = data;
        initMaskList();
    }

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

        setMaskList(new ImageMaskList(this));

    }

    public ImageMaskList getMaskList() {
        return (ImageMaskList)(super.getMaskList());
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
