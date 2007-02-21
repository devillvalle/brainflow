package com.brainflow.application.services;

import com.brainflow.core.ImageView;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 27, 2006
 * Time: 1:21:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewSelectionEvent extends ImageViewEvent {

    private ImageView view;

    /**
     * Creates a new instance of ImageDisplayModelEvent
     */
    public ImageViewSelectionEvent(ImageView _view) {
        super(_view);
        view = _view;

    }

    public ImageView getSelectedImageView() {
        return view;
    }

    public String toString() {
        return "Selected View Changed : " + view.getName();
    }

}
