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
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.data.IImageData;


/**
 * @author buchs
 */


public class ImageLayer {

    private final ImageLayerParameters parameters;
    private ILoadableImage limg;


    public ImageLayer(ILoadableImage _limg, ImageLayerParameters _params) {
        limg = _limg;
        parameters = _params;
    }

    public IImageData getImageData() {
        return limg.getData();
    }

    public ILoadableImage getLoadableImage() {
        return limg;
    }

    public ImageLayerParameters getImageLayerParameters() {
        return parameters;
    }

    public String toString() {
        return limg.getStem();
    }


}
