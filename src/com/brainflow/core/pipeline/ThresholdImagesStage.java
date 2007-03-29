package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.util.List;
import java.util.logging.Logger;

import com.brainflow.display.ThresholdRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 28, 2007
 * Time: 10:19:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdImagesStage extends ImageProcessingStage  {

    private static final Logger log = Logger.getLogger(ThresholdImagesStage.class.getName());



    public void process(StageFerry ferry) throws StageException {
        List<PipelineLayer> layers = ferry.getLayers();

        for (int i = 0; i < layers.size(); i++) {
            PipelineLayer layer = layers.get(i);
            ThresholdRange range = layer.getLayer().getImageLayerProperties().getThresholdRange().getProperty();
            if (layer.isVisible() && layer.getMaskedColoredImage() == null) {
                log.info("thresh : " + range);
                layer.setMaskedColoredImage(layer.getColoredImage());
            } else {
                log.info("resample stage : passing through layer " + i);
            }
        }

        emit(ferry);

    }
}
