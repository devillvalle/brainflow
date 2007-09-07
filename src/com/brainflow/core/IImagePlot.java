package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.display.InterpolationHint;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 3, 2004
 * Time: 12:14:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImagePlot {

    public InterpolationHint getScreenInterpolation();

    public void setScreenInterpolation(InterpolationHint hint);

    public void setPreserveAspectRatio(boolean b);

    public boolean isPreserveAspectRatio();

    public void setName(String name);

    public Insets getPlotInsets();

    public Insets getPlotMargins();

    public void setPlotInsets(Insets insets);

    public Anatomy3D getDisplayAnatomy();

    public IImageDisplayModel getModel();

    public void updateAxis(AxisRange range);

    public void setXAxisRange(AxisRange drange);

    public void setYAxisRange(AxisRange drange);

    public double getScaleX();

    public double getScaleY();

    public void setSlice(AnatomicalPoint1D slice);

    public AnatomicalPoint1D getSlice();

    public Point translateAnatToScreen(AnatomicalPoint2D pt);

    public AnatomicalPoint2D translateScreenToAnat(Point pt);

    public Rectangle getPlotArea();

    public double getXExtent();

    public double getYExtent();

    public AxisRange getXAxisRange();

    public AxisRange getYAxisRange();

    public JComponent getComponent();

    public IImageProducer getImageProducer();

    public void setImageProducer(IImageProducer producer);

    public Map<String, IAnnotation> getAnnotations();

    public void setAnnotation(String name, IAnnotation annotation);

    public IAnnotation getAnnotation(String name);

    public void removeAnnotation(String name);

    public void clearAnnotations();


}
