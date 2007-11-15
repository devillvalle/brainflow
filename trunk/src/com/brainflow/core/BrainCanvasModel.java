package com.brainflow.core;


import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.ObservableProperty;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 27, 2005
 * Time: 10:35:14 AM
 * To change this template use File | Settings | File Templates.
 */


public class BrainCanvasModel {

    private static final Logger log = Logger.getLogger(BrainCanvasModel.class.getCanonicalName());


    public final Property<Integer> listSelection = ObservableProperty.create(-1);
    
    public final IndexedProperty<ImageView> imageViewList = ObservableIndexed.create();


    //private ArrayListModel imageViewListModel = new ArrayListModel();

    //private SelectionInList imageViewSelection = new SelectionInList((ListModel) imageViewListModel);


    public BrainCanvasModel() {
        BeanContainer.bind(this);

        


    }


    private List<ImageView> views() {
        return  (List<ImageView>)imageViewList.get();


    }

    public void setSelectedView(ImageView view) {
        if (views().contains(view)) {
            int i = views().indexOf(view);
            listSelection.set(i);
        } else {
            throw new IllegalArgumentException("Supplied argument " + view + " is not contained by canvas model ");
        }
    }

    public ImageView getSelectedView() {
        if (listSelection.get().intValue() < 0) {
            return null;
        }

        return imageViewList.get(listSelection.get());
    }

    public List<ImageView> getImageViews() {
        return views();
    }

    public int getNumViews() {
        return views().size();
    }

    public int indexOf(ImageView view) {
        return views().indexOf(view);
    }

    public void addImageView(ImageView view) {
        imageViewList.add(view);
        

    }

    public void removeImageView(ImageView view) {
        imageViewList.remove(view);

    }


}
