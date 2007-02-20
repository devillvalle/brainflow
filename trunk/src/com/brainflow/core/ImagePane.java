package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPlane;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.colormap.operations.ColorMapUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 27, 2004
 * Time: 12:03:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePane extends JComponent {


    private IImagePlot plot;

    private static final Logger log = Logger.getLogger(ImagePane.class.getName());

    private int minimumDrawWidth = 25;

    private int minimumDrawHeight = 25;






    public ImagePane(IImagePlot _plot) {
        assert _plot != null;

        plot = _plot;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(plot.getComponent(), BorderLayout.CENTER);


    }


    public RenderedImage captureImage() {
        System.out.println("capturing image");
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        //BufferedImage img = ColorMapUtils.createCompatibleImage(getWidth(), getHeight());
        plot.getComponent().paint(img.createGraphics());

        return img;
    }




    public IImagePlot getImagePlot() {
        return plot;
    }


    public Dimension getPreferredSize() {
        Insets insets = this.getInsets();
        Insets plotInsets = plot.getPlotInsets();
        double w = plot.getXAxisRange().getInterval() + plotInsets.left + plotInsets.right + insets.left + insets.right;
        double h = plot.getYAxisRange().getInterval() + plotInsets.top + plotInsets.bottom + insets.left + insets.right;
        return new Dimension((int) w, (int) h);
    }


    public boolean pointInPlot(Component source, Point sourcePoint) {
        Point point = SwingUtilities.convertPoint(source, sourcePoint, this);
        return !((point.x < 0) || (point.y < 0) || (point.x > getWidth()) || (point.y > getHeight()));

    }


    public Point translateValueToScreen(Point2D value) {
        Insets insets = getInsets();
        Insets plotInsets = plot.getPlotInsets();
        int x = (int) (value.getX() * plot.getScaleX() + insets.left + plotInsets.left);
        int y = (int) (value.getY() * plot.getScaleY() + insets.top + plotInsets.top);
        return new Point(x, y);
    }


    public AnatomicalPoint2D translateScreenToValue(Point screenPoint) {
        Insets insets = getInsets();
        Insets plotInsets = plot.getPlotInsets();
        double x = (screenPoint.getX() - insets.left - plotInsets.left) / plot.getScaleX();
        double y = (screenPoint.getY() - insets.top - plotInsets.top) / plot.getScaleY();
        return new AnatomicalPoint2D(AnatomicalPlane.matchAnatomy(
                plot.getXAxisRange().getAnatomicalAxis(),
                plot.getYAxisRange().getAnatomicalAxis()),
                x + plot.getXAxisRange().getMinimum(),
                y + plot.getYAxisRange().getMinimum());
    }

    public AnatomicalPoint2D getLocationInPlot(Component source, Point point) {
        Point p = SwingUtilities.convertPoint(source, point, this);
        return translateScreenToValue(p);
    }


}
