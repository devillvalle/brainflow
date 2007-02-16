package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 19, 2006
 * Time: 10:03:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleOrthogonalImageView extends ImageView {


    private ImageView axialView;
    private ImageView coronalView;
    private ImageView sagittalView;


    private ArrayListModel plotList = new ArrayListModel();
    private SelectionInList plotSelection;


    public SimpleOrthogonalImageView(IImageDisplayModel imodel) {
        super(imodel);
        initView();

        plotSelection = new SelectionInList((ListModel) plotList);
        plotSelection.setSelectionIndex(0);
    }

    public void scheduleRepaint(DisplayChangeEvent e) {
        // repainting handled by simple image views
    }

    public Point getCrosshairLocation(IImagePlot plot) {
        List<IImagePlot> plots = getPlots();
        int idx = plots.indexOf(plot);

        if (idx == 0) {

            Point p = axialView.getCrosshairLocation(plot);
            return SwingUtilities.convertPoint(axialView, p, this);
        } else if (idx == 1) {
            Point p = coronalView.getCrosshairLocation(plot);
            return SwingUtilities.convertPoint(coronalView, p, this);
        } else if (idx == 2) {
            Point p = sagittalView.getCrosshairLocation(plot);
            return SwingUtilities.convertPoint(sagittalView, p, this);


        } else {
            return null;
        }

    }


    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point screenPoint) {

        // fix me this is ugly

        Point pointInView = SwingUtilities.convertPoint(source, screenPoint, this);
        ImageView subView = whichView(pointInView);
        if (subView != null) {
            AnatomicalPoint3D ap = subView.getAnatomicalLocation(source, screenPoint);
            return ap;
        } else {
            return null;
        }
    }


    public void addAnnotation(IAnnotation annotation) {
        axialView.addAnnotation(annotation);
    }

    public void setAnnotation(IAnnotation annotation) {
        axialView.setAnnotation(annotation);
    }

    public void removeAnnotation(IAnnotation annotation) {
        axialView.removeAnnotation(annotation);
    }

    public IAnnotation getAnnotation(Class clazz) {
        return axialView.getAnnotation(clazz);
    }

    public Iterator<IAnnotation> annotationIterator() {
        return axialView.annotationIterator();
    }

    protected List<IAnnotation> getAnnotations() {
        return axialView.getAnnotations();
    }

    private ImageView whichView(Point p) {
        if (axialView.whichPlot(SwingUtilities.convertPoint(this, p, axialView)) != null) {
            return axialView;
        }
        if (coronalView.whichPlot(SwingUtilities.convertPoint(this, p, coronalView)) != null) {
            return coronalView;
        }
        if (sagittalView.whichPlot(SwingUtilities.convertPoint(this, p, sagittalView)) != null) {
            return sagittalView;
        }

        return null;


    }

    public IImagePlot whichPlot(Point p) {
        if (axialView.whichPlot(SwingUtilities.convertPoint(this, p, axialView)) != null)
            return axialView.getPlots().get(0);
        if (coronalView.whichPlot(SwingUtilities.convertPoint(this, p, coronalView)) != null)
            return coronalView.getPlots().get(0);
        if (sagittalView.whichPlot(SwingUtilities.convertPoint(this, p, sagittalView)) != null)
            return sagittalView.getPlots().get(0);

        return null;
    }


    public SelectionInList getPlotSelection() {
        return plotSelection;
    }

    public IImagePlot getSelectedPlot() {
        return (IImagePlot) plotSelection.getSelection();
    }

    public RenderedImage captureImage() {
        throw new UnsupportedOperationException("SimpleOrthogonalImageView does not support method \"captureImage\" yet.");
    }

    public List<IImagePlot> getPlots() {
        List<IImagePlot> ret = new ArrayList<IImagePlot>();
        for (int i = 0; i < plotList.size(); i++) {
            ret.add((IImagePlot) plotList.get(i));
        }

        return ret;


    }

    private void initView() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);

        axialView = ImageViewFactory.createAxialView(getImageDisplayModel());
        axialView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        plotList.add(axialView.getPlots().get(0));

        coronalView = ImageViewFactory.createCoronalView(axialView);
        coronalView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sagittalView = ImageViewFactory.createSagittalView(axialView);
        sagittalView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setLayout(layout);
        add(axialView);
        add(coronalView);
        add(sagittalView);
    }

    public String toString() {
        return "SimpleOrthogonalImageView -- " + getImageDisplayModel().getName();
    }


}
