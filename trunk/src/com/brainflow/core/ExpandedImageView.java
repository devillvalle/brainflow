package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 30, 2007
 * Time: 1:37:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExpandedImageView extends ImageView {


    public IAnnotation getAnnotation(IImagePlot plot, String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeAnnotation(IImagePlot plot, String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clearAnnotations() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAnnotation(IImagePlot plot, String name, IAnnotation annotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void intervalAdded(ListDataEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void intervalRemoved(ListDataEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void contentsChanged(ListDataEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Point getCrosshairLocation(IImagePlot plot) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point screenPoint) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IImagePlot whichPlot(Point p) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<IImagePlot> getPlots() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SelectionInList getPlotSelection() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IImagePlot getSelectedPlot() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RenderedImage captureImage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
