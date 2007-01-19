/*
 * ImageLayer.java
 *
 * Created on June 29, 2006, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.data.IImageData;


/**
 * @author buchs
 */


public class ImageLayer {

    private ImageLayerParameters parameters;
    private IImageData data;


    public ImageLayer(IImageData _data) {
        data = _data;
        parameters = new ImageLayerParameters(this);
    }

    public ImageLayer(IImageData _data, ImageLayerParameters _params) {
        data = _data;
        parameters = _params;
    }

    public IImageData getImageData() {
        return data;
    }

    public ImageLayerParameters getImageLayerParameters() {
        return parameters;
    }
    


    public String toString() {
        return data.getImageLabel();
    }


}
