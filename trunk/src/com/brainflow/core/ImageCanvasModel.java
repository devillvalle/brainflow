package com.brainflow.core;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 27, 2005
 * Time: 10:35:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageCanvasModel {

    private static Logger log = Logger.getLogger(ImageCanvasModel.class.getCanonicalName());

    public static String SELECTED_VIEW_PROPERTY = "selectedView";

    private ExtendedPropertyChangeSupport support = new ExtendedPropertyChangeSupport(this);

    //private List<ImageView> imageViews = new ArrayList<ImageView>();
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

    public ImageView[] getImageViews() {
        ImageView[] views = new ImageView[imageViewListModel.size()];
        imageViewListModel.toArray(views);
        return views;
    }

    public void addImageView(ImageView view) {

        log.info("Adding ImageView to imageViewListModel");
        imageViewListModel.add(view);

        //log.info("setting selected view");
        //setSelectedView(view);
        //log.info("finished setting selected view");
        //assert getSelectedView() != null;
    }

    public void removeImageView(ImageView view) {
        imageViewListModel.remove(view);
    }


}
