/*
 * ImageViewDataEvent.java
 *
 * Created on June 26, 2006, 12:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.services;

import com.brainflow.core.ImageView;
import javax.swing.event.ListDataEvent;
import org.bushe.swing.event.AbstractEventServiceEvent;

/**
 *
 * @author buchs
 */
public class ImageViewDataEvent extends ImageViewEvent {
    
    private ImageView view;
    private ListDataEvent event;
    
    /** Creates a new instance of ImageViewDataEvent */
    public ImageViewDataEvent(ImageView _view, ListDataEvent _event) {
        super(_view);
        view = _view;
        event = _event;      
    }
    
    public ListDataEvent getListDataEvent() {
        return event;
    }
    
}
