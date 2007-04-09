package com.brainflow.modes;

import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageCanvas2;
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



    private boolean dragging = false;


    public MoveCrosshairMode(ImageCanvas2 canvas) {
        super.setImageCanvas(canvas);
    }


    public void mousePressed(MouseEvent event) {
        /*if (SwingUtilities.isLeftMouseButton(event)) {


            ImageView iview = canvas.whichView(event.getPoint());
            if (iview == null) return;
            if (canvas.isSelectedView(iview)) {
                iview.getAnatomicalLocation((Component) event.getSource(), event.getPoint());

            }
        } */

    }

    private void moveCrosshair(Point p, Component source) {
        ImageView iview = canvas.whichView(source, p);
        if (iview == null) return;
        if (canvas.isSelectedView(iview)) {

            AnatomicalPoint3D ap = iview.getAnatomicalLocation(source, p);
            if (iview.getViewport().getProperty().inBounds(ap)) {
                iview.getCrosshair().getProperty().setLocation(ap);
            }

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
            dragging = true;
            moveCrosshair(event.getPoint(), (Component) event.getSource());

        }

    }
}
