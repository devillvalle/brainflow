package com.brainflow.application.services;

import com.brainflow.core.ImageView;
import com.brainflow.image.anatomy.AnatomicalPoint;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 23, 2006
 * Time: 12:40:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewCursorEvent extends ImageViewMouseEvent {

    private AnatomicalPoint3D ap;

    public ImageViewCursorEvent(ImageView view, MouseEvent _event) {
        super(view, _event);

    }

    public AnatomicalPoint3D getLocation() {
        if (getImageView() == null) {
            return null;
        }

        if (ap == null) {
            MouseEvent event = getEvent();
            ap = getImageView().getAnatomicalLocation((Component) event.getSource(), event.getPoint());
        }

        return ap;

    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Cursor at : ");
        AnatomicalPoint tmp = getLocation();
        if (tmp == null) return "cursor : --";

        builder.append("zero: " + (int) getLocation().getX());
        builder.append(" zero: " + (int) getLocation().getY());
        builder.append(" one: " + (int) getLocation().getZ());

        return builder.toString();
    }
}
