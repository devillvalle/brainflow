/*
 * ImageLayerSelectionEvent.java
 *
 * Created on July 5, 2006, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.services;

import com.brainflow.core.ImageView;

/**
 * @author buchs
 */
public class ImageViewLayerSelectionEvent extends ImageViewEvent {

    private int selectionIndex;

    /**
     * Creates a new instance of ImageLayerSelectionEvent
     */

    public ImageViewLayerSelectionEvent(ImageView view, int selectionIndex) {
        super(view);
        this.selectionIndex = selectionIndex;
    }

    public int getSelectionIndex() {
        return selectionIndex;
    }

    public String toString() {
        return getImageView().getName() + " Selected Layer : " + selectionIndex;
    }

}
