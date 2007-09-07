package com.brainflow.core.rendering;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.util.List;

import com.brainflow.core.CoordinateLayer;
import com.brainflow.core.AbstractLayer;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 5:31:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class RenderCoordinatesStage extends ImageProcessingStage {



    public void flush() {

    }

    public Object filter(Object input) throws StageException {
        BufferedImage compositeImage = (BufferedImage) input;

        int nlayers = getModel().getNumLayers();
        for (int i=0; i<nlayers; i++) {
            AbstractLayer layer = getModel().getLayer(i);
            if (layer instanceof CoordinateLayer) {
                CoordinateLayer clayer = (CoordinateLayer)layer;
                renderUnto(clayer, compositeImage);
            }

        }
        return compositeImage;
    }

    private void renderUnto(CoordinateLayer layer, BufferedImage image) {
        List<AnatomicalPoint3D> pts = layer.getData().pointsWithinPlane(getSlice());
        for (int i=0; i<pts.size(); i++) {
            System.out.println("point within plance: " + pts.get(i));
        }

    }





}
