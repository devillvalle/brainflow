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
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import org.bushe.swing.event.EventBus;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 17, 2004
 * Time: 11:55:26 AM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageView extends JComponent implements ListDataListener {

    private static final Logger log = Logger.getLogger(ImageView.class.getName());

    private String id = "";

    private IImageDisplayModel displayModel;

    private AnatomicalVolume displayAnatomy = AnatomicalVolume.getCanonicalAxial();

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

        displayModel.addListDataListener(this);

        displayModel.addLayerChangeListener(new LayerChangeListener() {

            public void layerChanged(LayerChangeEvent event) {
                System.out.println("catching layer changed event " + event.getChangeType());
                List<IImagePlot> plots = ImageView.this.getPlots();
                for (IImagePlot iplot : plots) {
                    iplot.getImageProducer().updateImage(event);
                    iplot.getComponent().repaint();
                }
            }
        });


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


    protected void setDisplayAnatomy(AnatomicalVolume _displayAnatomy) {
        displayAnatomy = _displayAnatomy;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
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

    public abstract IAnnotation getAnnotation(IImagePlot plot, String name);

    public abstract void removeAnnotation(IImagePlot plot, String name);

    public abstract void clearAnnotations();

    public abstract void setAnnotation(IImagePlot plot, String name, IAnnotation annotation);
       
    public abstract void intervalAdded(ListDataEvent e);

    public abstract void intervalRemoved(ListDataEvent e);

    public abstract void contentsChanged(ListDataEvent e);

    public abstract Point getCrosshairLocation(IImagePlot plot);

    public abstract AnatomicalPoint3D getAnatomicalLocation(Component source, Point screenPoint);

    public abstract IImagePlot whichPlot(Point p);

    public abstract List<IImagePlot> getPlots();

    public abstract SelectionInList getPlotSelection();

    public abstract IImagePlot getSelectedPlot();

    public abstract RenderedImage captureImage();


}
