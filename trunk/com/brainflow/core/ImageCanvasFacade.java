package com.brainflow.core;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.EventListenerList;
import javax.swing.*;

import com.brainflow.application.managers.ImageCanvasManager;
import com.brainflow.application.services.ImageViewCursorEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.image.anatomy.AnatomicalPointOnGrid;
import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import org.bushe.swing.event.EventBus;

public class ImageCanvasFacade {

    public static final String SELECTED_CANVAS_PROPERTY = "selectedCanvas";


    private ImageCanvasManager manager;

    private ImageCanvas selectedCanvas;

    private AnatomicalPointOnGrid cursorPosition;

    private EventListenerList listeners = new EventListenerList();

    private CanvasPropertyChangeListener canvasListener;
    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    MouseListener localMouseListener = new ImageViewMouseListener();
    MouseMotionListener localMouseMotionListener = new ImageViewMouseMotionListener();


    public ImageCanvasFacade() {
        manager = ImageCanvasManager.getInstance();
        selectedCanvas = manager.getSelectedCanvas();
        listenToCanvas();

        manager.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName() == ImageCanvasManager.SELECTED_CANVAS_PROPERTY) {

                    if (selectedCanvas != null) {
                        selectedCanvas.removePropertyChangeListener(canvasListener);
                    }


                    selectedCanvas = (ImageCanvas) e.getNewValue();

                    listenToCanvas();
                    updateListeners((ImageCanvas) e.getOldValue());
                    changeSupport.firePropertyChange(ImageCanvasFacade.SELECTED_CANVAS_PROPERTY, e.getOldValue(), e.getNewValue());
                }
            }
        });


        listeners.add(MouseMotionListener.class, localMouseMotionListener);
        listeners.add(MouseListener.class, localMouseListener);

        updateListeners(null);


    }

    public ImageCanvas getSelectedCanvas() {
        return selectedCanvas;
    }

    public ImageView getSelectedImageView() {
        if (selectedCanvas != null)
            return selectedCanvas.getSelectedView();

        return null;
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    private void listenToCanvas() {
        if (selectedCanvas != null) {
            canvasListener = new CanvasPropertyChangeListener();
            selectedCanvas.getImageCanvasModel().addPropertyChangeListener(canvasListener);
        }
    }


    private void updateListeners(ImageCanvas oldCanvas) {

        MouseListener[] ml = listeners.getListeners(MouseListener.class);
        for (MouseListener m : ml) {
            if (oldCanvas != null) {
                oldCanvas.removeSpecialMouseListener(m);
            }
            if (selectedCanvas != null) {
                selectedCanvas.addSpecialMouseListener(m);
            }
        }

        MouseMotionListener[] mml = listeners.getListeners(MouseMotionListener.class);

        for (MouseMotionListener m : mml) {
            if (oldCanvas != null) {
                oldCanvas.removeSpecialMouseMotionListener(m);
            }
            if (selectedCanvas != null) {
                selectedCanvas.addSpecialMouseMotionListener(m);
            }
        }

    }

    public void addMouseListener(MouseListener listener) {
        listeners.add(MouseListener.class, listener);
        if (selectedCanvas != null) {
            selectedCanvas.addSpecialMouseListener(listener);
        }

    }

    public void addMouseMotionListener(MouseMotionListener listener) {
        listeners.add(MouseMotionListener.class, listener);
        if (selectedCanvas != null) {
            selectedCanvas.addSpecialMouseMotionListener(listener);
        }
    }

    private void showBorders(Component[] comps) {
        for (int i = 0; i < comps.length; i++) {
            JComponent c = (JComponent) comps[i];
            Component[] nc = c.getComponents();
            if (nc.length > 0) {
                showBorders(nc);
            }
            //System.out.println("class: " + c.getClass().getCanonicalName());
            //System.out.println("border: " + c.getBorder());
            //System.out.println("dim: " + c.getSize());
            //System.out.println("bg color: " + c.getBackground());
            //System.out.println("is opaque: " + c.isOpaque());
            // System.out.println("component insets: " + c.getInsets());

        }

    }

    class ImageViewMouseMotionListener implements MouseMotionListener {

        public void mouseDragged(MouseEvent e) {


        }

        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            ImageView iview = selectedCanvas.whichView(p);
            if (iview != selectedCanvas.getSelectedView()) {
                return;
            }

            EventBus.publish(new ImageViewCursorEvent(iview, e));

        }
    }

    class ImageViewMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {

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
    }

    class CanvasPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent e) {
            System.out.println("catching property " + e.getPropertyName());
            System.out.println("from source " + e.getSource().getClass());
            if (e.getPropertyName() == ImageCanvasModel.SELECTED_VIEW_PROPERTY) {
                ImageView selectedView = (ImageView) e.getNewValue();

                EventBus.publish(new ImageViewSelectionEvent(selectedView));
                
                //changeSupport.firePropertyChange(ImageCanvasModel.SELECTED_VIEW_PROPERTY, e.getOldValue(), e.getNewValue());
            }
        }

    }

    class CursorListener implements MouseMotionListener {

        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            ImageView iview = selectedCanvas.whichSelectedView(p);
            if (iview != null) {
                //System.out.println("class: " + iview.getClass().getCanonicalName());
                //System.out.println("dim: " + iview.getSize());
                //System.out.println("component insets: " + iview.getInsets());
                //System.out.println("border: " + iview.getBorder());
                // if (iview.getBorder() != null) {
                //System.out.println("border insets: " + iview.getBorder().getBorderInsets(iview));
                Component[] comps = iview.getComponents();
                showBorders(comps);


            }

        }

    }
}
