package com.brainflow.core;

import com.brainflow.core.ImageLayerProperties;
import com.brainflow.image.data.IImageData2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayer2D  {

    private IImageData2D data;

    private ImageLayerProperties properties;

    public ImageLayer2D(IImageData2D _data, ImageLayerProperties _params) {
        data = _data;
        properties = _params;
    }

    public IImageData2D getImageData() {
        return data;
    }

    public ImageLayerProperties getImageLayerProperties() {
        return properties;
    }

    public boolean isVisible() {
        return properties.getVisible().getProperty().isVisible();
    }

    public double getOpacity() {
        return properties.getOpacity().getProperty().getOpacity();
    }





}
