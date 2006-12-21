package com.brainflow.core;


import com.brainflow.modes.ImageCanvasMode;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 27, 2004
 * Time: 11:35:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageCanvas extends JComponent implements MouseListener, MouseMotionListener {


    private ImageCanvasModel canvasModel = new ImageCanvasModel();
    private ImageCanvasSelection canvasSelection;


    private JRootPane rootPane = new JRootPane();

    private Logger log = Logger.getLogger(ImageCanvas.class.getName());

    private EventListenerList listenerList = new EventListenerList();

    private CloseHandler closeHandler = new CloseHandler();


    public ImageCanvas() {
        super();
        setLayout(new BorderLayout());

        rootPane.getGlassPane().setVisible(true);
        rootPane.getGlassPane().addMouseListener(this);
        rootPane.getGlassPane().addMouseMotionListener(this);
        add(rootPane, "Center");


        canvasSelection = new ImageCanvasSelection(this);


    }

    public ImageCanvasSelection getCanvasSelection() {
        return canvasSelection;
    }


    public static void main(String[] args) {
        JFrame jf1 = new JFrame("hello");
        ImageCanvas canvas = new ImageCanvas();
        jf1.add(canvas, BorderLayout.class);
        jf1.setSize(800, 800);

        SimpleImageView view = new SimpleImageView(new ImageDisplayModel("garbage"));
        canvas.getImageCanvasModel().addImageView(view);

    }

    public ImageCanvasModel getImageCanvasModel() {
        return canvasModel;
    }


    public ImageView whichSelectedView(Point p) {
        Component c = rootPane.getLayeredPane().findComponentAt(p);
        if (c instanceof ImageView) {
            return (ImageView) c;
        }

        Container ct = SwingUtilities.getAncestorOfClass(ImageView.class, c);

        if (ct != null & ct instanceof ImageView) {
            ImageView iv = (ImageView) ct;
            if (iv == canvasModel.getSelectedView())
                return iv;
        }

        return null;


    }

    public ImageView whichView(Point p) {
        Component c = rootPane.getLayeredPane().findComponentAt(p);

        if (c instanceof ImageView) {
            return findParentView((ImageView) c);
        }

        Container ct = SwingUtilities.getAncestorOfClass(ImageView.class, c);

        if (ct != null & ct instanceof ImageView) {
            return findParentView((ImageView) ct);
        }

        return null;

    }

    private ImageView findParentView(ImageView view) {
        ImageView ret = view;

        while (view.getParent() instanceof ImageView) {
            ret = (ImageView) view.getParent();
            view = ret;
        }

        return ret;

    }

    public Component whichComponent(Point p) {

        Component c = rootPane.getLayeredPane().findComponentAt(p);
        return c;


    }


    public IImagePlot whichPlot(Point p) {
        Component c = rootPane.getLayeredPane().findComponentAt(p);
        Container ct = SwingUtilities.getAncestorOfClass(ImagePane.class, c);
        if (ct != null & ct instanceof ImageView) {
            ImageView iview = (ImageView) ct;
            Point relPoint = SwingUtilities.convertPoint(rootPane.getLayeredPane(), p, iview);
            IImagePlot iplot = iview.whichPlot(relPoint);
            return iplot;
        }

        return null;


    }

    public java.util.List<IImagePlot> getPlotList() {
        java.util.List<IImagePlot> plots = new ArrayList<IImagePlot>();
        for (ImageView view : canvasModel.getImageViews()) {
            plots.addAll(view.getPlots());
        }

        return plots;
    }

    public boolean isSelectedView(ImageView view) {
        if (view == canvasModel.getSelectedView())
            return true;
        else
            return false;
    }

    //this special mouselistener stuff is for the birds... need to change.

    public void addSpecialMouseListener(MouseListener listener) {
        listenerList.add(MouseListener.class, listener);
        //mouseListeners.add(listener);
        //rootPane.getGlassPane().addMouseListener(listener);

    }

    public void removeSpecialMouseListener(MouseListener listener) {
        listenerList.remove(MouseListener.class, listener);
        //mouseListeners.remove(listener);
        //rootPane.getGlassPane().removeMouseListener(listener);
    }

    public void addSpecialMouseMotionListener(MouseMotionListener listener) {
        listenerList.add(MouseMotionListener.class, listener);
        //mouseMotionListeners.add(listener);
        //rootPane.getGlassPane().addMouseMotionListener(listener);
    }

    public void removeSpecialMouseMotionListener(MouseMotionListener listener) {
        listenerList.remove(MouseMotionListener.class, listener);
        //mouseMotionListeners.remove(listener);
        //rootPane.getGlassPane().removeMouseMotionListener(listener);
    }


    public ImageCanvasMode getCanvasMode() {
        return canvasSelection.getCanvasMode();
    }


    public JLayeredPane getLayeredPane() {
        return rootPane.getLayeredPane();
    }

    public ImageView[] getViews() {
        return canvasModel.getImageViews();
    }


    public void setSelectedView(ImageView view) {
        canvasSelection.setSelectedView(view);
    }

    public ImageView getSelectedView() {
        return canvasModel.getSelectedView();
    }

    /*public void swapImageView(ImageView oldView, ImageView newView) {
        //assert canvasModel.getImageViews().contains(oldView);
        Point loc = oldView.getLocation();

        boolean sel = false;
        if (oldView == canvasModel.getSelectedView()) {
            sel = true;
        }

        canvasModel.removeImageView(oldView);

        newView.setLocation(loc);
        canvasModel.addImageView(newView);

        if (sel) {
            canvasModel.setSelectedView(newView);
        }


    } */

    public void removeImageView(ImageView view) {
        if (view == getSelectedView()) {
            canvasSelection.clearSelection();
        }

        canvasModel.removeImageView(view);

        rootPane.getLayeredPane().remove(view);
    }

    /*private void addImageView(ImageView view, Point location) {
        view.setSize(view.getPreferredSize());
        view.setLocation(location);
        canvasModel.addImageView(view);
        //rootPane.getLayeredPane().add(view, JLayeredPane.DEFAULT_LAYER);

    } */

    public void addImageView(ImageView view) {

        view.setSize(view.getPreferredSize());
        /*HandledBorder border = new HandledBorder();
        border.setSelected(true);

        Border margin = new EmptyBorder(4,4,4,4);
        EmptyBorder margin2 = new EmptyBorder(3,3,3,3);
        view.setBackground(Color.GREEN);
        view.setBorder(new CompoundBorder(margin2, new CompoundBorder(border, margin)));   */


        view.setLocation(100, 100);
        view.addPropertyChangeListener(closeHandler);
        canvasModel.addImageView(view);
        rootPane.getLayeredPane().add(view, JLayeredPane.DEFAULT_LAYER);
        rootPane.getLayeredPane().moveToFront(view);

    }

    class CloseHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName() == ImageView.CLOSED_PROPERTY) {
                removeImageView((ImageView) evt.getSource());
            }
        }
    }


    public void moveToFront(ImageView view) {
        //assert canvasModel.getImageViews().contains(view);
        rootPane.getLayeredPane().moveToFront(view);
    }


    public void mouseClicked(MouseEvent e) {
        canvasSelection.mouseClicked(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mouseClicked(e);
        }


        redispatchMouseEvent(e);
    }

    public void mousePressed(MouseEvent e) {
        canvasSelection.mousePressed(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mousePressed(e);
        }
        redispatchMouseEvent(e);

    }

    public void mouseReleased(MouseEvent e) {
        canvasSelection.mouseReleased(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mouseReleased(e);
        }
        redispatchMouseEvent(e);

    }

    public void mouseEntered(MouseEvent e) {
        canvasSelection.mouseEntered(e);
        requestFocus();
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mouseEntered(e);
        }
        redispatchMouseEvent(e);
    }

    public void mouseExited(MouseEvent e) {
        canvasSelection.mouseExited(e);
        MouseListener[] mouseListeners = listenerList.getListeners(MouseListener.class);
        for (MouseListener ml : mouseListeners) {
            ml.mouseExited(e);
        }
        redispatchMouseEvent(e);


    }

    public void mouseDragged(MouseEvent e) {
        canvasSelection.mouseDragged(e);
        MouseMotionListener[] mouseMotionListeners = listenerList.getListeners(MouseMotionListener.class);
        for (MouseMotionListener ml : mouseMotionListeners) {
            ml.mouseDragged(e);
        }
        redispatchMouseEvent(e);

    }

    public void mouseMoved(MouseEvent e) {
        canvasSelection.mouseMoved(e);
        MouseMotionListener[] mouseMotionListeners = listenerList.getListeners(MouseMotionListener.class);

        for (MouseMotionListener ml : mouseMotionListeners) {
            ml.mouseMoved(e);
        }
        redispatchMouseEvent(e);

    }

    private void redispatchMouseEvent(MouseEvent e) {

        //System.out.println("redispatching event ...");
        Point glassPanePoint = e.getPoint();
        Container container = rootPane.getLayeredPane();
        Point containerPoint = SwingUtilities.convertPoint(rootPane.getGlassPane(),
                glassPanePoint,
                container);

        //System.out.println("container point is: " + containerPoint);
        if (containerPoint.y < 0 | containerPoint.x < 0) {
            System.out.println("dispatching mouse event outside container");
            System.out.println("ocontainer " + container);
            System.out.println("need to find where we are");
            rootPane.dispatchEvent(new MouseEvent((Component) e.getSource(), e.getID(),
                    e.getWhen(),
                    e.getModifiers(),
                    glassPanePoint.x,
                    glassPanePoint.y,
                    e.getClickCount(),
                    e.isPopupTrigger()));// forward event....


        } else {
            //The mouse event is probably over the content pane.
            //Find out exactly which component it's over.
            Component component =
                    SwingUtilities.getDeepestComponentAt(container,
                            containerPoint.x,
                            containerPoint.y);

            Component ancestor = SwingUtilities.getAncestorOfClass(ImageView.class, component);

            //System.out.println("component is : " + component.getClass().getCanonicalName());


            if (ancestor != null) {
                //System.out.println("ancestor is : " + ancestor.getClass().getCanonicalName());

                ImageView selView = (ImageView) ancestor;
                while (ancestor.getParent() instanceof ImageView) {
                    selView = (ImageView) ancestor.getParent();
                    ancestor = selView;
                }

                //System.out.println("selected view: " + canvasModel.getSelectedView());
                if (canvasModel.getSelectedView() != selView) {
                    //System.out.println("not forwarding event");
                    // not selected, do not forward mouse event ...
                } else {
                    //System.out.println("forwarding event");
                    Point componentPoint = SwingUtilities.convertPoint(rootPane.getGlassPane(),
                            glassPanePoint,
                            component);


                    component.dispatchEvent(new MouseEvent(component, e.getID(),
                            e.getWhen(),
                            e.getModifiers(),
                            componentPoint.x,
                            componentPoint.y,
                            e.getClickCount(),
                            e.isPopupTrigger()));// forward event....


                }
            }

        }
    }


}
