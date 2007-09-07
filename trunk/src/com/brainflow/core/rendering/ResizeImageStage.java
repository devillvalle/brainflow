package com.brainflow.core.rendering;

import com.brainflow.core.IImagePlot;
import com.brainflow.display.InterpolationHint;
import org.apache.commons.pipeline.StageException;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

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
        //System.out.println("flushing resize stage ");
        resized = null;
    }

    public Object filter(Object input) throws StageException {
        BufferedImage cropped = (BufferedImage) input;
        if (cropped != null && resized == null) {
            //System.out.println("resizing image ...");
            IImagePlot plot = getPipeline().getPlot();

            Rectangle area = plot.getPlotArea();
            double sx = area.getWidth() / cropped.getWidth();
            double sy = area.getHeight() / cropped.getHeight();

            resized = scale(cropped, 0, 0,
                    (float) sx, (float) sy, plot.getScreenInterpolation());

        } else {
            //System.out.println("returning cached resized image");
        }

        return resized;

    }


    private BufferedImage scale(BufferedImage bimg, float ox, float oy, float sx, float sy, InterpolationHint hint) {

        AffineTransform at = AffineTransform.getTranslateInstance(ox, oy);
        at.scale(sx, sy);
        AffineTransformOp aop = new AffineTransformOp(at, hint.getID());
        return aop.filter(bimg, null);
    }
}
