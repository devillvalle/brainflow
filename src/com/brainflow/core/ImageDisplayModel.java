package com.brainflow.core;

import com.brainflow.display.ImageDisplayParameters;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 2, 2004
 * Time: 10:02:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDisplayModel implements IImageDisplayModel {

    private EventListenerList listeners = new EventListenerList();

    private ArrayListModel imageListModel = new ArrayListModel();

    private ChangeListener dirtyListener = new DirtyListener();

    private SelectionInList layerSelection = new SelectionInList((ListModel) imageListModel);

    private ImageDisplayParameters displayParameters;

    private Map<ImageLayer, List<ImageLayer>> alphaMaskMap = new HashMap<ImageLayer, List<ImageLayer>>();

    private static IImageSpace EMPTY_SPACE = new ImageSpace3D(new ImageAxis(0, 100, AnatomicalVolume.getCanonicalAxial().XAXIS, 100),
            new ImageAxis(0, 100, AnatomicalVolume.getCanonicalAxial().YAXIS, 100),
            new ImageAxis(0, 100, AnatomicalVolume.getCanonicalAxial().ZAXIS, 100));

    private IImageSpace compositeSpace = EMPTY_SPACE;

    private final static Logger log = Logger.getLogger(ImageDisplayModel.class.getName());

    private String name;

    public ImageDisplayModel(String _name) {
        name = _name;
        displayParameters = new ImageDisplayParameters(this);
        displayParameters.addChangeListener(dirtyListener);
        getDisplayParameters().getViewport().getParameter().setBounds((ImageSpace3D) EMPTY_SPACE);


    }

    public ListModel getListModel() {
        return imageListModel;
    }

    public ImageLayerParameters getLayerParameters(int layer) {
        assert layer >= 0 && layer < imageListModel.size();
        ImageLayer ilayer = (ImageLayer) imageListModel.get(layer);
        return ilayer.getImageLayerParameters();
    }

    public ImageDisplayParameters getDisplayParameters() {
        return displayParameters;
    }

    public SelectionInList getSelection() {
        return layerSelection;
    }

    public void removeMaskForLayer(ImageLayer iLayer, ImageLayer imaskLayer) {
        if (!containsLayer(iLayer)) {
            throw new IllegalArgumentException("ImageLayer not contained in model");
        }

        List<ImageLayer> mlist = alphaMaskMap.get(iLayer);
        mlist.remove(imaskLayer);


    }

    public void addMaskForLayer(ImageLayer iLayer, ImageLayer imaskLayer) {
        if (!containsLayer(iLayer) || !containsLayer(imaskLayer)) {
            throw new IllegalArgumentException("ImageLayer not contained in model");
        }

        List<ImageLayer> mlist = alphaMaskMap.get(iLayer);
        if (mlist == null) {
            mlist = new ArrayList<ImageLayer>();
            mlist.add(imaskLayer);
            alphaMaskMap.put(iLayer, mlist);

        } else if (!mlist.contains(imaskLayer)) {
            mlist.add(imaskLayer);
        }

        //fireChangeEvent(new DisplayChangeEvent())

        return;

    }


    public int getSelectedIndex() {
        return layerSelection.getSelectionIndex();
    }

    public String getName() {
        return name;
    }

    public void addListDataListener(ListDataListener listener) {
        imageListModel.addListDataListener(listener);
    }

    public void removeListDataListener(ListDataListener listener) {
        imageListModel.removeListDataListener(listener);
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
        imageListModel.add(layer);
        if (imageListModel.size() == 1) {
            layerSelection.setSelectionIndex(0);
        }
        computeCompositeSpace();
        listenToLayer(layer);

        //this.fireChangeEvent(new DisplayChangeEvent()
    }

    private void listenToLayer(ImageLayer layer) {
        layer.getImageLayerParameters().addChangeListener(dirtyListener);
    }


    private void computeCompositeSpace() {
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

        compositeSpace = (ImageSpace3D) uspace;
        getDisplayParameters().getViewport().getParameter().setBounds((ImageSpace3D) uspace);

    }


    public int indexOf(ImageLayer layer) {
        for (int i = 0; i < imageListModel.size(); i++) {
            ImageLayer l = (ImageLayer) imageListModel.get(i);
            if (l == layer) return i;
        }

        return -1;


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
        computeCompositeSpace();
        rlayer.getImageLayerParameters().removeChangeListener(dirtyListener);

    }

    public void removeLayer(ImageLayer layer) {
        assert imageListModel.contains(layer);
        imageListModel.remove(layer);
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

    public ImageSpace3D getCompositeImageSpace() {
        return (ImageSpace3D) compositeSpace;

    }

    public double getSpacing(Axis axis) {
        return getCompositeImageSpace().getSpacing(axis);

    }

    public double getSpacing(AnatomicalAxis axis) {
        return getCompositeImageSpace().getSpacing(axis);
    }

    public ImageAxis getImageAxis(Axis axis) {
        return getCompositeImageSpace().getImageAxis(axis);
    }

    public ImageAxis getImageAxis(AnatomicalAxis axis) {
        ImageAxis iaxis = getCompositeImageSpace().getImageAxis(axis, true);
        ImageAxis retAxis = iaxis.matchAxis(axis);
        return retAxis;
    }


    /* (non-Javadoc)
     * @see com.brainflow.core.IImageDisplayModel#setLayer(int, com.brainflow.application.SoftLoadableImage, com.brainflow.display.props.DisplayProperties)
     */
    public void setLayer(int index, ImageLayer layer) {
        assert index >= 0 && index < size();
        imageListModel.set(index, layer);
        computeCompositeSpace();
    }


    protected void fireChangeEvent(DisplayChangeEvent e) {
        ChangeListener[] cl = listeners.getListeners(ChangeListener.class);
        for (ChangeListener c : cl) {
            c.stateChanged(e);
        }
    }


    class DirtyListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            DisplayChangeEvent de = (DisplayChangeEvent) e;
            fireChangeEvent(de);
        }
    }


}
