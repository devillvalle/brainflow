package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.util.List;

import com.brainflow.core.ImageLayer2D;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.Axis;
import com.brainflow.image.rendering.RenderUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 2:50:45 PM
 * To change this template use File | Settings | File Templates.
 */


public class ComposeImagesStage extends ImageProcessingStage {


    public void process(StageFerry ferry) throws StageException {
        List<PipelineLayer> layers = ferry.getLayers();

        BufferedImage composite = ferry.getCompositeImage();

        if (composite == null) {
            if (layers.size() == 0) {
                ferry.setCompositeImage(null);
            }
            else if (layers.size() == 1) {
                if (layers.get(0).isVisible()) {
                    ferry.setCompositeImage(layers.get(0).getResampledImage());
                } else {
                    ferry.setCompositeImage(null);
                }
            } else {
                ferry.setCompositeImage(compose(layers));
            }

        }

        emit(ferry);
    }

    private BufferedImage compose(List<PipelineLayer> layers) {

        Rectangle2D frameBounds = getBounds(layers);

        BufferedImage sourceImage = RenderUtils.createCompatibleImage((int) frameBounds.getWidth(),
                (int) frameBounds.getHeight());

        Graphics2D g2 = sourceImage.createGraphics();

        for (int i = 0; i < layers.size(); i++) {
            ImageLayer2D layer2d = layers.get(i).getLayer();

            if (layer2d.isVisible()) {
                RenderedImage rim = layers.get(i).getResampledImage();
                IImageSpace space = layer2d.getImageData().getImageSpace();
                double minx = space.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
                double miny = space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();

                double transx = (minx - frameBounds.getMinX()); //+ (-frameBounds.getMinX());
                double transy = (miny - frameBounds.getMinY()); //+ (-frameBounds.getMinY());

                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer2d.getOpacity());
                g2.setComposite(composite);
                g2.drawRenderedImage(rim, AffineTransform.getTranslateInstance(transx, transy));

            }
        }

        g2.dispose();


        return sourceImage;
    }

    private Rectangle2D getBounds(List<PipelineLayer> layers) {
        if (layers == null || layers.size() == 0) {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (PipelineLayer layer : layers) {
            IImageData2D d2 = (IImageData2D) layer.getLayer().getImageData();
            IImageSpace space = d2.getImageSpace();
            minX = Math.min(minX, space.getImageAxis(Axis.X_AXIS).getRange().getMinimum());
            minY = Math.min(minY, space.getImageAxis(Axis.Y_AXIS).getRange().getMinimum());
            maxX = Math.max(maxX, space.getImageAxis(Axis.X_AXIS).getRange().getMaximum());
            maxY = Math.max(maxY, space.getImageAxis(Axis.Y_AXIS).getRange().getMaximum());
        }

        Rectangle2D imageBounds = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        return imageBounds;
    }


}
