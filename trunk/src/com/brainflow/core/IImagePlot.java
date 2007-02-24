package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 3, 2004
 * Time: 12:14:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImagePlot {

    public void setName(String name);

    public Insets getPlotInsets();

    public void setPlotInsets(Insets insets);

    public AnatomicalVolume getDisplayAnatomy();

    public void updateAxis(AxisRange range);

    public void setXAxisRange(AxisRange drange);

    public void setYAxisRange(AxisRange drange);

    public double getScaleX();

    public double getScaleY();

    public Point translateAnatToScreen(AnatomicalPoint2D pt);

    public AnatomicalPoint2D translateScreenToAnat(Point pt);

    public Rectangle getPlotArea();

    public AxisRange getXAxisRange();

    public AxisRange getYAxisRange();

    public JComponent getComponent();

    public void setRenderer(ImagePlotRenderer renderer);

    public void paint(Graphics2D g2, Rectangle2D area);

    public void setAnnotations(List<IAnnotation> annotationList);


}
