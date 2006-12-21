package com.brainflow.application.presentation;

import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.gui.AbstractPresenter;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 18, 2006
 * Time: 12:32:20 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageViewPresenter extends AbstractPresenter {


    private ImageView selectedView;
    private ImageLayer selectedLayer;

    public ImageViewPresenter() {
        EventBus.subscribeStrongly(ImageViewSelectionEvent.class, new EventSubscriber() {

            public void onEvent(EventServiceEvent evt) {
                ImageViewSelectionEvent event = (ImageViewSelectionEvent) evt;
                selectedView = event.getSelectedImageView();
                viewSelected(selectedView);

            }
        });

        EventBus.subscribeStrongly(ImageViewLayerSelectionEvent.class, new EventSubscriber() {

            public void onEvent(EventServiceEvent evt) {
                if (evt.getSource() == selectedView) {
                    int idx = selectedView.getSelectedIndex();
                    selectedLayer = selectedView.getImageDisplayModel().getImageLayer(idx);
                    selectedLayerChanged(selectedView.getImageDisplayModel().getImageLayer(idx));

                }
            }
        });


    }

    public abstract void viewSelected(ImageView view);

    protected void selectedLayerChanged(ImageLayer layer) {
    }

    public ImageView getSelectedView() {
        return selectedView;
    }

    public ImageLayer getSelectedLayer() {
        return selectedLayer;
    }
}
