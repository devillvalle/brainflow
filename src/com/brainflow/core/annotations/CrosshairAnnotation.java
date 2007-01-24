package com.brainflow.core.annotations;

import com.brainflow.core.IImagePlot;
import com.brainflow.display.Crosshair;
import com.brainflow.display.DisplayParameter;
import com.brainflow.image.anatomy.AnatomicalPoint1D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 15, 2006
 * Time: 2:05:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairAnnotation implements IAnnotation {

    public static final String LINE_PAINT_PROPERTY = "linePaint";
    public static final String LINE_LENGTH_PROPERTY = "lineLength";
    public static final String LINE_WIDTH_PROPERTY = "lineWidth";
    public static final String GAP_PROPERTY = "gap";

    public static final String VISIBLE_PROPERTY = "visible";


    public static final Paint DEFAULT_LINE_PAINT = Color.GREEN;
    public static final Float DEFAULT_LINE_LENGTH = 1.0f;
    public static final Float DEFAULT_LINE_WIDTH = 1.0f;
    public static final Integer DEFAULT_GAP = 0;

    private Paint linePaint = DEFAULT_LINE_PAINT;
    private double lineLength = DEFAULT_LINE_LENGTH;
    private double lineWidth = DEFAULT_LINE_WIDTH;

    private Integer gap = DEFAULT_GAP;

    private Stroke stroke;

    private Point location;

    private boolean visible = true;

    private DisplayParameter<Crosshair> crosshair;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public CrosshairAnnotation(DisplayParameter<Crosshair> _crosshair) {
        crosshair = _crosshair;
        linePaint = DEFAULT_LINE_PAINT;
        lineLength = DEFAULT_LINE_LENGTH.doubleValue();

        resetStroke();

    }

    private void resetStroke() {
        stroke = new BasicStroke((float) lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    }

    /*public Point getLocation() {
        return location;
    }*/

    public IAnnotation safeCopy() {
        CrosshairAnnotation annot = new CrosshairAnnotation(crosshair);
        annot.visible = visible;
        annot.location = new Point(location);
        annot.linePaint = linePaint;
        annot.stroke = stroke;
        annot.lineLength = lineLength;
        annot.lineWidth = lineWidth;
        annot.gap = gap;

        return annot;

    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) return;

        Crosshair cross = crosshair.getParameter();

        AnatomicalPoint1D xpt = cross.getValue(plot.getXAxisRange().getAnatomicalAxis());
        AnatomicalPoint1D ypt = cross.getValue(plot.getYAxisRange().getAnatomicalAxis());


        double percentX = (xpt.getX() - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt.getX() - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();

        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

        location = new Point((int) Math.round(screenX), (int) Math.round(screenY));

        //double width = Math.max(lineLength * plotArea.getWidth(), lineLength * plotArea.getHeight());
        g2d.setPaint(linePaint);
        g2d.setStroke(stroke);

        if (gap == 0) {
            Line2D lineX = new Line2D.Double(plotArea.getMinX(), screenY, plotArea.getMaxX(), screenY);
            Line2D lineY = new Line2D.Double(screenX, plotArea.getMinY(), screenX, plotArea.getMaxY());
            g2d.draw(lineX);
            g2d.draw(lineY);
        } else {


            Line2D lineXLeft = new Line2D.Double(plotArea.getMinX(), screenY, screenX - gap, screenY);
            Line2D lineXRight = new Line2D.Double(screenX + gap, screenY, plotArea.getMaxX(), screenY);
            Line2D lineYTop = new Line2D.Double(screenX, plotArea.getMinY(), screenX, screenY - gap);
            Line2D lineYBottom = new Line2D.Double(screenX, screenY + gap, screenX, plotArea.getMaxY());

            g2d.draw(lineXLeft);
            g2d.draw(lineXRight);
            g2d.draw(lineYTop);
            g2d.draw(lineYBottom);

        }

    }


    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        Paint oldPaint = this.linePaint;
        this.linePaint = linePaint;

        support.firePropertyChange(CrosshairAnnotation.LINE_PAINT_PROPERTY, oldPaint, getLinePaint());
    }

    public double getLineLength() {
        return lineLength;
    }

    public void setLineLength(double lineLength) {
        double old = getLineLength();
        this.lineLength = lineLength;

        support.firePropertyChange(CrosshairAnnotation.LINE_LENGTH_PROPERTY, old, getLineLength());
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        double old = getGap();
        this.gap = gap;
        support.firePropertyChange(CrosshairAnnotation.GAP_PROPERTY, old, getGap());
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }


    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        double old = this.lineWidth;
        this.lineWidth = lineWidth;

        resetStroke();
        support.firePropertyChange(CrosshairAnnotation.LINE_WIDTH_PROPERTY, old, getLineWidth());
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean old = this.visible;
        this.visible = visible;
        support.firePropertyChange(CrosshairAnnotation.VISIBLE_PROPERTY, old, isVisible());
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
