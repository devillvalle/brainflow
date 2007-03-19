package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;

import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.space.Axis;
import com.brainflow.core.ImageLayer2D;
import com.brainflow.display.ImageLayerProperties;
import com.brainflow.display.InterpolationMethod;
import com.brainflow.display.InterpolationHint;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 7:13:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResampleImagesStage extends ImageProcessingStage {

    private static final Logger log = Logger.getLogger(ResampleImagesStage.class.getName());


    public void process(StageFerry ferry) throws StageException {
        List<PipelineLayer> layers = ferry.getLayers();

        for (int i = 0; i < layers.size(); i++) {
            PipelineLayer layer = layers.get(i);
            if (layer.isVisible() && layer.getResampledImage() == null) {
                layer.setResampledImage(resample(layer.getLayer(), layer.getRawImage()));
            }
        }

        emit(ferry);

    }

    private BufferedImage resample(ImageLayer2D layer, BufferedImage source) {

        ImageLayerProperties dprops = layer.getImageLayerProperties();
        InterpolationMethod interp = dprops.getResampleInterpolation().getProperty();
        ImageSpace2D ispace = (ImageSpace2D) layer.getImageData().getImageSpace();

        double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
        double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

        double ox = ispace.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
        double oy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();

        //AffineTransform at = AffineTransform.getTranslateInstance(ox,oy);
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.scale(sx, sy);
        AffineTransformOp aop = null;


        if (interp.getInterpolation() == InterpolationHint.NEAREST_NEIGHBOR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        } else if (interp.getInterpolation() == InterpolationHint.CUBIC) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        } else if (interp.getInterpolation() == InterpolationHint.LINEAR) {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        } else {
            aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        }

        return aop.filter(source, null);


    }
}