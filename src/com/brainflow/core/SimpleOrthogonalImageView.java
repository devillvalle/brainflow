package com.brainflow.core;

import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.application.toplevel.ImageViewFactory;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.image.RenderedImage;
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


    private ImagePane axialPane;

    private ImagePane coronalPane;

    private ImagePane sagittalPane;


    private ArrayListModel plotList = new ArrayListModel();
    
    private SelectionInList plotSelection;


    public SimpleOrthogonalImageView(IImageDisplayModel imodel) {
        super(imodel);
        initView();

        plotSelection = new SelectionInList((ListModel) plotList);
        plotSelection.setSelectionIndex(0);
    }



    public void scheduleRepaint(ImageLayerEvent e) {
        repaint();
    }


    public SliceController getSliceController() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Point getCrosshairLocation(ImagePane ipane) {
        IImagePlot plot = ipane.getImagePlot();
        Rectangle plotArea = plot.getPlotArea();

        ICrosshair cross = getCrosshair();
        AnatomicalPoint1D xpt = cross.getValue(plot.getDisplayAnatomy().XAXIS);
        AnatomicalPoint1D ypt = cross.getValue(plot.getDisplayAnatomy().YAXIS);

        double percentX = (xpt.getX() - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt.getX() - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();


        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

        Point location = new Point((int) Math.round(screenX), (int) Math.round(screenY));

        return SwingUtilities.convertPoint(ipane, location, this);

    }

    public Point getCrosshairLocation(IImagePlot plot) {
        List<IImagePlot> plots = getPlots();
        int idx = plots.indexOf(plot);

        if (idx == 0) {
            Point p = getCrosshairLocation(axialPane);
            return SwingUtilities.convertPoint(axialPane, p, this);
        } else if (idx == 1) {
            Point p = getCrosshairLocation(coronalPane);
            return SwingUtilities.convertPoint(coronalPane, p, this);
        } else if (idx == 2) {
            Point p = getCrosshairLocation(sagittalPane);
            return SwingUtilities.convertPoint(sagittalPane, p, this);
        } else {
            throw new RuntimeException("Should never get here.");
        }

    }

    private ImagePane whichPane(IImagePlot plot) {
        if (axialPane.getImagePlot() == plot) {
            return axialPane;
        }

        if (coronalPane.getImagePlot() == plot) {
            return coronalPane;
        }

        if (sagittalPane.getImagePlot() == plot) {
            return sagittalPane;
        }

        throw new RuntimeException("Should never get here.");

    }

    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point p) {

        Point pointInView = SwingUtilities.convertPoint(source, p, this);
        // fix me this is ugly
        IImagePlot plot = whichPlot(pointInView);


        if (plot == null) {
            //todo deal with more elegantly
            return null;
        }

        ImagePane ipane = whichPane(plot);

        Point panePoint = SwingUtilities.convertPoint(source, p, ipane);

        AnatomicalPoint2D apoint = ipane.translateScreenToValue(panePoint);

        Anatomy3D displayAnatomy = plot.getDisplayAnatomy();
        AnatomicalPoint3D ap3d = new AnatomicalPoint3D(
                Anatomy3D.matchAnatomy(
                        plot.getXAxisRange().getAnatomicalAxis(),
                        plot.getYAxisRange().getAnatomicalAxis(),
                        plot.getDisplayAnatomy().ZAXIS),
                apoint.getX(), apoint.getY(),
                getCrosshair().getValue(displayAnatomy.ZAXIS).getX());


        return ap3d;
    }


    public IImagePlot whichPlot(Point p) {
        if (axialPane.pointInPlot(this, p)) {
            return axialPane.getImagePlot();
        }
        if (coronalPane.pointInPlot(this, p)) {
            return coronalPane.getImagePlot();
        }
        if (sagittalPane.pointInPlot(this, p)) {
            return sagittalPane.getImagePlot();
        }

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




    public void intervalAdded(ListDataEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void intervalRemoved(ListDataEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void contentsChanged(ListDataEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initView() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);

        axialPane = new ImagePane(ImageViewFactory.createAxialPlot(getModel()));
        axialPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        coronalPane = new ImagePane(ImageViewFactory.createCoronalPlot(getModel()));
        coronalPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        sagittalPane = new ImagePane(ImageViewFactory.createSagittalPlot(getModel()));
        sagittalPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        plotList.add(axialPane.getImagePlot());
        plotList.add(coronalPane.getImagePlot());
        plotList.add(sagittalPane.getImagePlot());


        setLayout(layout);
        add(axialPane);
        add(coronalPane);
        add(sagittalPane);
    }


    public void clearAnnotations() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public IAnnotation getAnnotation(IImagePlot plot, String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeAnnotation(IImagePlot plot, String name) {
        
    }

    public void setAnnotation(IImagePlot plot, String name, IAnnotation annotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String toString() {
        return "SimpleOrthogonalImageView -- " + getModel().getName();
    }


}
