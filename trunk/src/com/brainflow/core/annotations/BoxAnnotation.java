package com.brainflow.core.annotations;

import com.brainflow.core.IImagePlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;

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


    private double xmin;

    private double ymin;

    private double width;

    private double height;

    private Paint fillPaint = new Color(0, 255, 0, 87);

    private Paint linePaint = Color.GREEN.brighter();


    public IAnnotation safeCopy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible()) return;

        System.out.println("paint box annotation");
        double percentX = (xmin - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();
        double percentY = (ymin - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();
        double percentWidth = width / plot.getXAxisRange().getInterval();
        double percentHeight = height / plot.getYAxisRange().getInterval();


        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();
        double screenWidth = percentWidth * plotArea.getWidth();
        double screenHeight = percentHeight * plotArea.getHeight();


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
        return xmin;
    }

    public void setXmin(double xmin) {
        System.out.println("changing x min in box annotation to " + xmin);
        double old = this.xmin;
        this.xmin = xmin;
        support.firePropertyChange(BoxAnnotation.XMIN_PROPERTY, old, getXmin());
    }

    public double getYmin() {
        return ymin;
    }

    public void setYmin(double ymin) {
        double old = this.ymin;
        this.ymin = ymin;
        support.firePropertyChange(BoxAnnotation.YMIN_PROPERTY, old, ymin);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        double old = this.width;
        this.width = width;
        support.firePropertyChange(BoxAnnotation.WIDTH_PROPERTY, old, width);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        double old = this.height;
        this.height = height;
        support.firePropertyChange(BoxAnnotation.HEIGHT_PROPERTY, old, height);
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
