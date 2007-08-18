package com.brainflow.application.toplevel;

import com.brainflow.application.IBrainFlowClient;
import com.brainflow.application.services.ImageDisplayModelEvent;
import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageView;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 13, 2007
 * Time: 10:33:12 PM
 */
public class BrainFlowClientSupport {


    private IBrainFlowClient client;

    private ImageView selectedView;


    public BrainFlowClientSupport(IBrainFlowClient client) {
        this.client = client;


        EventBus.subscribe(ImageViewSelectionEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                ImageViewSelectionEvent event = (ImageViewSelectionEvent) evt;


                selectedView = event.getSelectedImageView();
                //selectedView.getModel().addImageDisplayModelListener(ImageViewPresenter.this);

                if (selectedView != null) {

                    BrainFlowClientSupport.this.client.viewSelected(selectedView);

                } else {
                    BrainFlowClientSupport.this.client.allViewsDeselected();
                }
            }
        });

        EventBus.subscribe(ImageViewLayerSelectionEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                EventServiceEvent ese = (EventServiceEvent) evt;
                if (ese.getSource() == selectedView) {
                    int idx = selectedView.getModel().getSelectedIndex();

                    if (idx >= 0) {
                        AbstractLayer newLayer = selectedView.getModel().getLayer(idx);
                        BrainFlowClientSupport.this.client.layerSelected(newLayer);
                    }

                }
            }
        });


        EventBus.subscribe(ImageDisplayModelEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                ImageDisplayModelEvent event = (ImageDisplayModelEvent) evt;

                ImageView view = selectedView;

                if (view == null) return;

                if (view.getModel() == event.getModel()) {
                    BrainFlowClientSupport.this.client.layerChangeNotification();
                    switch (event.getType()) {
                        case LAYER_ADDED:
                            BrainFlowClientSupport.this.client.layerAdded(event.getListDataEvent());
                            break;
                        case LAYER_CHANGED:
                            BrainFlowClientSupport.this.client.layerChanged(event.getListDataEvent());
                            break;
                        case LAYER_INTERVAL_ADDED:
                            BrainFlowClientSupport.this.client.layerIntervalAdded(event.getListDataEvent());
                            break;
                        case LAYER_INTERVAL_REMOVED:
                            BrainFlowClientSupport.this.client.layerIntervalRemoved(event.getListDataEvent());
                            break;
                        case LAYER_REMOVED:
                            BrainFlowClientSupport.this.client.layerRemoved(event.getListDataEvent());
                            break;
                    }
                }
            }
        });

    }


}
