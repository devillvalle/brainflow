package com.brainflow.display;

import com.brainflow.image.data.BasicImageData2D;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class DisplayableImage {

    private BasicImageData2D image;
    private ImageLayerProperties properties;


    public DisplayableImage(BasicImageData2D _image, ImageLayerProperties _properties) {
        image = _image;
        properties = _properties;
    }

    public BasicImageData2D getImageData() {
        return image;
    }

    public ImageLayerProperties getDisplayProperties() {
        return properties;
    }


}