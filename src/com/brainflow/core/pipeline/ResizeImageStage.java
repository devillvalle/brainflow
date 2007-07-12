package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.brainflow.core.IImagePlot;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 8:45:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResizeImageStage extends ImageProcessingStage {

    private static Logger log = Logger.getLogger(ResizeImageStage.class.getName());

    private BufferedImage resized = null;


    public void flush() {
        resized = null;
    }

    public Object filter(Object input) throws StageException {
        BufferedImage cropped = (BufferedImage)input;
        if (cropped != null && resized == null) {
            //System.out.println("resizing image ...");
            IImagePlot plot = getPipeline().getPlot();
            Rectangle area = plot.getPlotArea();
            double sx = area.getWidth() / cropped.getWidth();
            double sy = area.getHeight() / cropped.getHeight();

            resized = scale(cropped, 0, 0,
                    (float)sx, (float)sy);

        }

        return resized;

    }

  

    private BufferedImage scale(BufferedImage bimg, float ox, float oy, float sx, float sy) {

        AffineTransform at = AffineTransform.getTranslateInstance(ox, oy);
        at.scale(sx, sy);
        AffineTransformOp aop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return aop.filter(bimg, null);   
    }
}
