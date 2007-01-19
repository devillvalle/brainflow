package com.brainflow.modes;

import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 22, 2004
 * Time: 1:37:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageCrossHairMode extends ImageCanvasMode implements MouseListener, MouseMotionListener {

    final static Logger log = Logger.getLogger(ImageCrossHairMode.class.getName());

    boolean dragging = false;
    Point dragBegin = null;

    public ImageCrossHairMode(ImageCanvas canvas) {
        super.setImageCanvas(canvas);
    }

   
    public void mouseClicked(MouseEvent e) {
        positionCrossHair(e);
    }

    public void mousePressed(MouseEvent event) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    private void positionCrossHair(MouseEvent e) {

        JComponent source = (JComponent) e.getSource();
        ImageView iview = canvas.whichView(e.getPoint());

        if (iview != null) {
            if (canvas.isSelectedView(iview)) {
                log.info("mouse clicked in Image View, positioning cross hair");
                //canvas.positionCrossHair(source, e.getPoint(), iview);
            }
        }


    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }
}
