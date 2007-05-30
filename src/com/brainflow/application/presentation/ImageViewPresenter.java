package com.brainflow.application.presentation;

import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.core.*;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.image.space.IImageSpace;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.event.ListDataEvent;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 18, 2006
 * Time: 12:32:20 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageViewPresenter extends AbstractPresenter implements ImageDisplayModelListener {


    private ImageView selectedView;

    public ImageViewPresenter() {
        EventBus.subscribeStrongly(ImageViewSelectionEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                ImageViewSelectionEvent event = (ImageViewSelectionEvent) evt;

                //if (selectedView == event.getSelectedImageView()) return;

                if (selectedView != null) {
                    selectedView.getModel().removeImageDisplayModelListener(ImageViewPresenter.this);
                }

                selectedView = event.getSelectedImageView();
                selectedView.getModel().addImageDisplayModelListener(ImageViewPresenter.this);

                // in fact this should not happen.
                //if ( (selectedView != null) && (selectedView.getModel().getSelectedIndex() < 0) ) {
                //    selectedView.getModel().getSelection().setSelectionIndex(0);
                //
                // }

                if (selectedView != null) {

                    viewSelected(selectedView);

                } else {
                    allViewsDeselected();
                }
            }
        });

        EventBus.subscribeStrongly(ImageViewLayerSelectionEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                EventServiceEvent ese = (EventServiceEvent) evt;
                if (ese.getSource() == selectedView) {
                    int idx = selectedView.getModel().getSelectedIndex();

                    if (idx >= 0) {
                        AbstractLayer newLayer = selectedView.getModel().getLayer(idx);
                        assert newLayer != null;

                        layerSelected(newLayer);
                    } else {
                        Logger.getLogger(getClass().getCanonicalName()).warning("Selected layer index is negative");
                    }


                }
            }
        });

     

    }

    public abstract void viewSelected(ImageView view);

    public abstract void allViewsDeselected();

    protected void layerSelected(AbstractLayer layer) {
    }




    public ImageView getSelectedView() {
        return selectedView;
    }

    public AbstractLayer getSelectedLayer() {
        if (selectedView == null) return null;
        int idx = selectedView.getModel().getSelectedIndex();
        return selectedView.getModel().getLayer(idx);

    }

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {

    }

    public void intervalAdded(ListDataEvent e) {

    }

    public void intervalRemoved(ListDataEvent e) {

    }

    public void contentsChanged(ListDataEvent e) {
       
    }
}