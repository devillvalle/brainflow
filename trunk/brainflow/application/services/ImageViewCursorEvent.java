package com.brainflow.application.services;

import org.bushe.swing.event.AbstractEventServiceEvent;

import java.awt.event.MouseEvent;
import java.awt.*;

import com.brainflow.core.ImageView;
import com.brainflow.image.anatomy.AnatomicalPointOnGrid;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 23, 2006
 * Time: 12:40:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewCursorEvent extends ImageViewMouseEvent {


    public ImageViewCursorEvent(ImageView view, MouseEvent _event) {
        super(view, _event);

    }

    public AnatomicalPoint3D getLocation() {
        if (getImageView() == null) {
            return null;
        }


        MouseEvent event = getEvent();
        AnatomicalPoint3D ap = getImageView().getAnatomicalLocation((Component)event.getSource(), event.getPoint());

        return ap;

    }
}
