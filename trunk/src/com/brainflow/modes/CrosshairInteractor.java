package com.brainflow.modes;

import com.brainflow.core.ImageView;
import com.brainflow.display.ICrosshair;
import com.brainflow.display.Viewport3D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

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

        ICrosshair crosshair = iview.getCrosshair();
        AnatomicalPoint3D ap = iview.getAnatomicalLocation(event.getComponent(), event.getPoint());
        ap = AnatomicalPoint3D.convertPoint(ap,crosshair.getAnatomy() );


        Viewport3D viewport = iview.getViewport();

        if (ap != null && viewport.inBounds(ap)) {
            iview.getCrosshair().setLocation(ap);
        } else {
            System.out.println("point is out of viewport bounds " + ap);
        }

    }

    private void moveCrosshair(Point p, Component source) {
        ImageView iview = getView();

        ICrosshair crosshair = iview.getCrosshair();
        AnatomicalPoint3D ap = iview.getAnatomicalLocation(source, p);
        ap= AnatomicalPoint3D.convertPoint(ap,crosshair.getAnatomy() );

        if (ap != null && iview.getViewport().inBounds(ap)) {
            iview.getCrosshair().setLocation(ap);
        } else {
            System.out.println("point is out of viewport bounds " + ap);
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
