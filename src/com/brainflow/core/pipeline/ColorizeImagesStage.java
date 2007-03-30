package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.brainflow.display.ImageLayerProperties;
import com.brainflow.core.ImageLayer2D;
import com.brainflow.core.ImageLayer;
import com.brainflow.colormap.IColorMap;
import com.brainflow.image.data.RGBAImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 1, 2007
 * Time: 7:44:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorizeImagesStage extends ImageProcessingStage {

    private List<RGBAImage> images;

    private static final Logger log = Logger.getLogger(ColorizeImagesStage.class.getName());

   

    public Object filter(Object input) throws StageException {
        List<ImageLayer2D> layers = (List<ImageLayer2D>)input;

        if (images == null || images.size() != getModel().getNumLayers()) {
            images = new ArrayList<RGBAImage>();
            for (int i=0; i<getModel().getNumLayers(); i++) {
                ImageLayer2D layer = layers.get(i);
                if (layer != null && layer.isVisible()) {
                    images.add(createRGBAImage(layer));
                } else {
                    images.add(null);
                }
            }
        }

        return images;


    }

    private RGBAImage createRGBAImage(ImageLayer2D layer) {
        IColorMap cmap = layer.getImageLayerProperties().getColorMap().getProperty();
        return cmap.getRGBAImage(layer.getImageData());
    }


}
