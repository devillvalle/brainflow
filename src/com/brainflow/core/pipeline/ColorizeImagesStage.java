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
        List<PipelineLayer> layers = ferry.getLayers();

        if (layers == null) throw new StageException(this, "Cannot colorize images, layer stack is null");

        for (int i = 0; i < layers.size(); i++) {
            PipelineLayer layer = layers.get(i);
            if (layer.isVisible() && layer.getColoredImage() == null) {
                layer.setColoredImage(createRGBAImage(layer.getLayer()));
            } else {
                log.info("colorize images : passing through layer " + i);
            }
        }


        emit(ferry);

    }


    private RGBAImage createRGBAImage(ImageLayer2D layer) {
        IColorMap cmap = layer.getImageLayerProperties().getColorMap().getProperty();
        return cmap.getRGBAImage(layer.getImageData());
    }


}
