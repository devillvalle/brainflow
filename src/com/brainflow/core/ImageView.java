package com.brainflow.core;

import com.brainflow.application.services.ImageViewCrosshairEvent;
import com.brainflow.application.services.ImageViewDataEvent;
import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.display.Crosshair;
import com.brainflow.display.DisplayParameter;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace3D;
import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    protected boolean selected = false;

    protected ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    private String id = "";

    public static final String CLOSED_PROPERTY = "closed";

    private boolean closed = false;

    private IImageDisplayModel displayModel;

    protected AnatomicalVolume displayAnatomy = AnatomicalVolume.getCanonicalAxial();

    private final List<IAnnotation> annotationList = new ArrayList<IAnnotation>();


    public ImageView() {

    }


    public ImageView(IImageDisplayModel imodel) {
        super();
        displayModel = imodel;
        init();
    }

    protected void setDisplayAnatomy(AnatomicalVolume _displayAnatomy) {
        displayAnatomy = _displayAnatomy;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }

    protected List<IAnnotation> getAnnotations() {
        return annotationList;
    }

    public Iterator<IAnnotation> annotationIterator() {
        return annotationList.iterator();
    }

    public void addAnnotation(IAnnotation annotation) {
        annotationList.add(annotation);
        annotation.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                log.info("Annotation changed event, repainting");
                log.info("Property is " + evt.getPropertyName());
                log.info("New Value is " + evt.getNewValue());
                IAnnotation annotation = (IAnnotation) evt.getSource();
                scheduleRepaint(new DisplayChangeEvent(
                        new DisplayParameter<IAnnotation>(annotation),
                        DisplayAction.ANNOTATION_CHANGED));

            }
        });
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


    protected void init() {

        displayModel.addListDataListener(this);

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

        displayModel.getDisplayParameters().getCrosshair().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                EventBus.publish(new ImageViewCrosshairEvent(ImageView.this));
            }
        });

    }


    public boolean isClosed() {
        return closed;
    }

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }

    public int getSelectedIndex() {
        return displayModel.getSelection().getSelectionIndex();
    }

    public void setSelectedIndex(int selectedIndex) {
        assert selectedIndex < getImageDisplayModel().getNumLayers() & selectedIndex >= 0;
        displayModel.getSelection().setSelectionIndex(selectedIndex);
    }


    public IImageDisplayModel getImageDisplayModel() {
        return displayModel;
    }

    public abstract void scheduleRepaint(DisplayChangeEvent e);


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Crosshair getCrosshair() {
        return displayModel.
                getDisplayParameters().getCrosshair().
                getParameter();
    }


    public abstract Point getCrosshairLocation(IImagePlot plot);

    public abstract AnatomicalPoint3D getAnatomicalLocation(Component source, Point screenPoint);

    public abstract IImagePlot whichPlot(Point p);

    public abstract List<IImagePlot> getPlots();


    public RenderedImage captureImage() {
        return null;
    }


    public AnatomicalPoint3D getCentroid() {
        ImageSpace3D compositeSpace = displayModel.getCompositeImageSpace();
        ImageAxis a1 = compositeSpace.getImageAxis(Axis.X_AXIS);
        ImageAxis a2 = compositeSpace.getImageAxis(Axis.Y_AXIS);
        ImageAxis a3 = compositeSpace.getImageAxis(Axis.Z_AXIS);

        AnatomicalPoint1D x = a1.getRange().getCenter();
        AnatomicalPoint1D y = a2.getRange().getCenter();
        AnatomicalPoint1D z = a3.getRange().getCenter();

        return new AnatomicalPoint3D((AnatomicalVolume) compositeSpace.getAnatomy(), x.getX(), y.getX(), z.getX());


    }

    protected void listChanged(ListDataEvent e) {
        EventBus.publish(new ImageViewDataEvent(this, e));
        scheduleRepaint(new DisplayChangeEvent(new DisplayParameter(new ValueHolder(getImageDisplayModel())), DisplayAction.DATA_CHANGED));


    }

    public void intervalAdded(ListDataEvent e) {
        EventBus.publish(new ImageViewDataEvent(this, e));
        scheduleRepaint(new DisplayChangeEvent(new DisplayParameter(new ValueHolder(getImageDisplayModel())), DisplayAction.DATA_CHANGED));

    }

    public void intervalRemoved(ListDataEvent e) {
        if (getImageDisplayModel().getNumLayers() == 0) {
            changeSupport.firePropertyChange(ImageView.CLOSED_PROPERTY, false, true);
        } else {
            scheduleRepaint(new DisplayChangeEvent(new DisplayParameter(new ValueHolder(getImageDisplayModel())), DisplayAction.DATA_CHANGED));
        }


    }

    public void contentsChanged(ListDataEvent e) {
        scheduleRepaint(new DisplayChangeEvent(new DisplayParameter(new ValueHolder(getImageDisplayModel())), DisplayAction.DATA_CHANGED));
        EventBus.publish(new ImageViewDataEvent(this, e));
    }

    protected class DirtListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            DisplayChangeEvent de = (DisplayChangeEvent) e;
            scheduleRepaint(de);

        }
    }


}
