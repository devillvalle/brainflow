package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;

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
public class BasicImagePlot implements IImagePlot {

    private AxisRange xAxis;
    private AxisRange yAxis;

    private java.util.List<IAnnotation> annotationList = new ArrayList<IAnnotation>();
    private ImagePlotRenderer renderer;
    private AnatomicalVolume displayAnatomy;

    public BasicImagePlot(AnatomicalVolume displayAnatomy, AxisRange xAxis, AxisRange yAxis, ImagePlotRenderer renderer) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.renderer = renderer;
        this.displayAnatomy = displayAnatomy;
    }


    public void paint(Graphics2D g2, Rectangle2D area) {
        renderer.paint(g2, area, this);

        for (IAnnotation ia : annotationList) {
            if (ia.isVisible()) {
                ia.draw(g2, area, this);
            }
        }
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
}
