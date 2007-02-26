/*
 * ImageDisplayModelEvent.java
 *
 * Created on June 26, 2006, 12:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.services;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.application.BrainflowProject;
import com.brainflow.application.toplevel.BrainflowProjectEvent;
import org.bushe.swing.event.AbstractEventServiceEvent;

import javax.swing.event.ListDataEvent;

/**
 * @author buchs
 */
public class ImageDisplayModelEvent extends AbstractEventServiceEvent {

    public enum TYPE {
        MODEL_ADDED,
        MODEL_REMOVED,
        MODEL_CHANGED,
        MODEL_INTERVAL_ADDED,
        MODEL_INTERVAL_REMOVED
    }

    private BrainflowProjectEvent event;
    private TYPE type;
   

    /**
     * Creates a new instance of ImageDisplayModelEvent
     */

    public ImageDisplayModelEvent(BrainflowProjectEvent _event, TYPE _type) {
        super(_event);
        event = _event;
        type = _type;
    }


    public TYPE getType() {
        return type;
    }

    public ListDataEvent getListDataEvent() {
        //todo do we need to expose low level event especially since it might be null?
        // could have getIndex
        // etc ..
        return event.getListDataEvent();
    }

    public IImageDisplayModel getModel() {
        return event.getModel();
    }

    public BrainflowProject getProject() {
        return event.getProject();
    }

}
