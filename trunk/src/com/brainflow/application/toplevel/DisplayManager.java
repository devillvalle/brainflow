/*
 * DisplayManager.java
 *
 * Created on April 30, 2003, 10:45 AM
 */

package com.brainflow.application.toplevel;

import com.brainflow.application.services.ImageViewMousePointerEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.application.dnd.ImageViewTransferHandler;
import com.brainflow.core.*;
import com.brainflow.modes.ImageViewInteractor;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;

import org.bushe.swing.event.EventBus;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Bradley
 */


public class DisplayManager {

    public static final String SELECTED_CANVAS_PROPERTY = "selectedCanvas";


    private static final Logger log = Logger.getLogger(DisplayManager.class.getName());

    private List<IBrainCanvas> canvasList = new ArrayList<IBrainCanvas>();

    private IBrainCanvas selectedCanvas = null;
   
    private PropertyChangeSupport support = new PropertyChangeSupport(this);


    private CanvasSelectionListener canvasListener;

    private ImageViewMouseMotionListener cursorListener;




    //private WeakHashMap<ImageView, IImageDisplayModel> registeredViews = new WeakHashMap<ImageView, IImageDisplayModel>();

    

    protected DisplayManager() {

    }


    public static DisplayManager getInstance() {
        return (DisplayManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.DisplayManager");
    }

    private void listenToCanvas(IBrainCanvas canvas) {
        if (canvasListener == null) canvasListener = new CanvasSelectionListener();
         if (cursorListener == null) cursorListener = new ImageViewMouseMotionListener();

        //canvas.getImageCanvasModel().addPropertyChangeListener(canvasListener);
        //canvas.getImageCanvasModel().listSelection.

        BeanContainer.get().addListener(canvas.getImageCanvasModel().listSelection, canvasListener);

        canvas.addInteractor(cursorListener);


    }


    /*public String register(ImageView view) {
        if (registeredViews.containsKey(view)) {
            log.warning("view " + view + " is already registered, no action taken");
            return view.getId();

        }

        registeredViews.put(view, view.getModel());

        if (registeredViews.size() >= ids.length) {
            return String.valueOf(registeredViews.size());
        }


        return ids[registeredViews.size() - 1];
    }*/


    public void addImageCanvas(IBrainCanvas _canvas) {
        canvasList.add(_canvas);
        if (selectedCanvas == null)
            selectedCanvas = _canvas;

        listenToCanvas(_canvas);

        //List<ImageView> views = _canvas.getImageCanvasModel().getImageViews();

        //for (ImageView v : views) {
        //    registeredViews.put(v, v.getModel());

        //}

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public IBrainCanvas getSelectedCanvas() {
        return selectedCanvas;
    }

    public void displayView(ImageView view) {
        view.setTransferHandler(new ImageViewTransferHandler());
        getSelectedCanvas().addImageView(view);
    }

    public void setSelectedCanvas(IBrainCanvas canvas) {
        if (canvasList.contains(canvas)) {
            IBrainCanvas oldCanvas = this.selectedCanvas;
            selectedCanvas = canvas;
            support.firePropertyChange(SELECTED_CANVAS_PROPERTY, oldCanvas, selectedCanvas);
        } else {
            throw new RuntimeException("ImageCanvas " + canvas + " is not currently managed my DisplayManager.");
        }
    }


    public IBrainCanvas[] getImageCanvases() {
        BrainCanvas[] canvi = new BrainCanvas[canvasList.size()];
        canvasList.toArray(canvi);
        return canvi;
    }

    public IBrainCanvas createCanvas() {
        BrainCanvas canvas = new BrainCanvas();
        addImageCanvas(canvas);
        return canvas;
    }

    public IBrainCanvas getImageCanvas(int idx) {
        if (canvasList.size() > idx && idx >= 0)
            return canvasList.get(idx);
        else {
            throw new IllegalArgumentException("No canvas exists at index" + idx);
        }
    }

    public void removeImageCanvas(IBrainCanvas canvas) {
        if (canvasList.contains(canvas)) {
            canvasList.remove(canvas);

            BeanContainer.get().removeListener(canvas.getImageCanvasModel().listSelection, canvasListener);
            
        }

    }

    /*public void addImageView(ImageView view) {
        if (!registeredViews.containsKey(view)) {
            register(view);
            BrainCanvas canvas = getSelectedCanvas();
            if (canvas != null) {
                canvas.add(view);
            }

        }
    }*/

    public ImageView getSelectedImageView() {
        if (selectedCanvas != null)
            return selectedCanvas.getSelectedView();

        //todo is this correct to return null?
        return null;

    }

    public Set<ImageView> getYokedViews(ImageView view) {
        return getSelectedCanvas().getImageCanvasModel().getYokedViews(view);
    }

    
    public void unyoke(ImageView target1, ImageView target2) {
        getSelectedCanvas().getImageCanvasModel().unyoke(target1, target2);

    }


    public void yoke(ImageView target1, ImageView target2) {
        getSelectedCanvas().getImageCanvasModel().yoke(target1, target2);

    }


    class CanvasSelectionListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            BrainCanvasModel model = (BrainCanvasModel)prop.getParent();
            log.info("image view slected: firing event");
            EventBus.publish(new ImageViewSelectionEvent(model.getSelectedView()));


        }


    }



    class ImageViewMouseMotionListener extends ImageViewInteractor {


        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            ImageView iview = selectedCanvas.whichView((Component) e.getSource(), p);

            if (iview != selectedCanvas.getSelectedView()) {
                return;
            }

            EventBus.publish(new ImageViewMousePointerEvent(iview, e));

        }
    }


}
