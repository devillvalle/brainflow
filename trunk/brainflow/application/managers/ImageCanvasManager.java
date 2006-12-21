/*
 * ImageCanvasManager.java
 *
 * Created on April 30, 2003, 10:45 AM
 */

package com.brainflow.application.managers;

import com.brainflow.core.ImageCanvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bradley
 */
public class ImageCanvasManager implements PropertyChangeListener, MouseMotionListener {

    private static List canvasList = new ArrayList();

    private ImageCanvas selectedCanvas = null;

    public static final String SELECTED_CANVAS_PROPERTY = "selectedCanvas";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    protected ImageCanvasManager() {
        // Exists only to thwart instantiation.
    }

    public static ImageCanvasManager getInstance() {
        return (ImageCanvasManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.managers.ImageCanvasManager");
    }

    public void addImageCanvas(ImageCanvas _canvas) {
        canvasList.add(_canvas);
        if (selectedCanvas == null)
            selectedCanvas = _canvas;

        _canvas.addMouseMotionListener(this);
        _canvas.addPropertyChangeListener(this);

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public ImageCanvas getSelectedCanvas() {
        return selectedCanvas;
    }

    public void setSelectedCanvas(ImageCanvas canvas) {
        if (canvasList.contains(canvas)) {
            ImageCanvas oldCanvas = this.selectedCanvas;
            selectedCanvas = canvas;
            support.firePropertyChange(SELECTED_CANVAS_PROPERTY, oldCanvas, selectedCanvas);
        }
    }


    public ImageCanvas[] getImageCanvases() {
        ImageCanvas[] canvi = new ImageCanvas[canvasList.size()];
        canvasList.toArray(canvi);
        return canvi;
    }

    public ImageCanvas createCanvas() {
        ImageCanvas canvas = new ImageCanvas();
        addImageCanvas(canvas);
        return canvas;
    }

    public ImageCanvas getImageCanvas(int idx) {
        if (canvasList.size() > idx)
            return (ImageCanvas) canvasList.get(idx);
        return null;
    }

    public void removeImageCanvas(ImageCanvas canvas) {
        if (canvasList.contains(canvas))
            canvasList.remove(canvas);
    }


    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {


    }


}
