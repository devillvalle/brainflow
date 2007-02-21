package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;

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
public class BasicImagePlot extends JComponent implements IImagePlot {

    private AxisRange xAxis;
    private AxisRange yAxis;

    private java.util.List<IAnnotation> annotationList = new ArrayList<IAnnotation>();
    private ImagePlotRenderer renderer;
    private AnatomicalVolume displayAnatomy;

    private Insets plotInsets = new Insets(30, 30, 30, 30);
    private Rectangle plotArea;

    private double scaleX = 1f;
    private double scaleY = 1f;

    private String name;

    public BasicImagePlot(AnatomicalVolume displayAnatomy, AxisRange xAxis, AxisRange yAxis, ImagePlotRenderer renderer) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.renderer = renderer;
        this.displayAnatomy = displayAnatomy;
    }




    public void paint(Graphics2D g2, Rectangle2D area) {
        renderer.paint(g2, area, this);


    }


    public JComponent getComponent() {
        return this;
    }


    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("painting plot ... ");
        Graphics2D g2 = (Graphics2D)g;
        Dimension size = getSize();
        Insets insets = getInsets();



        Rectangle2D available = new Rectangle2D.Double(
                insets.left + plotInsets.left, insets.top + plotInsets.top,
                size.getWidth() - insets.left - insets.right - plotInsets.left - plotInsets.right,
                size.getHeight() - insets.top - insets.bottom - plotInsets.top - plotInsets.bottom
        );


        int drawWidth = (int)available.getWidth();
        int drawHeight = (int)available.getHeight();

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

        renderer.paint(g2, plotArea, this );
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
