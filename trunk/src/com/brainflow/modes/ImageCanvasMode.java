package com.brainflow.modes;

import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImagePane;
import com.brainflow.core.ImageCanvas2;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public abstract class ImageCanvasMode implements MouseListener, MouseMotionListener, KeyListener {

    protected ImageCanvas2 canvas;

    
    public void setImageCanvas(ImageCanvas2 _canvas) {
        canvas = _canvas;
    }

    public ImageCanvas2 getImageCanvas() {
        return canvas;
    }

    public boolean stillInterestedBefore(MouseEvent event, MouseAction action) {
        return true;
    }

    public boolean stillInterestedAfter(MouseEvent event, MouseAction action) {
        return true;
    }


    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseDragged(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseMoved(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyPressed(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyReleased(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}