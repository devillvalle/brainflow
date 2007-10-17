package com.brainflow.core;

import com.brainflow.display.InterpolationType;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:22:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageProducer implements IImageProducer {


    private IImageDisplayModel model;

    private Anatomy3D displayAnatomy;

    private AnatomicalPoint1D slice;

    private Rectangle screenSize;

    private AxisRange xaxis;

    private AxisRange yaxis;

    private InterpolationType screenInterpolation = InterpolationType.CUBIC;


    public void setModel(IImageDisplayModel model) {
        this.model = model;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        this.displayAnatomy = displayAnatomy;
    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setSlice(AnatomicalPoint1D slice) {
        this.slice = slice;

    }

    public InterpolationType getScreenInterpolation() {
        return screenInterpolation;
    }

    public void setScreenInterpolation(InterpolationType screenInterpolation) {
        this.screenInterpolation = screenInterpolation;
    }

    public AxisRange getXAxis() {
        return xaxis;
    }

    public void setXAxis(AxisRange xaxis) {
        this.xaxis = xaxis;
    }

    public AxisRange getYAxis() {
        return yaxis;
    }

    public void setYAxis(AxisRange yaxis) {
        this.yaxis = yaxis;
    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }


    public Rectangle getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Rectangle screenSize) {
        this.screenSize = screenSize;
    }
}
