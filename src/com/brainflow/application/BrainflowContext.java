package com.brainflow.application;

import com.brainflow.application.actions.ActionContext;
import com.brainflow.application.services.ImageViewSelectionEvent;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 1, 2006
 * Time: 12:00:50 AM
 * To change this template use File | Settings | File Templates.
 */


public class BrainflowContext {

    private Map context = new HashMap();


    public BrainflowContext() {
        init();
    }

    public void putValue(ActionContext key, Object value) {
        context.put(key, value);

    }


    private void init() {
        EventBus.subscribeStrongly(ImageViewSelectionEvent.class, new EventSubscriber() {
            public void onEvent(Object evt) {
                ImageViewSelectionEvent event = (ImageViewSelectionEvent) evt;
                

            }
        });
    }


}