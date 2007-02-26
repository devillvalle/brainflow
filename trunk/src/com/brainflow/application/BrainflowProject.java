package com.brainflow.application;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.application.toplevel.BrainflowProjectListener;
import com.brainflow.application.toplevel.BrainflowProjectEvent;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 20, 2007
 * Time: 1:18:57 PM
 */
public class BrainflowProject {

    private static final Logger log = Logger.getLogger(BrainflowProject.class.getName());

    private List<IImageDisplayModel> modelList = new ArrayList<IImageDisplayModel>();

    private ModelListDataListener listener = new ModelListDataListener();

    private List<BrainflowProjectListener> listenerList = new ArrayList<BrainflowProjectListener>();

    public void addModel(IImageDisplayModel model) {
        if (!modelList.contains(model)) {
            modelList.add(model);
            model.addListDataListener(listener);
            fireModelAdded(new BrainflowProjectEvent(this, model, null));
        } else {
            log.warning("BrainflowProject already contains model supplied as argument, not adding.");
        }
    }

    public void removeModel(IImageDisplayModel model) {
        if (modelList.contains(model)) {
            modelList.remove(model);
            fireModelRemoved(new BrainflowProjectEvent(this, model, null));
        } else {
            log.warning("BrainflowProject does not contain model supplied as argument, cannot remove");
        }
    }


    public Iterator<IImageDisplayModel> iterator() {
        return modelList.iterator();
    }

    public int size() {
        return modelList.size();
    }

    public void addListDataListener(BrainflowProjectListener listener) {
        listenerList.add(listener);
    }

    public void removeListDataListener(BrainflowProjectListener listener) {
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

    private void fireModelAdded(BrainflowProjectEvent event) {
        for (BrainflowProjectListener l : listenerList) {
            l.modelAdded(event);

        }

    }

    private void fireModelRemoved(BrainflowProjectEvent event) {
         for (BrainflowProjectListener l : listenerList) {
            l.modelAdded(event);

        }


    }

    private void fireIntervalAdded(ListDataEvent e) {
        for (BrainflowProjectListener l : listenerList) {
           l.intervalAdded(new BrainflowProjectEvent(this, (IImageDisplayModel)e.getSource(), e));

        }

    }

    private void fireContentsChanged(ListDataEvent e) {
        for (BrainflowProjectListener l : listenerList) {
            l.contentsChanged(new BrainflowProjectEvent(this, (IImageDisplayModel)e.getSource(), e));


        }

    }

    private void fireIntervalRemoved(ListDataEvent e) {
        for (BrainflowProjectListener l : listenerList) {
            l.intervalRemoved(new BrainflowProjectEvent(this, (IImageDisplayModel)e.getSource(), e));

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
