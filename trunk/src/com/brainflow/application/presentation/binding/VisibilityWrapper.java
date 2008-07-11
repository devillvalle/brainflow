package com.brainflow.application.presentation.binding;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.layer.ImageLayerListenerImpl;
import com.brainflow.core.layer.ImageLayerEvent;
import com.brainflow.core.layer.ImageLayer;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.events.IndexedPropertyListener;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.BeanContainer;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 8, 2008
 * Time: 9:55:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class VisibilityWrapper {

    private IImageDisplayModel model;


    public final IndexedProperty<Integer> visibleIndices = new ObservableIndexed<Integer>() {


        public void set(List<Integer> t) {
            if (t.equals(get())) {
                System.out.println("do nothing : old set equlas new set");
                return;
            }

            for (Integer i : t) {
                if (i >= model.getNumLayers()) {
                    System.out.println("WHOA!!!!");
                    throw new IllegalArgumentException(" index " + i + " invalid for list model with " + model.getNumLayers() + " layers");
                }
            }

            for (int i=0; i<model.getNumLayers(); i++) {
                if (t.contains(i)) {
                    System.out.println("setting layer " + i + " to visible");
                    model.getLayer(i).getImageLayerProperties().visible.set(true);
                } else {
                    System.out.println("setting layer " + i + " to not visible");
                    model.getLayer(i).getImageLayerProperties().visible.set(false);
                }
            }

            super.set(t);
        }

        public void set(int index, Integer integer) {
            super.set(index, integer);
            model.getLayer(integer).getImageLayerProperties().visible.set(true);
        }

        public void add(Integer integer) {
            super.add(integer);
            model.getLayer(integer).getImageLayerProperties().visible.set(true);

        }

        public void remove(int index) {
            int layerIndex = get(index);
            super.remove(index);
            model.getLayer(layerIndex).getImageLayerProperties().visible.set(false);
        }

        public void remove(Integer integer) {
            super.remove(integer);
            model.getLayer(integer).getImageLayerProperties().visible.set(false);
        }


    };

    public VisibilityWrapper(IImageDisplayModel model) {
        BeanContainer.bind(this);
        this.model = model;
        init();
    }

    private void init() {

        int i = 0;
        for (ImageLayer layer : model) {
            if (layer.isVisible()) {
                visibleIndices.add(i);
            }
            i++;
        }

        model.addImageLayerListener(new ImageLayerListenerImpl() {
            public void visibilityChanged(ImageLayerEvent event) {

                int layerIndex = event.getLayerIndex();

                ImageLayer layer = model.getLayer(layerIndex);
                System.out.println("visibility changed! layer " + layerIndex + " is now " + layer.isVisible());
                if (!layer.isVisible()) {
                    visibleIndices.remove(new Integer(layerIndex));
                }

            }
        });

        BeanContainer.get().addListener(model.getListModel(), new IndexedPropertyListener() {

            public void propertyInserted(IndexedProperty prop, Object value, int index) {
                System.out.println("layer inserted! " + index);
                updateModel();
            }

            public void propertyRemoved(IndexedProperty prop, Object value, int index) {
                updateModel();
            }

            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                System.out.println("layer changed! " + index);
                updateModel();
            }
        });


    }


    private void updateModel() {

        List<Integer> newSet = new ArrayList<Integer>();
        for (int i = 0; i < model.getNumLayers(); i++) {
            ImageLayer layer = model.getLayer(i);
            if (layer.isVisible()) {
                newSet.add(i);
            }

        }

       
        visibleIndices.set(newSet);

    }

    
}
