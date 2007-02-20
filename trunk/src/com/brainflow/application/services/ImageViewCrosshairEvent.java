package com.brainflow.application.services;

import com.brainflow.core.ImageView;
import com.brainflow.display.ICrosshair;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 17, 2006
 * Time: 6:18:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewCrosshairEvent extends ImageViewEvent {
    /**
     * Creates a new instance of ImageViewEvent
     */
    public ImageViewCrosshairEvent(ImageView view) {        
        super(view);
    }

    public ICrosshair getCrosshair() {
        return getImageView().getCrosshair();
    }


}
