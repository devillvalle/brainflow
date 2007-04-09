package com.brainflow.modes;

import com.brainflow.core.ImageCanvas2;
import com.brainflow.core.ImageView;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.display.Viewport3D;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 29, 2006
 * Time: 5:34:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairInteractor extends ImageViewInteractor {


    private boolean dragging = false;


    public CrosshairInteractor(ImageView view) {
        super(view);
    }


    public CrosshairInteractor() {
    }

    public void mousePressed(MouseEvent event) {
        ImageView iview = getView();
        if (iview == null) {
            return;
        }


        AnatomicalPoint3D pt = iview.getAnatomicalLocation(event.getComponent(), event.getPoint());
        Viewport3D viewport = iview.getViewport().getProperty();

        if (pt != null && viewport.inBounds(pt)) {
            iview.getCrosshair().getProperty().setLocation(pt);
        }

    }

    private void moveCrosshair(Point p, Component source) {
        ImageView iview = getView();

        AnatomicalPoint3D ap = iview.getAnatomicalLocation(source, p);
        if (iview.getViewport().getProperty().inBounds(ap)) {
            iview.getCrosshair().getProperty().setLocation(ap);
        }

    }


    public void mouseReleased(MouseEvent event) {
        if (dragging) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                moveCrosshair(event.getPoint(), (Component) event.getSource());

            }

            dragging = false;
        }
    }


    public void mouseDragged(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event) && !event.isAltDown() && !event.isControlDown() && !event.isShiftDown()) {
            dragging = true;
            moveCrosshair(event.getPoint(), (Component) event.getSource());

        }

    }
}
