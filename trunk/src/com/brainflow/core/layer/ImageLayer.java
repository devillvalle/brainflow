/*
 * ImageLayer.java
 *
 * Created on June 29, 2006, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core.layer;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.colormap.ColorTable;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.utils.Range;
import com.brainflow.core.layer.AbstractLayer;
import com.brainflow.core.layer.ImageLayerProperties;
import com.brainflow.core.layer.ImageMaskList;
import com.brainflow.core.ClipRange;


/**
 * @author buchs
 */


public abstract class ImageLayer<T extends IImageSpace> extends AbstractLayer {


    private IImageDataSource dataSource;

    //private IImageData data;

    public ImageLayer(ImageLayer layer) {
        super(layer.getImageLayerProperties());
        this.dataSource = layer.getDataSource();
        initMaskList();
    }

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


        if (dataSource.isLoaded()) {
            ClipRange clip = _properties.getClipRange();
            _properties.clipRange.set(new ClipRange(getData().minValue(), getData().maxValue(), clip.getLowClip(), clip.getHighClip()));
            //_properties.getClipRange().setLowClip(getData().minValue());
            //_properties.getClipRange().setHighClip(getData().maxValue());

        }

        //data = dataSource.getData();
        initMaskList();
    }


    private void initMaskList() {
        setMaskList(new ImageMaskList(this));
    }

    public ImageMaskList getMaskList() {
        return (ImageMaskList) (super.getMaskList());
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
        return (T) dataSource.getData().getImageSpace();
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /*public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;

       ImageLayer that = (ImageLayer) o;

       if (!dataSource.equals(that.dataSource)) return false;

       return true;
   } */


}
