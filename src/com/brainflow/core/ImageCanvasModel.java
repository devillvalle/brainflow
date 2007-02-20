package com.brainflow.core;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 27, 2005
 * Time: 10:35:14 AM
 * To change this template use File | Settings | File Templates.
 */


public class ImageCanvasModel {

    private static final Logger log = Logger.getLogger(ImageCanvasModel.class.getCanonicalName());

    public static String SELECTED_VIEW_PROPERTY = "selectedView";

    private PropertyChangeSupport support = new ExtendedPropertyChangeSupport(this);

    private ArrayListModel imageViewListModel = new ArrayListModel();

    private SelectionInList imageViewSelection = new SelectionInList((ListModel) imageViewListModel);

    private ImageView selectedView = null;



    public ImageCanvasModel() {


    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }


    public SelectionInList getImageViewSelection() {
        return imageViewSelection;
    }

    public void setSelectedView(ImageView view) {
        ImageView oldView = this.selectedView;
        imageViewSelection.setSelection(view);
        this.selectedView = view;
        support.firePropertyChange(ImageCanvasModel.SELECTED_VIEW_PROPERTY, oldView, selectedView);
    }

    public ImageView getSelectedView() {
        return (ImageView) imageViewSelection.getSelection();
    }

    public List<ImageView> getImageViews() {
        ImageView[] views = new ImageView[imageViewListModel.size()];
        imageViewListModel.toArray(views);
        return Arrays.asList(views);

    }



    public void addImageView(ImageView view) {
        imageViewListModel.add(view);
        //linkedViews.put(view, new ArrayList<ImageView>());
    }

    public void removeImageView(ImageView view) {
        imageViewListModel.remove(view);
        //linkedViews.remove(view);
    }


}
