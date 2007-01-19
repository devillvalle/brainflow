package com.brainflow.application.presentation;

import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.gui.AbstractPresenter;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 18, 2006
 * Time: 12:32:20 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageViewPresenter extends AbstractPresenter {


    private ImageView selectedView;

    public ImageViewPresenter() {
        EventBus.subscribeStrongly(ImageViewSelectionEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                ImageViewSelectionEvent event = (ImageViewSelectionEvent) evt;

                //if (selectedView == event.getSelectedImageView()) return;

                selectedView = event.getSelectedImageView();
                // in fact this should not happen.
                //if ( (selectedView != null) && (selectedView.getImageDisplayModel().getSelectedIndex() < 0) ) {
                //    selectedView.getImageDisplayModel().getSelection().setSelectionIndex(0);
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
                    int idx = selectedView.getImageDisplayModel().getSelectedIndex();

                    if (idx >= 0) {
                        ImageLayer newLayer = selectedView.getImageDisplayModel().getImageLayer(idx);
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

    protected void layerSelected(ImageLayer layer) {
    }


    public ImageView getSelectedView() {
        return selectedView;
    }

    public ImageLayer getSelectedLayer() {
        if (selectedView == null) return null;
        int idx = selectedView.getImageDisplayModel().getSelectedIndex();
        return selectedView.getImageDisplayModel().getImageLayer(idx);

    }
}
