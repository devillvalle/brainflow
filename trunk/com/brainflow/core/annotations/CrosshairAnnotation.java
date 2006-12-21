package com.brainflow.core.annotations;

import com.brainflow.core.IImagePlot;
import com.brainflow.display.Crosshair;
import com.brainflow.display.DisplayParameter;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 15, 2006
 * Time: 2:05:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairAnnotation implements IAnnotation {

    public static final Paint DEFAULT_LINE_PAINT = Color.RED;
    public static final Double DEFAULT_LINE_LENGTH = 1.0;
    public static final Double DEFAULT_LINE_WIDTH = 3.0;

    private Paint linePaint;
    private Double lineLength;
    private Stroke stroke;

    private Point location;

    private DisplayParameter<Crosshair> crosshair;

    public CrosshairAnnotation(DisplayParameter<Crosshair> _crosshair) {
        crosshair = _crosshair;
        linePaint = DEFAULT_LINE_PAINT;
        lineLength = DEFAULT_LINE_LENGTH;
        stroke = new BasicStroke(DEFAULT_LINE_WIDTH.floatValue(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
    }

    public Point getLocation() {
        return location;
    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        Crosshair cross = crosshair.getParameter();

        AnatomicalPoint1D xpt = cross.getValue(plot.getXAxisRange().getAnatomicalAxis());
        AnatomicalPoint1D ypt = cross.getValue(plot.getYAxisRange().getAnatomicalAxis());


        double percentX = (xpt.getX() - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt.getX() - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();

        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getX();

        location = new Point((int) Math.round(screenX), (int) Math.round(screenY));

        Line2D lineX = new Line2D.Double(plotArea.getMinX(), screenY, plotArea.getMaxX(), screenY);
        Line2D lineY = new Line2D.Double(screenX, plotArea.getMinY(), screenX, plotArea.getMaxY());

        g2d.setPaint(linePaint);
        g2d.setStroke(stroke);
        g2d.draw(lineX);
        g2d.draw(lineY);

    }
}
