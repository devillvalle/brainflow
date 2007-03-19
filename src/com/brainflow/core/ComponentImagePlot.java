package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.AnatomicalPlane;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 2, 2006
 * Time: 3:10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentImagePlot extends JComponent implements IImagePlot {

    private AxisRange xAxis;

    private AxisRange yAxis;

    private java.util.List<IAnnotation> annotationList = new ArrayList<IAnnotation>();

    //private ImagePlotRenderer renderer;

    private AnatomicalVolume displayAnatomy;

    private Insets plotInsets = new Insets(30, 30, 30, 30);

    private Rectangle plotArea = new Rectangle(300,300);

    private double scaleX = 1f;

    private double scaleY = 1f;

    private String name;



    public ComponentImagePlot(AnatomicalVolume displayAnatomy, AxisRange xAxis, AxisRange yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        this.displayAnatomy = displayAnatomy;
    }

    // not necessary?
    public void paint(Graphics2D g2, Rectangle2D area) {
        renderer.paint(g2, area, this);
    }


    public JComponent getComponent() {
        return this;
    }


    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();
        Insets insets = getInsets();


        Rectangle2D available = new Rectangle2D.Double(
                insets.left + plotInsets.left, insets.top + plotInsets.top,
                size.getWidth() - insets.left - insets.right - plotInsets.left - plotInsets.right,
                size.getHeight() - insets.top - insets.bottom - plotInsets.top - plotInsets.bottom
        );


        int drawWidth = (int) available.getWidth();
        int drawHeight = (int) available.getHeight();

        /*if (drawWidth < this.minimumDrawWidth) {
            drawWidth = this.minimumDrawWidth;

        }

        if (drawHeight < this.minimumDrawHeight) {
            drawHeight = this.minimumDrawHeight;

        }*/


        plotArea = new Rectangle(
                insets.left + plotInsets.left, insets.top + plotInsets.top, drawWidth, drawHeight
        );

        scaleX = plotArea.getWidth() / getXAxisRange().getInterval();
        scaleY = plotArea.getHeight() / getYAxisRange().getInterval();

        Color oldColor = g2.getColor();
        g2.setColor(Color.BLACK);
        g2.fillRect(insets.left, insets.top,
                (int) (size.getWidth() - insets.left - insets.right),
                (int) (size.getHeight() - insets.top - insets.bottom));


        g2.setColor(oldColor);

        renderer.paint(g2, plotArea, this);
        for (IAnnotation ia : annotationList) {
            if (ia.isVisible()) {
                ia.draw(g2, plotArea, this);
            }
        }
    }

    public Insets getPlotInsets() {
        return plotInsets;
    }


    public Rectangle getPlotArea() {
        return plotArea;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setPlotInsets(Insets insets) {
        plotInsets = insets;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;

    }

    public void updateAxis(AxisRange range) {
        if (range.getAnatomicalAxis().sameAxis(xAxis.getAnatomicalAxis())) {
            if (!range.getAnatomicalAxis().sameDirection(xAxis.getAnatomicalAxis())) {
                setXAxisRange(range.flip());
            } else {
                setXAxisRange(range);
            }
        } else if (range.getAnatomicalAxis().sameAxis(yAxis.getAnatomicalAxis())) {
            if (!range.getAnatomicalAxis().sameDirection(yAxis.getAnatomicalAxis())) {
                setYAxisRange(range.flip());
            } else {
                setYAxisRange(range);
            }
        }
    }


    public void setXAxisRange(AxisRange xrange) {
        xAxis = xrange;
    }

    public void setYAxisRange(AxisRange yrange) {
        yAxis = yrange;
    }

    public Point translateAnatToScreen(AnatomicalPoint2D pt) {
        if (pt.getAnatomy().XAXIS != getXAxisRange().getAnatomicalAxis()) {
            throw new ImageAxis.IncompatibleAxisException("supplied point does not match image plot axes: " +
                    "X: " + pt.getAnatomy().XAXIS + " Y: " + pt.getAnatomy().YAXIS +
                    " Plot X: " + getXAxisRange().getAnatomicalAxis() +
                    " Plot Y: " + getYAxisRange().getAnatomicalAxis());
        }

        Insets insets = getInsets();
        Insets plotInsets = getPlotInsets();
        int x = (int) (pt.getX() * getScaleX() + plotInsets.left + insets.left);
        int y = (int) (pt.getY() * getScaleY() + plotInsets.top + insets.top);
        return new Point(x, y);
    }


    public AnatomicalPoint2D translateScreenToAnat(Point screenPoint) {
        Insets insets = getInsets();
        Insets plotInsets = getPlotInsets();
        double x = (screenPoint.getX() - insets.left - plotInsets.left) / getScaleX();
        double y = (screenPoint.getY() - insets.top - plotInsets.top) / getScaleY();
        return new AnatomicalPoint2D(AnatomicalPlane.matchAnatomy(
                getXAxisRange().getAnatomicalAxis(),
                getYAxisRange().getAnatomicalAxis()),
                x + getXAxisRange().getMinimum(),
                y + getYAxisRange().getMinimum());
    }


    public AxisRange getXAxisRange() {
        return xAxis;
    }

    public AxisRange getYAxisRange() {
        return yAxis;
    }

    public void setRenderer(ImagePlotRenderer _renderer) {
        renderer = _renderer;
    }


    public void setAnnotations(java.util.List<IAnnotation> _annotationList) {
        annotationList = _annotationList;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String toString() {
        if (name != null) return name;
        else {
            return super.toString();
        }
    }



}
