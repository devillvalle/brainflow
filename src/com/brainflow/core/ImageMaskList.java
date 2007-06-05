package com.brainflow.core;

import com.brainflow.image.data.IImageData;
import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 10, 2007
 * Time: 3:02:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageMaskList {

    private List<MaskItem<ImageLayer>> maskItems = new ArrayList<MaskItem<ImageLayer>>();

    private MaskItem<ImageLayer> root;

    private ExtendedPropertyChangeSupport support = new ExtendedPropertyChangeSupport(this);

    private PropertyChangeListener propertyListener = new MaskItemChangeListener();

    private List<ListDataListener> listDataListeners = new ArrayList<ListDataListener>();


    public ImageMaskList() {

    }

    public ImageMaskList(MaskItem<ImageLayer> root) {
        maskItems.add(root);
        this.root = root;


        root.addPropertyChangeListener(propertyListener);

    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
        
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }


    public void addListDataListener(ListDataListener listener) {
        listDataListeners.add(listener);
    }

    public void removeListDataListener(ListDataListener listener) {
        listDataListeners.remove(listener);
    }



    public List<AbstractLayer> getCongruentLayers(IImageDisplayModel model) {
        List<AbstractLayer> list = new ArrayList<AbstractLayer>();
        for (int i = 0; i < model.getNumLayers(); i++) {
            AbstractLayer layer = model.getLayer(i);
            if (layer instanceof ImageLayer) {
                ImageLayer il = (ImageLayer) layer;
                if (root.getSource().getData().getImageSpace().sameGeometry(il.getData().getImageSpace())) {
                    list.add(il);
                }
            }
        }

        return list;
    }

    public boolean isCongruent(AbstractLayer layer) {
        if (root == null) return false;
        if (layer instanceof ImageLayer) {
            ImageLayer ilayer = (ImageLayer)layer;
            if (root.getSource().getData().getImageSpace().sameGeometry(ilayer.getData().getImageSpace())) {
                return true;
            }
        }

        return false;
    }

    public MaskItem<ImageLayer> getLastItem() {
        if (maskItems.size() == 0) {
            throw new IllegalStateException("ImageMaskList is empty, cannot access last item");
        }
        return maskItems.get(maskItems.size() - 1);
    }

    public MaskItem<ImageLayer> getFirstItem() {
        if (maskItems.size() == 0) {
            throw new IllegalStateException("ImageMaskList is empty, cannot access first item");
        }

        return maskItems.get(0);
    }

    public MaskItem<ImageLayer> getMaskItem(int index) {
        if (maskItems.size() == 0) {
            throw new IllegalStateException("ImageMaskList is empty, cannot access " + index + "th item");
        }
        return maskItems.get(index);
    }

    public void removeMaskItem(int idx) {
        if (idx == 0) throw new IllegalArgumentException("Cannot remove root item form ImageMaskList");

        MaskItem item = maskItems.get(idx);
        item.removePropertyChangeListener(propertyListener);
        maskItems.remove(idx);
        fireItemRemoved(idx);
    }

    public int size() {
        return maskItems.size();
    }

    public void fireItemAdded(int index) {
        for (ListDataListener listener : listDataListeners) {
            listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index));
        }
    }

    public void fireItemRemoved(int index) {
        for (ListDataListener listener : listDataListeners) {
            listener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index));
        }
    }


    public void addMask(MaskItem<ImageLayer> item) {
        if (item.getGroup() > (getLastItem().getGroup() + 1)) {
            throw new IllegalArgumentException("Illegal Group number for MaskItem : " + item + " " + item.getGroup());
        }

        if (item.getGroup() < (getLastItem().getGroup())) {
            throw new IllegalArgumentException("Illegal Group number for MaskItem : " + item + " " + item.getGroup());
        }

        if (!isCongruent(item.getSource())) {
            throw new IllegalArgumentException("Argument has different geomtry than this mask group");
        }

        if (maskItems.size() == 0) {
            root = item;
        }

        maskItems.add(item);

        item.addPropertyChangeListener(propertyListener);
        fireItemAdded(maskItems.size() - 1);

    }


    class MaskItemChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            MaskItem item = (MaskItem) evt.getSource();
            int index = maskItems.indexOf(item);
            support.fireIndexedPropertyChange(evt.getPropertyName(), index, evt.getOldValue(), evt.getNewValue());
        }
    }


}
