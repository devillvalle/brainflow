/*
 * ImageLayer.java
 *
 * Created on June 29, 2006, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import com.brainflow.application.IImageDataSource;
import com.brainflow.application.IImageDataSource;
import com.brainflow.colormap.ColorTable;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.utils.Range;


/**
 * @author buchs
 */


public abstract class ImageLayer extends AbstractLayer {


    private IImageDataSource dataSource;

    private IImageData data;

    public ImageLayer(IImageDataSource dataSource) {
        super(new ImageLayerProperties(ColorTable.GRAYSCALE, new Range(dataSource.getData().getMinValue(), dataSource.getData().getMaxValue())));
        this.dataSource = dataSource;
        data = dataSource.getData();
        initMaskList();
    }

    public ImageLayer(IImageDataSource dataSource, ImageLayerProperties _properties) {
        super(_properties);
        this.dataSource = dataSource;
        data = dataSource.getData();
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

    public IImageDataSource getDataSource() {
        return dataSource;
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
