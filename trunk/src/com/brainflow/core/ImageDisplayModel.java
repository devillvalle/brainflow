package com.brainflow.core;

import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import javax.swing.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 2, 2004
 * Time: 10:02:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDisplayModel implements IImageDisplayModel {


    public static final String IMAGE_SPACE_PROPERTY = "imageSpace";

    private EventListenerList listeners = new EventListenerList();

    private ArrayListModel imageListModel = new ArrayListModel();

    private ChangeListener dirtyListener = new DirtyListener();

    private SelectionInList layerSelection = new SelectionInList((ListModel) imageListModel);

    private List<ListDataListener> listenerList = new ArrayList<ListDataListener>();

    private ForwardingListDataListener forwarder = new ForwardingListDataListener();

    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);


    private static IImageSpace EMPTY_SPACE = new ImageSpace3D(new ImageAxis(0, 100, AnatomicalVolume.getCanonicalAxial().XAXIS, 100),
            new ImageAxis(0, 100, AnatomicalVolume.getCanonicalAxial().YAXIS, 100),
            new ImageAxis(0, 100, AnatomicalVolume.getCanonicalAxial().ZAXIS, 100));

    private IImageSpace imageSpace = EMPTY_SPACE;

    private final static Logger log = Logger.getLogger(ImageDisplayModel.class.getName());

    private String name;

    public ImageDisplayModel(String _name) {
        name = _name;
        imageListModel.addListDataListener(forwarder);
    }


    public ImageLayerParameters getLayerParameters(int layer) {
        assert layer >= 0 && layer < imageListModel.size();
        ImageLayer ilayer = (ImageLayer) imageListModel.get(layer);
        return ilayer.getImageLayerParameters();
    }

    public ImageLayer getLayer(ImageLayerParameters params) {
        for (int i = 0; i < imageListModel.size(); i++) {
            ImageLayer layer = (ImageLayer) imageListModel.get(i);
            if (params == layer.getImageLayerParameters()) return layer;
        }

        return null;
    }

    public SelectionInList getSelection() {
        return layerSelection;
    }


    public int getSelectedIndex() {
        return layerSelection.getSelectionIndex();
    }

    public String getName() {
        return name;
    }

    public void addListDataListener(ListDataListener listener) {
        listenerList.add(listener);
    }

    public void removeListDataListener(ListDataListener listener) {
        listenerList.remove(listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }


    public void addChangeListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }

    public String getLayerName(int idx) {
        assert idx >= 0 && idx < imageListModel.size();
        ImageLayer layer = (ImageLayer) imageListModel.get(idx);
        return layer.getImageData().getImageLabel();
    }


    public void addLayer(ImageLayer layer) {
        listenToLayer(layer);
        imageListModel.add(layer);
        if (imageListModel.size() == 1) {
            layerSelection.setSelectionIndex(0);
        }
        computeImageSpace();

        //this.fireChangeEvent(new DisplayChangeEvent()
    }

    private void listenToLayer(ImageLayer layer) {

        layer.getImageLayerParameters().addChangeListener(dirtyListener);
    }


    private void computeImageSpace() {
        IImageSpace uspace = null;

        if (!imageListModel.isEmpty()) {
            for (int i = 0; i < imageListModel.size(); i++) {
                IImageData data = getImageData(i);
                IImageSpace space = data.getImageSpace();
                if (uspace == null) {
                    uspace = space;
                } else {
                    uspace = uspace.union(space);

                }
            }
        } else {
            uspace = EMPTY_SPACE;
        }

        if (!uspace.equals(imageSpace)) {

            IImageSpace old = imageSpace;
            imageSpace = uspace;
            changeSupport.firePropertyChange(ImageDisplayModel.IMAGE_SPACE_PROPERTY, old, imageSpace);
        }
        //fireChangeEvent(
        //getDisplayParameters().getViewport().getProperty().setBounds((ImageSpace3D) uspace);

    }


    public int indexOf(ImageLayer layer) {
        return imageListModel.indexOf(layer);


    }

    public List<Integer> indexOf(IImageData data) {
        List<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < imageListModel.size(); i++) {
            ImageLayer layer = (ImageLayer) imageListModel.get(i);
            if (layer.getImageData() == data) {
                ret.add(i);

            }
        }

        return ret;
    }

    public void removeLayer(int layer) {
        assert imageListModel.size() > layer && layer >= 0;
        ImageLayer rlayer = (ImageLayer) imageListModel.get(layer);
        imageListModel.remove(layer);
        computeImageSpace();
        //rlayer.getImageLayerParameters().removeChangeListener(dirtyListener);

    }

    public void removeLayer(ImageLayer layer) {
        assert imageListModel.contains(layer);
        int idx = imageListModel.indexOf(layer);
        removeLayer(idx);
    }

    public boolean containsLayer(ImageLayer layer) {
        return imageListModel.contains(layer);
    }

    public ImageLayer getImageLayer(int layer) {
        assert layer >= 0 && layer < imageListModel.size();
        return (ImageLayer) imageListModel.get(layer);
    }

    public IImageData getImageData(int layer) {
        assert layer >= 0 && layer < imageListModel.size();
        ImageLayer ilayer = (ImageLayer) imageListModel.get(layer);
        return ilayer.getImageData();
    }


    public int size() {
        return imageListModel.size();
    }

    public int getNumLayers() {
        return size();
    }

    public IImageSpace getImageSpace() {
        return imageSpace;

    }

    public double getSpacing(Axis axis) {
        return getImageSpace().getSpacing(axis);

    }

    public double getSpacing(AnatomicalAxis axis) {
        return getImageSpace().getSpacing(axis);
    }

    public ImageAxis getImageAxis(Axis axis) {
        return getImageSpace().getImageAxis(axis);
    }

    public ImageAxis getImageAxis(AnatomicalAxis axis) {
        ImageAxis iaxis = getImageSpace().getImageAxis(axis, true);
        ImageAxis retAxis = iaxis.matchAxis(axis);
        return retAxis;
    }

    /* (non-Javadoc)
    * @see com.brainflow.core.IImageDisplayModel#setLayer(int, com.brainflow.application.SoftLoadableImage, com.brainflow.display.props.DisplayProperties)
    */
    //public void setLayer(int index, ImageLayer layer) {
    //    assert index >= 0 && index < size();
    //    imageListModel.set(index, layer);
    //    computeImageSpace();
    // }


    protected void fireChangeEvent(DisplayChangeEvent e) {
        ChangeListener[] cl = listeners.getListeners(ChangeListener.class);
        for (ChangeListener c : cl) {
            c.stateChanged(e);
        }
    }

    public String toString() {
        return getName();
    }


    class DirtyListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            DisplayChangeEvent de = (DisplayChangeEvent) e;
            System.out.println("firing dirty event : " + de.getDisplayAction());
            fireChangeEvent(de);
        }
    }


    class ForwardingListDataListener implements ListDataListener {

        public void intervalAdded(ListDataEvent e) {
            fireIntervalAdded(e);
        }

        public void intervalRemoved(ListDataEvent e) {
            fireIntervalRemoved(e);
        }

        public void contentsChanged(ListDataEvent e) {
            fireContentsChanged(e);
        }

        private void fireIntervalAdded(ListDataEvent e) {
            ListDataEvent ne = new ListDataEvent(ImageDisplayModel.this, e.getType(), e.getIndex0(), e.getIndex1());
            for (ListDataListener l : listenerList) {
                l.intervalAdded(ne);

            }

        }

        private void fireContentsChanged(ListDataEvent e) {
            ListDataEvent ne = new ListDataEvent(ImageDisplayModel.this, e.getType(), e.getIndex0(), e.getIndex1());
            for (ListDataListener l : listenerList) {
                l.intervalAdded(ne);

            }

        }

        private void fireIntervalRemoved(ListDataEvent e) {
            ListDataEvent ne = new ListDataEvent(ImageDisplayModel.this, e.getType(), e.getIndex0(), e.getIndex1());
            for (ListDataListener l : listenerList) {
                l.intervalAdded(ne);

            }

        }
    }


}
