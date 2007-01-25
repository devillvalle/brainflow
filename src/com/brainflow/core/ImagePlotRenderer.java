package com.brainflow.core;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 3, 2006
 * Time: 1:07:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePlotRenderer {


    private IImageCompositor compositor;
    private AbstractImageProcurer procurer;


    public ImagePlotRenderer(IImageCompositor _compositor, AbstractImageProcurer _procurer) {
        compositor = _compositor;
        procurer = _procurer;
    }

    public void paint(Graphics2D g2, Rectangle2D area, IImagePlot iplot) {
        java.util.List<ImageLayer2D> list = procurer.procure();
        if (list.isEmpty()) {
            g2.fillRect((int) area.getX(), (int) area.getY(), (int) area.getWidth(), (int) area.getHeight());
        }


        compositor.setImageList(list);

        Rectangle2D cropRect = new Rectangle2D.Double(iplot.getXAxisRange().getMinimum(),
                iplot.getYAxisRange().getMinimum(),
                iplot.getXAxisRange().getInterval(),
                iplot.getYAxisRange().getInterval());


        RenderedImage rimg = compositor.crop(cropRect);
        //RenderedImage rimg = compositor.compose();

        double sx = area.getWidth() / cropRect.getWidth();
        double sy = area.getHeight() / cropRect.getHeight();


        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        Shape oldClip = g2.getClip();
        g2.setClip(area);

        AffineTransform trans = AffineTransform.getTranslateInstance(area.getX(), area.getY());
        trans.scale(sx, sy);

        g2.drawRenderedImage(rimg, trans);

        g2.setClip(oldClip);

    }


}
