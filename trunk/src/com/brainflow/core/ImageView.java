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

    private final List<IAnnotation> annotationList = new ArrayList<IAnnotation>();

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

    public void clearAnnotations() {
        annotationList.clear();
    }

    protected List<IAnnotation> getAnnotations() {
        return annotationList;
    }

    public Iterator<IAnnotation> annotationIterator() {
        return annotationList.iterator();
    }

    public void addAnnotation(IAnnotation annotation) {
        annotationList.add(annotation);
        repaint();
    }

    public void setAnnotation(IAnnotation annotation) {
        ListIterator<IAnnotation> iter = annotationList.listIterator();
        int index = -1;
        while (iter.hasNext()) {
            IAnnotation a = iter.next();
            if (a.getClass().equals(annotation.getClass())) {
                index = iter.previousIndex();
            }
        }

        if (index == -1) {
            addAnnotation(annotation);
        } else {

            annotationList.set(index, annotation);
            repaint();
        }


    }

    public void removeAnnotation(IAnnotation annotation) {
        annotationList.remove(annotation);
        repaint();
    }

    // temporary solution to get a particular annotation from an image view.
    // alternative is to use HashMap of Annotations and for each annotation
    // String identifier
    public IAnnotation getAnnotation(Class clazz) {
        for (IAnnotation i : annotationList) {
            if (i.getClass().getName().equals(clazz.getName())) {
                return i;
            }

        }

        return null;
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

    public Viewport3D getViewport() {
        return viewport.getProperty();
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
