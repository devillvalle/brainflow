package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPlane;
import com.brainflow.image.anatomy.AnatomicalPoint2D;

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

    private double scaleX = 1f;

    private double scaleY = 1f;

    private Rectangle2D plotArea;

    private int xmargin = 35;
    private int ymargin = 35;


    public ImagePane(IImagePlot _plot) {
        assert _plot != null;

        plot = _plot;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }


    public RenderedImage captureImage() {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        paintComponent(img.createGraphics());

        return img;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.plot == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        Dimension size = getSize();
        Insets insets = getInsets();

        Rectangle2D available = new Rectangle2D.Double(
                insets.left + xmargin, insets.top + ymargin,
                size.getWidth() - insets.left - insets.right - (xmargin * 2),
                size.getHeight() - insets.top - insets.bottom - (ymargin * 2)
        );


        double drawWidth = available.getWidth();
        double drawHeight = available.getHeight();

        if (drawWidth < this.minimumDrawWidth) {
            drawWidth = this.minimumDrawWidth;

        }

        if (drawHeight < this.minimumDrawHeight) {
            drawHeight = this.minimumDrawHeight;

        }

        plotArea = new Rectangle.Double(
                insets.left + xmargin, insets.top + ymargin, drawWidth, drawHeight
        );

        scaleX = plotArea.getWidth() / plot.getXAxisRange().getInterval();
        scaleY = plotArea.getHeight() / plot.getYAxisRange().getInterval();

        Color oldColor = g2.getColor();
        g2.setColor(Color.BLACK);
        g2.fillRect(insets.left, insets.top,
                (int) (size.getWidth() - insets.left - insets.right),
                (int) (size.getHeight() - insets.top - insets.bottom));

        g2.setColor(oldColor);

        plot.paint(g2, plotArea);

    }

    public Rectangle2D getPlotArea() {
        return plotArea;
    }

    public IImagePlot getImagePlot() {
        return plot;
    }


    public Dimension getPreferredSize() {
        Insets insets = this.getInsets();
        double w = plot.getXAxisRange().getInterval() + xmargin * 2 + insets.left + insets.right;
        double h = plot.getYAxisRange().getInterval() + ymargin * 2 + insets.left + insets.right;
        return new Dimension((int) w, (int) h);
    }


    public boolean pointInPlot(Component source, Point sourcePoint) {
        Point point = SwingUtilities.convertPoint(source, sourcePoint, this);
        return !((point.x < 0) | (point.y < 0) | (point.x > getWidth()) | (point.y > getHeight()));

    }

    public Point translateValueToScreen(Point2D value) {
        Insets insets = getInsets();
        int x = (int) (value.getX() * this.scaleX + insets.left + xmargin);
        int y = (int) (value.getY() * this.scaleY + insets.top + ymargin);
        return new Point(x, y);
    }


    public AnatomicalPoint2D translateScreenToValue(Point screenPoint) {
        Insets insets = getInsets();
        double x = (screenPoint.getX() - insets.left - xmargin) / this.scaleX;
        double y = (screenPoint.getY() - insets.top - ymargin) / this.scaleY;
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
