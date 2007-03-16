package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.brainflow.display.ImageLayerProperties;
import com.brainflow.core.ImageLayer2D;
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

    private static final Logger log = Logger.getLogger(ColorizeImagesStage.class.getName());

    public void process(StageFerry ferry) throws StageException {
        List<ImageLayer2D> layers = ferry.getImageLayerStack();

        if (layers == null) throw new StageException(this, "Cannot colorize images, image layer stack is null");

        List<RGBAImage> imageList = ferry.getRGBAImageLayerStack();

        if (imageList == null || imageList.size() != ferry.getModel().getNumLayers()) {
            imageList = new ArrayList<RGBAImage>();
            int n = ferry.getModel().getNumLayers();
            for (int i=0; i<n; i++) {
                if (ferry.getModel().getImageLayer(i).isVisible()) {
                    imageList.add(createRGBAImage(layers.get(i)));
                } else {
                    imageList.add(null);
                }
            }

            ferry.setRGBAImageLayerStack(imageList);
        }

        emit(ferry);
      
    }


    private RGBAImage createRGBAImage(ImageLayer2D layer) {
        IColorMap cmap = layer.getImageLayerProperties().getColorMap().getProperty();
        return cmap.getRGBAImage(layer.getImageData());
    }

    



}
