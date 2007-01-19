/*
 * LoadableImageProgressEvent.java
 *
 * Created on June 22, 2006, 3:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.services;

/**
 *
 * @author buchs
 */
public class LoadableImageProgressEvent extends ProgressEvent {
    
    /** Creates a new instance of LoadableImageProgressEvent */
    public LoadableImageProgressEvent(Object source, int _progress, String _message) {
        super(source, _progress, _message);
    }
    
}
