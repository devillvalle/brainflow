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
import org.bushe.swing.event.AbstractEventServiceEvent;

import javax.swing.event.ListDataEvent;

/**
 * @author buchs
 */
public class ImageDisplayModelEvent extends AbstractEventServiceEvent {


    private ListDataEvent event;
    private IImageDisplayModel model;

    /**
     * Creates a new instance of ImageDisplayModelEvent
     */

    public ImageDisplayModelEvent(IImageDisplayModel _model, ListDataEvent _event) {
        super(_event);
        event = _event;
        model = _model;

    }

    public ListDataEvent getListDataEvent() {
        return event;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

}
