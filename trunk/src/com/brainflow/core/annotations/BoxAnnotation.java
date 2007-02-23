package com.brainflow.core.annotations;

import com.brainflow.core.IImagePlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 22, 2007
 * Time: 3:02:29 PM
 */
public class BoxAnnotation extends AbstractAnnotation {

    public static final String ID = "box";

    public static final String XMIN_PROPERTY = "xmin";
    public static final String YMIN_PROPERTY = "ymin";
    public static final String WIDTH_PROPERTY = "width";
    public static final String HEIGHT_PROPERTY = "height";

    public static final String FILLPAINT_PROPERTY = "fillPaint";
    public static final String LINEPAINT_PROPERTY = "linePaint";

    private Rectangle2D rect = new Rectangle2D.Double();



    private Paint fillPaint = new Color(0, 255, 0, 87);

    private Paint linePaint = Color.GREEN.brighter();


    public IAnnotation safeCopy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public boolean containsPoint(IImagePlot plot, Point plotPoint) {
        Insets plotInsets = plot.getPlotInsets();
        double x = (plotPoint.getX() - plotInsets.left) / plot.getScaleX();
        double y = (plotPoint.getY() - plotInsets.top) / plot.getScaleY();
        x = x + plot.getXAxisRange().getMinimum();
        y = y + plot.getYAxisRange().getMinimum();

        if (rect.contains(x,y)) {
            return true;
        } else {
            return false;
        }
    }

    public Point2D translateFromJava2D(IImagePlot plot, Point plotPoint) {
        Insets plotInsets = plot.getPlotInsets();
        double x = (plotPoint.getX() - plotInsets.left) / plot.getScaleX();
        double y = (plotPoint.getY() - plotInsets.top) / plot.getScaleY();
        x = x + plot.getXAxisRange().getMinimum();
        y = y + plot.getYAxisRange().getMinimum();

        return new Point2D.Double(x,y);

    }

    public double getNormalizedX(IImagePlot plot, double val) {
        return (val - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();

    }

    public double getNormalizedY(IImagePlot plot, double val) {
        return (val - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();
    }

    public double getNormalizedWidth(IImagePlot plot, double val) {
        return val / plot.getXAxisRange().getInterval();
    }

    public double getNormalizedHeight(IImagePlot plot, double val) {
        return val / plot.getYAxisRange().getInterval();
    }



    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) return;


        double nX = getNormalizedX(plot, getXmin());
        double nY = getNormalizedY(plot, getYmin());
        double nW = getNormalizedWidth(plot, getWidth());
        double nH = getNormalizedHeight(plot, getHeight());


        double screenX = (nX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (nY * plotArea.getHeight()) + plotArea.getY();
        double screenWidth = nW * plotArea.getWidth();
        double screenHeight = nH * plotArea.getHeight();


        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(linePaint);
        g2d.drawRect((int) screenX, (int) screenY, (int) screenWidth, (int) screenHeight);
        g2d.setPaint(fillPaint);
        g2d.fillRect((int) (screenX + 1), (int) (screenY + 1), (int) (screenWidth - 1), (int) (screenHeight - 1));

        g2d.setPaint(oldPaint);

    }

    public String getIdentifier() {
        return "box";
    }


    public double getXmin() {
        return rect.getX();
    }

    public void setXmin(double xmin) {
        System.out.println("changing x min in box annotation to " + xmin);
        double old = getXmin();
        rect.setRect(xmin, rect.getY(), rect.getWidth(), rect.getHeight());
        support.firePropertyChange(BoxAnnotation.XMIN_PROPERTY, old, getXmin());
    }

    public double getYmin() {
        return rect.getY();
    }

    public void setYmin(double ymin) {
        double old = getYmin();

        rect.setRect(rect.getX(), ymin, rect.getWidth(), rect.getHeight());
        support.firePropertyChange(BoxAnnotation.YMIN_PROPERTY, old, getYmin());
    }

    public double getWidth() {
        return rect.getWidth();
    }

    public void setWidth(double width) {
        double old = getWidth();
        rect.setRect(getXmin(), getYmin(), width, rect.getHeight());
        support.firePropertyChange(BoxAnnotation.WIDTH_PROPERTY, old, getWidth());
    }

    public double getHeight() {
        return rect.getHeight();
    }

    public void setHeight(double height) {
        double old = getHeight();
         rect.setRect(getXmin(), getYmin(), rect.getWidth(), height);
        support.firePropertyChange(BoxAnnotation.HEIGHT_PROPERTY, old, getHeight());
    }


    public Paint getFillPaint() {
        return fillPaint;
    }

    public void setFillPaint(Paint fillPaint) {
        Paint old = fillPaint;
        this.fillPaint = fillPaint;
        support.firePropertyChange(BoxAnnotation.FILLPAINT_PROPERTY, old, fillPaint);
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        Paint old = linePaint;
        this.linePaint = linePaint;
        support.firePropertyChange(BoxAnnotation.LINEPAINT_PROPERTY, old, linePaint);

    }
}
