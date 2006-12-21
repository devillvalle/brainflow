package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 19, 2006
 * Time: 10:03:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleOrthogonalImageView extends ImageView {


    private SimpleImageView axialView;
    private SimpleImageView coronalView;
    private SimpleImageView sagittalView;

    private List<IImagePlot> plotList;


    public SimpleOrthogonalImageView(IImageDisplayModel imodel) {
        super(imodel);
        initView();
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
            Point ret = SwingUtilities.convertPoint(sagittalView, p, this);
            System.out.println("sag loc: " + p);
            System.out.println("view loc: " + ret);
            return ret;

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

    private ImageView whichView(Point p) {
        if (axialView.whichPlot(SwingUtilities.convertPoint(this, p, axialView)) != null) {
            System.out.println("view is axial");
            return axialView;
        }
        if (coronalView.whichPlot(SwingUtilities.convertPoint(this, p, coronalView)) != null) {
            System.out.println("view is coronal");
            return coronalView;
        }
        if (sagittalView.whichPlot(SwingUtilities.convertPoint(this, p, sagittalView)) != null) {
            System.out.println("view is sagittal");
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

    public List<IImagePlot> getPlots() {
        if (plotList == null) {
            plotList = new ArrayList<IImagePlot>();
            plotList.addAll(axialView.getPlots());
            plotList.addAll(coronalView.getPlots());
            plotList.addAll(sagittalView.getPlots());
        }
        return plotList;
    }

    private void initView() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

        axialView = new SimpleImageView(getImageDisplayModel(), AnatomicalVolume.getCanonicalAxial());
        axialView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        coronalView = new SimpleImageView(getImageDisplayModel(), AnatomicalVolume.getCanonicalCoronal());
        coronalView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sagittalView = new SimpleImageView(getImageDisplayModel(), AnatomicalVolume.getCanonicalSagittal());
        sagittalView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setLayout(layout);
        add(axialView);
        add(coronalView);
        add(sagittalView);
    }


}
