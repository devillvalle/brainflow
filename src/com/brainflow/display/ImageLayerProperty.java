package com.brainflow.display;

import com.brainflow.core.ImageLayer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 26, 2006
 * Time: 11:30:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayerProperty<T> extends Property {


    private ImageLayer layer;

    public ImageLayerProperty(ImageLayer _layer, T param) {
        super(param);
        layer = _layer;
    }


    public ImageLayer getLayer() {
        return layer;
    }
}
