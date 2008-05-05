/*
 * ImageLayer.java
 *
 * Created on June 29, 2006, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.colormap.ColorTable;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.utils.Range;


/**
 * @author buchs
 */


public abstract class ImageLayer<T extends IImageSpace> extends AbstractLayer {


    private IImageDataSource dataSource;

    //private IImageData data;

    public ImageLayer(IImageDataSource dataSource) {
        super(new ImageLayerProperties(ColorTable.GRAYSCALE, new Range(0, 255)));
        this.dataSource = dataSource;

        if (dataSource.isLoaded()) {
            getImageLayerProperties().getClipRange().setLowClip(getData().minValue());
            getImageLayerProperties().getClipRange().setHighClip(getData().maxValue());

        }
        //data = dataSource.getData();
        initMaskList();
    }

    public ImageLayer(IImageDataSource dataSource, ImageLayerProperties _properties) {
        super(_properties);
        this.dataSource = dataSource;

        try { if (dataSource.isLoaded()) {
            getImageLayerProperties().getClipRange().setLowClip(getData().minValue());
            getImageLayerProperties().getClipRange().setHighClip(getData().maxValue());

        }
        } catch(NullPointerException e) {

            e.printStackTrace();
        }
        //data = dataSource.getData();
        initMaskList();
    }



    private void initMaskList() {
        setMaskList(new ImageMaskList(this));
    }

    public ImageMaskList getMaskList() {
        return (ImageMaskList)(super.getMaskList());
    }


    public IImageData getData() {
        return dataSource.getData();
    }

    public IImageDataSource getDataSource() {
        return dataSource;
    }
    
    public String getLabel() {
        return dataSource.getStem();
    }

    public T getCoordinateSpace() {
        return (T)dataSource.getData().getImageSpace();
    }

    public double getMaxValue() {
        return dataSource.getData().maxValue();
    }

    public double getMinValue() {
        return dataSource.getData().minValue();
    }


    public String toString() {
        return dataSource.getImageInfo().getImageLabel();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageLayer that = (ImageLayer) o;

        if (!dataSource.equals(that.dataSource)) return false;

        return true;
    }

    public int hashCode() {
        return dataSource.hashCode();
    }
}
