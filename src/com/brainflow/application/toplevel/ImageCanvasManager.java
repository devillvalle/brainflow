/*
 * ImageCanvasManager.java
 *
 * Created on April 30, 2003, 10:45 AM
 */

package com.brainflow.application.toplevel;

import com.brainflow.application.services.ImageViewCrosshairEvent;
import com.brainflow.application.services.ImageViewCursorEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.application.YokeHandler;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageCanvasModel;
import com.brainflow.core.ImageView;
import com.brainflow.display.Viewport3D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Bradley
 */
public class ImageCanvasManager  {

    private static final Logger log = Logger.getLogger(ImageCanvasManager.class.getName());

    private List<ImageCanvas> canvasList = new ArrayList<ImageCanvas>();

    private ImageCanvas selectedCanvas = null;

    public static final String SELECTED_CANVAS_PROPERTY = "selectedCanvas";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ContextMenuHandler contextMenuHandler;

    private CanvasPropertyChangeListener canvasListener;

    private MouseListener localMouseListener;

    private MouseMotionListener localMouseMotionListener;

    private Map<ImageView, YokeHandler> yokeHandlers = new HashMap<ImageView, YokeHandler>();

    private WeakHashMap<ImageView, IImageDisplayModel> registeredViews = new WeakHashMap<ImageView, IImageDisplayModel>();

    private static final String[] ids = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    protected ImageCanvasManager() {
        // Exists only to thwart instantiation.
        //EventBus.subscribe(ImageViewCrosshairEvent.class, this);
    }


    public static ImageCanvasManager getInstance() {
        return (ImageCanvasManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.ImageCanvasManager");
    }

    private void listenToCanvas(ImageCanvas canvas) {
        if (canvasListener == null) canvasListener = new CanvasPropertyChangeListener();
        if (localMouseListener == null) localMouseListener = new ImageViewMouseListener();
        if (localMouseMotionListener == null) localMouseMotionListener = new ImageViewMouseMotionListener();
        if (contextMenuHandler == null) contextMenuHandler = new ContextMenuHandler();

        canvas.getImageCanvasModel().addPropertyChangeListener(canvasListener);
        canvas.addSpecialMouseMotionListener(localMouseMotionListener);
        canvas.addSpecialMouseListener(localMouseListener);
        canvas.addSpecialMouseListener(contextMenuHandler);


    }


    public String register(ImageView view) {
        if (registeredViews.containsKey(view)) {
            log.warning("view " + view + " is already registered, no action taken");
            return view.getId();

        }

        registeredViews.put(view, view.getModel());

        if (registeredViews.size() >= ids.length) {
            return String.valueOf(registeredViews.size());
        }


        return ids[registeredViews.size() - 1];
    }


    public void addImageCanvas(ImageCanvas _canvas) {
        canvasList.add(_canvas);
        if (selectedCanvas == null)
            selectedCanvas = _canvas;

        listenToCanvas(_canvas);

        List<ImageView> views = _canvas.getImageCanvasModel().getImageViews();

        for (ImageView v : views) {
            registeredViews.put(v, v.getModel());
        }

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
        } else {
            throw new RuntimeException("ImageCanvas " + canvas + " is not currently managed my ImageCanvasManager.");
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
        if (canvasList.size() > idx && idx >= 0)
            return (ImageCanvas) canvasList.get(idx);
        else {
            throw new IllegalArgumentException("No canvas exists at index" + idx);
        }
    }

    public void removeImageCanvas(ImageCanvas canvas) {
        if (canvasList.contains(canvas)) {
            canvasList.remove(canvas);
            canvas.removeSpecialMouseListener(localMouseListener);
            canvas.removeSpecialMouseMotionListener(localMouseMotionListener);
            canvas.removePropertyChangeListener(canvasListener);
        }

    }


    public void addImageView(ImageView view) {
        if (!registeredViews.containsKey(view)) {
            register(view);
            ImageCanvas canvas = getSelectedCanvas();
            if (canvas != null) {
                canvas.add(view);
            }

        }
    }

    public ImageView getSelectedImageView() {
        if (selectedCanvas != null)
            return selectedCanvas.getSelectedView();

        //todo is this correct to return null?
        return null;

    }

    public void unyoke(ImageView target1, ImageView target2) {
        YokeHandler handler = yokeHandlers.get(target1);
        if (handler != null) {
            handler.removeSource(target2);
        }

        handler = yokeHandlers.get(target2);
        if (handler != null) {
            handler.removeSource(target1);
        }

    }

    
    public void yoke(ImageView target1, ImageView target2) {
        
        log.info("Yoking : " + target1 + " to " + target2);
        YokeHandler handler = yokeHandlers.get(target1);
        if (handler == null) {
            handler = new YokeHandler(target1);
            yokeHandlers.put(target1, handler);
        }

        handler.addSource(target2);

        handler = yokeHandlers.get(target2);
        if (handler == null) {
            handler = new YokeHandler(target2);
            yokeHandlers.put(target2, handler);
        }

        handler.addSource(target1);



    }




    class CanvasPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent e) {

            if (e.getPropertyName() == ImageCanvasModel.SELECTED_VIEW_PROPERTY) {
                ImageView selectedView = (ImageView) e.getNewValue();
                EventBus.publish(new ImageViewSelectionEvent(selectedView));

            }
        }

    }

    class ContextMenuHandler extends MouseAdapter {


        public void mouseReleased(MouseEvent e) {
            Action yokeAction = ActionManager.getInstance().getAction("yoke-views");
            Action unyokeAction = ActionManager.getInstance().getAction("unyoke-views");
            if (e.isPopupTrigger()) {
                JPopupMenu popup = new JPopupMenu();
                System.out.println(yokeAction);
                System.out.println(unyokeAction);
                popup.add(ActionUIFactory.getInstance().createMenuItem(yokeAction));
                popup.add(ActionUIFactory.getInstance().createMenuItem(unyokeAction));
                popup.setInvoker(getSelectedCanvas());
                popup.setLocation(e.getXOnScreen(), e.getYOnScreen());
                popup.setVisible(true);
            }
        }
    }


    class ImageViewMouseMotionListener extends MouseMotionAdapter {


        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            ImageView iview = selectedCanvas.whichView((Component) e.getSource(), p);

            if (iview != selectedCanvas.getSelectedView()) {
                return;
            }

            EventBus.publish(new ImageViewCursorEvent(iview, e));

        }
    }

    class ImageViewMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();

            ImageView iview = selectedCanvas.whichView((Component) e.getSource(), p);
            if (iview == null || iview != selectedCanvas.getSelectedView()) {
                return;
            }


            AnatomicalPoint3D pt = iview.getAnatomicalLocation(e.getComponent(), e.getPoint());
            Viewport3D viewport = iview.getViewport();

            if (pt != null && viewport.inBounds(pt)) {
                iview.getCrosshair().getProperty().setLocation(pt);
            }
        }


    }





}
