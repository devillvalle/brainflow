package com.brainflow.display;

import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.data.BasicImageData2D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 22, 2005
 * Time: 8:22:26 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImagePipeline {

    private final static Logger log = Logger.getLogger(AbstractImagePipeline.class.getName());


    protected DisplayableImageStack dstack;
    protected Rectangle2D imageBounds;
    protected Rectangle2D frameBounds;
    protected RenderedImage mergedImage;

    public AbstractImagePipeline() {
    }

    public void setDisplayableImageStack(DisplayableImageStack _dstack) {
        if (_dstack.equals(dstack)) {
            log.info("replacement stack equals current stack: no update required");
            return;
        }

        dstack = _dstack;
        computeBounds();
        mergedImage = null;
    }

    protected void computeBounds() {
        if (dstack.size() == 0) {
            log.log(Level.WARNING, "RenderableImagePipelineBackup.computeBounds(): DisplayStack is empty");
            return;
        }


        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;


        for (int i = 0; i < dstack.size(); i++) {
            DisplayableImage dimg = dstack.get(i);
            BasicImageData2D img = dimg.getImageData();
            ImageSpace2D ispace = (ImageSpace2D) img.getImageSpace();
            AxisRange xRange = ispace.getImageAxis(Axis.X_AXIS).getRange();
            AxisRange yRange = ispace.getImageAxis(Axis.Y_AXIS).getRange();

            minX = Math.min(minX, xRange.getMinimum());
            minY = Math.min(minY, yRange.getMinimum());
            maxX = Math.max(maxX, xRange.getMaximum());
            maxY = Math.max(maxY, yRange.getMaximum());
        }

        imageBounds = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        log.info("real space image bounds = " + imageBounds);

    }

    public Rectangle2D getImageBounds() {
        return (Rectangle2D) imageBounds.clone();
    }

    public Rectangle2D getFrameBounds() {
        return (Rectangle2D) frameBounds.clone();
    }

    public void setFrameAxes(Rectangle2D rect) {
        frameBounds = rect;
        mergeImages();
    }

    protected RenderedImage getMergedImage() {
        return mergedImage;
    }

    protected abstract RenderedImage mergeImages();

    public abstract RenderedImage getScreenImage(Dimension screenDim);
}
