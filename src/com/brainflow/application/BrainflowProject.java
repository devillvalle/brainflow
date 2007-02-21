package com.brainflow.application;

import com.brainflow.core.IImageDisplayModel;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 20, 2007
 * Time: 1:18:57 PM
 */
public class BrainflowProject {


    private List<IImageDisplayModel> modelList = new ArrayList<IImageDisplayModel>();

    private ModelListDataListener listener = new ModelListDataListener();

    private List<ListDataListener> listenerList = new ArrayList<ListDataListener>();

    public void addModel(IImageDisplayModel model) {
        if (!modelList.contains(model)) {
            modelList.add(model);
            model.addListDataListener(listener);
        }
    }

    public void removeModel(IImageDisplayModel model) {
        if (modelList.contains(model)) {
            modelList.remove(model);
        }
    }


    public Iterator<IImageDisplayModel> iterator() {
        return modelList.iterator();
    }

    public int size() {
        return modelList.size();
    }

    public void addListDataListener(ListDataListener listener) {
        listenerList.add(listener);
    }

    public void removeDataListener(ListDataListener listener) {
        listenerList.remove(listener);
    }

    public List<ILoadableImage> getImageList() {

        List<ILoadableImage> list = new ArrayList<ILoadableImage>();
        for (IImageDisplayModel model : modelList) {
            int n = model.getNumLayers();
            for (int i = 0; i < n; i++) {
                ILoadableImage limg = model.getImageLayer(i).getLoadableImage();
                if (!list.contains(limg)) {
                    list.add(limg);
                }
            }

        }

        return list;

    }

    private void fireIntervalAdded(ListDataEvent e) {
        for (ListDataListener l : listenerList) {
            l.intervalAdded(e);

        }

    }

    private void fireContentsChanged(ListDataEvent e) {
        for (ListDataListener l : listenerList) {
            l.contentsChanged(e);

        }

    }

    private void fireIntervalRemoved(ListDataEvent e) {
        for (ListDataListener l : listenerList) {
            l.intervalRemoved(e);

        }

    }


    class ModelListDataListener implements ListDataListener {

        public void intervalAdded(ListDataEvent e) {
            fireIntervalAdded(e);
        }

        public void intervalRemoved(ListDataEvent e) {
            fireIntervalRemoved(e);

        }

        public void contentsChanged(ListDataEvent e) {
            fireContentsChanged(e);

        }
    }


}
