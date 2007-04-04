package com.brainflow.core;

import com.brainflow.application.services.ImageViewCrosshairEvent;
import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.display.Crosshair;
import com.brainflow.display.ICrosshair;
import com.brainflow.display.Property;
import com.brainflow.display.Viewport3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.list.ArrayListModel;
import org.bushe.swing.event.EventBus;


import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 17, 2004
 * Time: 11:55:26 AM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageView extends JComponent implements ListDataListener, ImageDisplayModelListener {

    private static final Logger log = Logger.getLogger(ImageView.class.getName());

    private String id = "";

    private IImageDisplayModel displayModel;

    
    private Property<ICrosshair> crosshair;

    protected Property<Viewport3D> viewport;

   

    public ImageView(IImageDisplayModel imodel) {
        super();

        displayModel = imodel;
        init();
    }


    protected void init() {
        viewport = new Property<Viewport3D>(new Viewport3D(getModel()));

        crosshair = new Property<ICrosshair>(new Crosshair(viewport.getProperty()));

        displayModel.addImageDisplayModelListener(this);


        crosshair.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {

                EventBus.publish(new ImageViewCrosshairEvent(ImageView.this));

            }
        });


        displayModel.getSelection().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName() == SelectionInList.PROPERTYNAME_SELECTION_INDEX) {
                    int selectionIndex = (Integer) e.getNewValue();
                    if (selectionIndex >= 0) {
                        EventBus.publish(new ImageViewLayerSelectionEvent(ImageView.this, (Integer) e.getNewValue()));
                    }
                }

            }
        });


    }

    public abstract SelectionInList getPlotSelection();


    public IImagePlot getSelectedPlot() {
        return (IImagePlot) getPlotSelection().getSelection();
    }





    public int getSelectedIndex() {
        return displayModel.getSelection().getSelectionIndex();
    }

    public void setSelectedIndex(int selectedIndex) {
        assert selectedIndex < getModel().getNumLayers() & selectedIndex >= 0;
        displayModel.getSelection().setSelectionIndex(selectedIndex);
    }


    public IImageDisplayModel getModel() {
        return displayModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Property<ICrosshair> getCrosshair() {
        return crosshair;
    }

    public Property<Viewport3D> getViewport() {
        return viewport;
    }


    public AnatomicalPoint3D getCentroid() {
        IImageSpace compositeSpace = displayModel.getImageSpace();
        ImageAxis a1 = compositeSpace.getImageAxis(Axis.X_AXIS);
        ImageAxis a2 = compositeSpace.getImageAxis(Axis.Y_AXIS);
        ImageAxis a3 = compositeSpace.getImageAxis(Axis.Z_AXIS);

        AnatomicalPoint1D x = a1.getRange().getCenter();
        AnatomicalPoint1D y = a2.getRange().getCenter();
        AnatomicalPoint1D z = a3.getRange().getCenter();

        return new AnatomicalPoint3D((AnatomicalVolume) compositeSpace.getAnatomy(), x.getX(), y.getX(), z.getX());


    }

    public void clearAnnotations() {
        for (IImagePlot plot : getPlots()) {
            plot.clearAnnotations();
        }
    }

    public IAnnotation getAnnotation(IImagePlot plot, String name) {

        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        return plot.getAnnotation(name);
    }

    public void removeAnnotation(IImagePlot plot, String name) {
        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        plot.removeAnnotation(name);

    }

    public void setAnnotation(IImagePlot plot, String name, IAnnotation annotation) {
        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        plot.setAnnotation(name, annotation);


    }

    public Point getCrosshairLocation(IImagePlot plot) {
        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        ICrosshair cross = getCrosshair().getProperty();

        AnatomicalPoint1D xpt = cross.getValue(plot.getDisplayAnatomy().XAXIS);
        AnatomicalPoint1D ypt = cross.getValue(plot.getDisplayAnatomy().YAXIS);

        double percentX = (xpt.getX() - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt.getX() - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();

        Rectangle2D plotArea = plot.getPlotArea();

        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

        Point location = new Point((int) Math.round(screenX), (int) Math.round(screenY));
        return SwingUtilities.convertPoint(plot.getComponent(), location, this);


    }

    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point p) {

        Point viewPoint = SwingUtilities.convertPoint(source, p, this);
        IImagePlot plot = whichPlot(viewPoint);

        if (plot == null) {
            return null;
        }

        Point plotPoint = SwingUtilities.convertPoint(this, viewPoint, plot.getComponent());

        AnatomicalPoint2D apoint = plot.translateScreenToAnat(plotPoint);

        AnatomicalVolume displayAnatomy = plot.getDisplayAnatomy();
        AnatomicalPoint3D ap3d = new AnatomicalPoint3D(
                AnatomicalVolume.matchAnatomy(
                        plot.getXAxisRange().getAnatomicalAxis(),
                        plot.getYAxisRange().getAnatomicalAxis(),
                        plot.getDisplayAnatomy().ZAXIS),
                apoint.getX(), apoint.getY(),
                getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS).getX());


        return ap3d;
    }


    public abstract IImagePlot whichPlot(Point p);

    public abstract List<IImagePlot> getPlots();


    public abstract RenderedImage captureImage();


    public void intervalAdded(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }

    }

    public void intervalRemoved(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }
    }

    public void contentsChanged(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {

            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }
    }

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
        Viewport3D viewport = getViewport().getProperty();
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.setXAxisRange(viewport.getRange(plot.getDisplayAnatomy().XAXIS));
            plot.setYAxisRange(viewport.getRange(plot.getDisplayAnatomy().YAXIS));

        }

    }
}
