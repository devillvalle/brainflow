package com.brainflow.core;

import com.brainflow.colormap.IColorMap;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.WeakEventListenerList;
import com.brainflow.core.layer.*;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.events.IndexedPropertyListener;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 2, 2004
 * Time: 10:02:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDisplayModel implements IImageDisplayModel {

    private final static Logger log = Logger.getLogger(ImageDisplayModel.class.getName());

    private static IImageSpace EMPTY_SPACE = new ImageSpace3D(new ImageAxis(0, 100, Anatomy3D.getCanonicalAxial().XAXIS, 100),
            new ImageAxis(0, 100, Anatomy3D.getCanonicalAxial().YAXIS, 100),
            new ImageAxis(0, 100, Anatomy3D.getCanonicalAxial().ZAXIS, 100));

    private IImageSpace imageSpace = EMPTY_SPACE;


    public static final String IMAGE_SPACE_PROPERTY = "imageSpace";


    public final IndexedProperty<ImageLayer3D> listModel = ObservableIndexed.create();

    public final IndexedProperty<Integer> visibleSelection = new ObservableIndexed<Integer>() {



        private boolean isSynced() {
            for (int i=0; i<getNumLayers(); i++) {
                ImageLayer layer = getLayer(i);
                if (layer.isVisible() && !get().contains(i)) {
                    return false;
                }
                if (!layer.isVisible() && get().contains(i)) {
                    return false;
                }
            }

            return true;

        }
        public void remove(Integer i) {
            ImageLayer layer = getLayer(i);
            boolean vis = layer.getImageLayerProperties().visible.get();
            if (vis) {
                layer.getImageLayerProperties().visible.set(false);
            }

            if (get().contains(i)) {
                List<Integer> oldlist = get();
                List<Integer> newlist = new ArrayList<Integer>();
                newlist.addAll(oldlist);

                newlist.remove(i);
                super.set(newlist);
            }

          
            assert isSynced();


        }

       

        public void add(Integer i) {
            //todo this breaks down if same layer is addewd twice to model
            ImageLayer layer = getLayer(i);
            boolean vis = layer.getImageLayerProperties().visible.get();

            if (!vis) {
                layer.getImageLayerProperties().visible.set(true);
            }

            if (!get().contains(i)) {
                List<Integer> oldlist = get();
                List<Integer> newlist = new ArrayList<Integer>();
                newlist.addAll(oldlist);

                newlist.add(i);
                super.set(newlist);
            }


            assert isSynced();

        }

        public void set(List<Integer> t) {
            for (int i = 0; i < getNumLayers(); i++) {
                ImageLayer layer = getLayer(i);
                  if (t.contains(i)) {
                    layer.getImageLayerProperties().visible.set(true);
                 } else {
                     layer.getImageLayerProperties().visible.set(false);
                }
            }

            super.set(t);


            //To change body of overridden methods use File | Settings | File Templates.
            assert isSynced();
        }

        public void set(int index, Integer integer) {
            super.set(index, integer);


            assert isSynced();
        }
    };


    public final Property<Integer> listSelection = new ObservableProperty<Integer>(-1) {
        public void set(Integer integer) {
            if (integer >= listModel.size() || integer < -1) {
                throw new IllegalArgumentException("selection index exceeds size of list");
            }
            super.set(integer);
        }


    };


    private WeakEventListenerList eventListeners = new WeakEventListenerList();

    private ForwardingListDataListener forwarder = new ForwardingListDataListener();


    private String name;

    private ImageLayerListener visListener;

    public ImageDisplayModel(String _name) {
        name = _name;
        BeanContainer.bind(this);

        BeanContainer.get().addListener(listModel, new IndexedPropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                forwarder.fireContentsChanged(new ListDataEvent(ImageDisplayModel.this, ListDataEvent.CONTENTS_CHANGED, index, index));
            }

            public void propertyInserted(IndexedProperty prop, Object value, int index) {
                forwarder.fireIntervalAdded(new ListDataEvent(ImageDisplayModel.this, ListDataEvent.INTERVAL_ADDED, index, index));
            }

            public void propertyRemoved(IndexedProperty prop, Object value, int index) {
                forwarder.fireIntervalRemoved(new ListDataEvent(ImageDisplayModel.this, ListDataEvent.INTERVAL_REMOVED, index, index));
            }
        });


        visListener = new ImageLayerListenerImpl() {
            public void visibilityChanged(ImageLayerEvent event) {
                //todo hack cast
                ImageLayer3D layer = (ImageLayer3D)event.getAffectedLayer();
                int index = ImageDisplayModel.this.indexOf(layer);
                if (layer.isVisible()) {
                    visibleSelection.add(index);
                } else {
                    if (visibleSelection.get().contains(index)) {
                        visibleSelection.remove(visibleSelection.get().indexOf(index));
                    }
                }

            }
        };

        addImageLayerListener(visListener);


    }

    public IndexedProperty<Integer> getVisibleSelection() {
        return visibleSelection;
    }

    public IndexedProperty<ImageLayer3D> getListModel() {
        return listModel;
    }

    public Property<Integer> getListSelection() {
        return listSelection;
    }

    public Iterator<ImageLayer3D> iterator() {
        return new Iterator<ImageLayer3D>() {

            int i = 0;

            public boolean hasNext() {
                if (i < listModel.size()) return true;

                return false;
            }

            public ImageLayer3D next() {
                return listModel.get(i++);
            }

            public void remove() {
                listModel.remove(i);
            }
        };


    }

    public ImageLayerProperties getLayerParameters(int layer) {
        if (layer < 0 || layer > listModel.size()) {
            throw new IllegalArgumentException("illegal layer index : " + layer);
        }

        ImageLayer ilayer = listModel.get(layer);
        return ilayer.getImageLayerProperties();
    }


    public void setSelectedIndex(int index) {
        if (index < 0 || index >= getNumLayers()) {
            throw new IllegalArgumentException("index out of bounds : " + index);
        }

        if (getSelectedIndex() != index) {
            listSelection.set(index);
        }
    }

    public int getSelectedIndex() {
        return listSelection.get();
    }

    public ImageLayer3D getSelectedLayer() {
        return listModel.get(listSelection.get());
    }


    public String getName() {
        return name;
    }


    public void addImageDisplayModelListener(ImageDisplayModelListener listener) {
        if (eventListeners.contains(ImageDisplayModelListener.class, listener)) {
            return;
        }
        eventListeners.add(ImageDisplayModelListener.class, listener);
    }

    public void removeImageDisplayModelListener(ImageDisplayModelListener listener) {
        eventListeners.remove(ImageDisplayModelListener.class, listener);
    }

    public void addImageLayerListener(ImageLayerListener listener) {
        if (eventListeners.contains(ImageLayerListener.class, listener)) {
            throw new IllegalArgumentException("listener list already contains listener : " + listener);
        }
        eventListeners.add(ImageLayerListener.class, listener);


    }

    public void removeImageLayerListener(ImageLayerListener listener) {
        eventListeners.remove(ImageLayerListener.class, listener);
    }


    public String getLayerName(int idx) {
        assert idx >= 0 && idx < listModel.size();
        ImageLayer layer = listModel.get(idx);
        return layer.getLabel();
    }


    public void addLayer(ImageLayer3D layer) {
        listenToLayer(layer);

        boolean vis = layer.isVisible();

        listModel.add(layer);

        if (listModel.size() == 1) {
            imageSpace = layer.getCoordinateSpace();
            listSelection.set(0);
        }

        if (vis) {
            visibleSelection.add(indexOf(layer));
        }

        //computeImageSpace();

    }

    private void updateColorMapClips() {

    }

    private void listenToLayer(final ImageLayer3D layer) {


        /*layer.addPropertyChangeListener(ImageLayerProperties.RESAMPLE_PROPERTY, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.interpolationMethodChanged(new ImageLayerEvent(ImageDisplayModel.this, (ImageLayer) evt.getSource()));
                }
            }
        }); */


        BeanContainer.get().addListener(layer.getImageLayerProperties().colorMap, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.colorMapChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }
            }
        });


        BeanContainer.get().addListener(layer.getImageLayerProperties().interpolationType, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.interpolationMethodChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }
            }
        });

        BeanContainer.get().addListener(layer.getImageLayerProperties().visible, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                //ImageLayer layer = (ImageLayer)prop.getParent();
                //int i = ImageDisplayModel.this.indexOf(layer);
                //visibilityList.set(i, true);

                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.visibilityChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }
            }
        });

        BeanContainer.get().addListener(layer.getImageLayerProperties().opacity, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.opacityChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }
            }
        });

        BeanContainer.get().addListener(layer.getImageLayerProperties().smoothingRadius, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.smoothingChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }
            }
        });


        BeanContainer.get().addListener(layer.getImageLayerProperties().thresholdRange.get().lowClip, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.thresholdChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }
            }
        });

        BeanContainer.get().addListener(layer.getImageLayerProperties().thresholdRange.get().highClip, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.thresholdChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }
            }
        });

        BeanContainer.get().addListener(layer.getImageLayerProperties().thresholdRange, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {

                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.thresholdChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }

            }
        });

        BeanContainer.get().addListener(layer.getImageLayerProperties().clipRange, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                ClipRange clip = (ClipRange)newValue;
                Number lowClip = clip.getLowClip();
                Number highClip = clip.getHighClip();

                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);

                IColorMap oldMap = layer.getImageLayerProperties().getColorMap();

                if (oldMap.getLowClip() == lowClip.doubleValue() && oldMap.getHighClip() == highClip.doubleValue()) {
                    return;
                }

                IColorMap newMap = oldMap.newClipRange(lowClip.doubleValue(), highClip.doubleValue());
                layer.getImageLayerProperties().colorMap.set(newMap);

                for (ImageLayerListener listener : listeners) {
                    listener.clipRangeChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }

            }
        });


        BeanContainer.get().addListener(layer.getImageLayerProperties().clipRange.get().lowClip, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                Number lowClip = (Number) newValue;
                Number highClip = layer.getImageLayerProperties().clipRange.get().getHighClip();
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);

                IColorMap oldMap = layer.getImageLayerProperties().getColorMap();

                if (oldMap.getLowClip() == lowClip.doubleValue()) {
                    return;
                }

                IColorMap newMap = oldMap.newClipRange(lowClip.doubleValue(), highClip.doubleValue());


                layer.getImageLayerProperties().colorMap.set(newMap);

                for (ImageLayerListener listener : listeners) {
                    listener.clipRangeChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));
                }

            }
        });


        BeanContainer.get().addListener(layer.maskProperty, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                MaskProperty3D mp = (MaskProperty3D)newValue;

                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);
                for (ImageLayerListener listener : listeners) {
                    listener.maskChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));

                }
                //System.out.println("mask changed!");
            }
        });




        BeanContainer.get().addListener(layer.getImageLayerProperties().clipRange.get().highClip, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                Number highClip = (Number) newValue;
                Number lowClip = layer.getImageLayerProperties().clipRange.get().getLowClip();
                ImageLayerListener[] listeners = eventListeners.getListeners(ImageLayerListener.class);

                IColorMap oldMap = layer.getImageLayerProperties().getColorMap();

                if (oldMap.getHighClip() == highClip.doubleValue()) {
                    return;
                }


                IColorMap newMap = oldMap.newClipRange(lowClip.doubleValue(), highClip.doubleValue());
                layer.getImageLayerProperties().colorMap.set(newMap);
                for (ImageLayerListener listener : listeners) {
                    listener.clipRangeChanged(new ImageLayerEvent(ImageDisplayModel.this, layer));

                }


            }

            // may not be necessary if because of  call above ...


        });


    }



    public boolean containsLayer(ImageLayer3D layer) {
        return listModel.get().contains(layer);
    }

    public int indexOf(ImageLayer3D layer) {
        return listModel.get().indexOf(layer);


    }

    public List<Integer> indexOf(IImageData data) {
        List<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < listModel.size(); i++) {
            ImageLayer layer = listModel.get(i);

            if (layer.getDataSource() == data) {
                ret.add(i);

            }
        }

        return ret;
    }

    public void rotateLayers() {
        if (getNumLayers() < 2) return;

        List<ImageLayer3D> newModel = new ArrayList<ImageLayer3D>();
        ImageLayer3D firstLayer = getLayer(getNumLayers() - 1);

        newModel.add(firstLayer);
        for (int i = 0; i < listModel.size() - 1; i++) {
            newModel.add(listModel.get(i));
        }

        listModel.set(newModel);


        forwarder.fireContentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getNumLayers() - 1));


    }

    public void swapLayers(int index0, int index1) {
        //todo check for valid indices

        if (index0 == index1) return;
        if (index0 > (getNumLayers() - 1)) {
            throw new IllegalArgumentException("Invalid layer index : " + index0);
        }

        if (index1 > (getNumLayers() - 1)) {
            throw new IllegalArgumentException("Invalid layer index : " + index1);
        }

        List<ImageLayer3D> newModel = new ArrayList<ImageLayer3D>();
        for (int i = 0; i < listModel.size(); i++) {
            if (i == index0) {
                newModel.add(i, listModel.get(index1));
            } else if (i == index1) {
                newModel.add(i, listModel.get(index0));
            } else {
                newModel.add(i, listModel.get(i));
            }

        }

        listModel.set(newModel);
//imageListModel.addListDataListener(forwarder);
//layerSelection.setListModel(imageListModel);

        forwarder.fireContentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1));
    }

    public void removeLayer(int layer) {
        assert listModel.size() > layer && layer >= 0;
        if (listSelection.get() == layer) {
            int selidx = -1;
            if (layer > 1) {
                selidx = layer - 1;
            } else if (layer == 0 && getNumLayers() > 1) {
                selidx = 1;
            }

            listSelection.set(selidx);

        }

        listModel.remove(layer);
        //computeImageSpace();

    }

    public void removeLayer(ImageLayer3D layer) {
        assert listModel.get().contains(layer);
        int idx = listModel.get().indexOf(layer);
        removeLayer(idx);
    }

    public boolean containsLayer(ImageLayer layer) {
        return listModel.get().contains(layer);
    }

    public ImageLayer3D getLayer(int layer) {
        if (layer < 0 || layer >= listModel.size()) {
            throw new IllegalArgumentException("illegal layer index : " + layer);
        }
        return listModel.get(layer);
    }


    public int size() {
        return listModel.size();
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
* @see com.brainflow.core.IImageDisplayModel#setLayer(int, com.brainflow.image.io.SoftImageDataSource, com.brainflow.display.props.DisplayProperties)
*/
//public void setLayer(int index, ImageLayer layer) {
//    assert index >= 0 && index < size();
//    imageListModel.set(index, layer);
//    computeImageSpace();
// }


    public String toString() {
        return getName();
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
            ImageDisplayModelListener[] listeners = eventListeners.getListeners(ImageDisplayModelListener.class);
            ListDataEvent ne = new ListDataEvent(ImageDisplayModel.this, e.getType(), e.getIndex0(), e.getIndex1());
            for (ListDataListener l : listeners) {
                l.intervalAdded(ne);

            }

        }

        private void fireContentsChanged(ListDataEvent e) {
            ImageDisplayModelListener[] listeners = eventListeners.getListeners(ImageDisplayModelListener.class);
            ListDataEvent ne = new ListDataEvent(ImageDisplayModel.this, e.getType(), e.getIndex0(), e.getIndex1());
            for (ListDataListener l : listeners) {
                l.contentsChanged(ne);

            }

        }

        private void fireIntervalRemoved(ListDataEvent e) {
            ListDataEvent ne = new ListDataEvent(ImageDisplayModel.this, e.getType(), e.getIndex0(), e.getIndex1());
            ImageDisplayModelListener[] listeners = eventListeners.getListeners(ImageDisplayModelListener.class);
            for (ListDataListener l : listeners) {
                l.intervalRemoved(ne);

            }

        }
    }


}
