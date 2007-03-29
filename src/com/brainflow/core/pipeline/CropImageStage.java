package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;
import com.brainflow.core.IImagePlot;

import java.awt.image.BufferedImage;

import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2007
 * Time: 7:58:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CropImageStage extends ImageProcessingStage {

    private static Logger log = Logger.getLogger(CropImageStage.class.getName());

    public void process(StageFerry ferry) throws StageException {
        log.entering(getClass().getName(), "process");
        if (ferry.getCompositeImage() != null && ferry.getCroppedImage() == null) {
            ImagePlotPipeline pipe = getPipeline();
            IImagePlot plot = pipe.getPlot();
            Rectangle2D bounds = ImagePlotPipeline.getBounds(ferry.getLayers());
            log.info("bounds  : " + bounds);
            Rectangle2D region = new Rectangle2D.Double(plot.getXAxisRange().getMinimum(),
                plot.getYAxisRange().getMinimum(),
                plot.getXAxisRange().getInterval(),
                plot.getYAxisRange().getInterval());

            log.info("region  : " + region);

            BufferedImage composite = ferry.getCompositeImage();
            BufferedImage cropped = crop(bounds, region, composite );
            ferry.setCroppedImage(cropped);
        }
        
        emit(ferry);

    }

    private BufferedImage crop(Rectangle2D bounds, Rectangle2D region, BufferedImage image) {
        if (bounds.equals(region)) {
            return image;
        }

        int xmin = (int) Math.floor(region.getX() - bounds.getX());
        int ymin = (int) Math.floor(region.getY() - bounds.getY());
        int width = (int) Math.min(bounds.getWidth() - xmin, region.getWidth());
        int height = (int) Math.min(bounds.getHeight() - ymin, region.getHeight());

        return image.getSubimage(xmin, ymin, width, height);

    }
}
