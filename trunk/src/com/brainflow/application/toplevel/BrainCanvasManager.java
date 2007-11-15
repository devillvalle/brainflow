/*
 * BrainCanvasManager.java
 *
 * Created on April 30, 2003, 10:45 AM
 */

package com.brainflow.application.toplevel;

import com.brainflow.application.YokeHandler;
import com.brainflow.application.services.ImageViewCursorEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.core.BrainCanvas;
import com.brainflow.core.BrainCanvasModel;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageView;
import com.brainflow.modes.ImageViewInteractor;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;
import net.java.dev.properties.BaseProperty;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;
import org.bushe.swing.event.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Bradley
 */
public class BrainCanvasManager {

    private static final Logger log = Logger.getLogger(BrainCanvasManager.class.getName());

    private List<BrainCanvas> canvasList = new ArrayList<BrainCanvas>();

    private BrainCanvas selectedCanvas = null;

    public static final String SELECTED_CANVAS_PROPERTY = "selectedCanvas";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ContextMenuHandler contextMenuHandler;

    private CanvasSelectionListener canvasListener;

    private ImageViewMouseMotionListener cursorListener;


    private Map<ImageView, YokeHandler> yokeHandlers = new HashMap<ImageView, YokeHandler>();

    private WeakHashMap<ImageView, IImageDisplayModel> registeredViews = new WeakHashMap<ImageView, IImageDisplayModel>();

    

    protected BrainCanvasManager() {
        // Exists only to thwart instantiation.
        //EventBus.subscribe(ImageViewCrosshairEvent.class, this);
    }


    public static BrainCanvasManager getInstance() {
        return (BrainCanvasManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.BrainCanvasManager");
    }

    private void listenToCanvas(BrainCanvas canvas) {
        if (canvasListener == null) canvasListener = new CanvasSelectionListener();
        if (contextMenuHandler == null) contextMenuHandler = new ContextMenuHandler();
        if (cursorListener == null) cursorListener = new ImageViewMouseMotionListener();

        //canvas.getImageCanvasModel().addPropertyChangeListener(canvasListener);
        //canvas.getImageCanvasModel().listSelection.

        BeanContainer.get().addListener(canvas.getImageCanvasModel().listSelection, canvasListener);

        canvas.addMouseListener(contextMenuHandler);
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


    public void addImageCanvas(BrainCanvas _canvas) {
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

    public BrainCanvas getSelectedCanvas() {
        return selectedCanvas;
    }

    public void setSelectedCanvas(BrainCanvas canvas) {
        if (canvasList.contains(canvas)) {
            BrainCanvas oldCanvas = this.selectedCanvas;
            selectedCanvas = canvas;
            support.firePropertyChange(SELECTED_CANVAS_PROPERTY, oldCanvas, selectedCanvas);
        } else {
            throw new RuntimeException("ImageCanvas " + canvas + " is not currently managed my BrainCanvasManager.");
        }
    }


    public BrainCanvas[] getImageCanvases() {
        BrainCanvas[] canvi = new BrainCanvas[canvasList.size()];
        canvasList.toArray(canvi);
        return canvi;
    }

    public BrainCanvas createCanvas() {
        BrainCanvas canvas = new BrainCanvas();
        addImageCanvas(canvas);
        return canvas;
    }

    public BrainCanvas getImageCanvas(int idx) {
        if (canvasList.size() > idx && idx >= 0)
            return (BrainCanvas) canvasList.get(idx);
        else {
            throw new IllegalArgumentException("No canvas exists at index" + idx);
        }
    }

    public void removeImageCanvas(BrainCanvas canvas) {
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
        YokeHandler handler = yokeHandlers.get(view);

        Set<ImageView> ret;
        if (handler == null) {
            ret = new HashSet<ImageView>();
        } else {
            ret = handler.getSources();
        }

        return ret;

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


    class CanvasSelectionListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            BrainCanvasModel model = (BrainCanvasModel)prop.getParent();
            EventBus.publish(new ImageViewSelectionEvent(model.getSelectedView()));


        }


    }

    class ContextMenuHandler extends MouseAdapter {


        public void mouseReleased(MouseEvent e) {
            Action yokeAction = ActionManager.getInstance().getAction("yoke-views");
            Action unyokeAction = ActionManager.getInstance().getAction("unyoke-views");
            if (e.isPopupTrigger()) {
                JPopupMenu popup = new JPopupMenu();

                popup.add(ActionUIFactory.getInstance().createMenuItem(yokeAction));
                popup.add(ActionUIFactory.getInstance().createMenuItem(unyokeAction));
                popup.setInvoker(getSelectedCanvas());
                popup.setLocation(e.getXOnScreen(), e.getYOnScreen());
                popup.setVisible(true);
            }
        }
    }


    class ImageViewMouseMotionListener extends ImageViewInteractor {


        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            ImageView iview = selectedCanvas.whichView((Component) e.getSource(), p);

            if (iview != selectedCanvas.getSelectedView()) {
                return;
            }

            EventBus.publish(new ImageViewCursorEvent(iview, e));

        }
    }


}