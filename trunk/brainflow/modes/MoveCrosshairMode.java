package com.brainflow.modes;

import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;
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
public class MoveCrosshairMode extends ImageCanvasMode {


    private Point dragBegin;
    private boolean dragging = false;


    public MoveCrosshairMode(ImageCanvas canvas) {
        super.setImageCanvas(canvas);
    }


    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            System.out.println("mouse pressed: crosshair mode in control");
            dragging = true;
            ImageView iview = canvas.whichView(event.getPoint());
            if (iview == null) return;
            if (canvas.isSelectedView(iview)) {
                iview.getAnatomicalLocation((Component) event.getSource(), event.getPoint());

            }
        }

    }

    private void moveCrosshair(Point p, Component source) {
        ImageView iview = canvas.whichView(p);
        if (iview == null) return;
        if (canvas.isSelectedView(iview)) {

            AnatomicalPoint3D ap = iview.getAnatomicalLocation(source, p);
            iview.getCrosshair().setLocation(ap);

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
        if (SwingUtilities.isLeftMouseButton(event)) {
            moveCrosshair(event.getPoint(), (Component) event.getSource());

        }

    }
}
