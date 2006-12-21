/*
 * DefaultImageProcurer.java
 *
 * Created on June 30, 2006, 3:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalVolume;

import java.util.ArrayList;
import java.util.List;

/**
 * @author buchs
 */
public class DefaultImageProcurer extends AbstractImageProcurer {

    private List<ImageLayer2D> cachedRaw;


    /**
     * Creates a new instance of DefaultImageProcurer
     */
    public DefaultImageProcurer(IImageDisplayModel displayModel, AnatomicalVolume displayAnatomy) {
        super(displayModel, displayAnatomy);
    }

    public void reset() {
        cachedRaw = null;
    }

    public synchronized List<ImageLayer2D> procure() {
        if ((cachedRaw != null)) {
            return cachedRaw;
        } else {
            createRawImages();
            return cachedRaw;
        }
    }


    protected synchronized List<ImageLayer2D> createRawImages() {

        cachedRaw = new ArrayList<ImageLayer2D>();

        for (int i = 0; i < getModel().getNumLayers(); i++) {

            ImageLayer3D layer = (ImageLayer3D) getModel().getImageLayer(i);

            //if (!layer.getImageLayerParameters().getVisiblility().getParameter().isVisible()) {
            //    continue;
            //}

            ImageLayer2D layer2d = layer.getSlice(getDisplayAnatomy(), getDisplaySlice());

            cachedRaw.add(layer2d);


        }


        return cachedRaw;
    }


}
