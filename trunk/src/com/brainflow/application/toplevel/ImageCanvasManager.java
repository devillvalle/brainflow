/*
 * ImageCanvasManager.java
 *
 * Created on April 30, 2003, 10:45 AM
 */

package com.brainflow.application.toplevel;

import com.brainflow.application.services.ImageViewCrosshairEvent;
import com.brainflow.application.services.ImageViewCursorEvent;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageCanvasModel;
import com.brainflow.core.ImageView;
import com.brainflow.display.Viewport3D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

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
public class ImageCanvasManager implements EventSubscriber {

    private static final Logger log = Logger.getLogger(ImageCanvasManager.class.getName());

    private List<ImageCanvas> canvasList = new ArrayList<ImageCanvas>();

    private ImageCanvas selectedCanvas = null;

    public static final String SELECTED_CANVAS_PROPERTY = "selectedCanvas";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ContextMenuHandler contextMenuHandler;

    private CanvasPropertyChangeListener canvasListener;

    private MouseListener localMouseListener;

    private MouseMotionListener localMouseMotionListener;

    private Map<ImageView, Set<ImageView>> linkedViews = new HashMap<ImageView, Set<ImageView>>();

    private WeakHashMap<ImageView, IImageDisplayModel> registeredViews = new WeakHashMap<ImageView, IImageDisplayModel>();

    private static final String[] ids = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    protected ImageCanvasManager() {
        // Exists only to thwart instantiation.
        EventBus.subscribe(ImageViewCrosshairEvent.class, this);
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

        linkedViews.put(view, new LinkedHashSet<ImageView>());

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


    public void yoke(ImageView view1, ImageView view2) {
        if (!registeredViews.containsKey(view1)) {
            register(view1);
        }

        if (!registeredViews.containsKey(view2)) {
            register(view2);
        }

        if (registeredViews.containsKey(view1)) {
            Set<ImageView> set1 = linkedViews.get(view1);
            Set<ImageView> set2 = linkedViews.get(view2);

            set1.addAll(set2);
            set2.addAll(set1);

            set1.add(view2);
            set2.add(view1);

        }

    }

    public void unyoke(ImageView view1, ImageView view2) {
        Set<ImageView> set1 = linkedViews.get(view1);
        Set<ImageView> set2 = linkedViews.get(view2);

        if (set1 != null && !set1.isEmpty() && set1.contains(view2)) {
            set1.remove(view2);
        } else {
            log.warning("attempting to unyoke but view " + view1 + "is not yoked to " + view2);
        }

        if (set2 != null && !set2.isEmpty() && set2.contains(view1)) {
            set2.remove(view1);
        } else {
            log.warning("attempting to unyoke but view " + view1 + "is not yoked to " + view2);
        }


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
            if (e.isPopupTrigger()) {
                System.out.println("popup menu!");
                System.out.println(e.paramString());
                System.out.println(e.getSource().getClass());
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
                iview.getCrosshair().setLocation(pt);
            }
        }


    }


    public void onEvent(Object evt) {
        ImageViewCrosshairEvent event = (ImageViewCrosshairEvent) evt;
        ImageView source = event.getImageView();

        Set<ImageView> yoked = linkedViews.get(source);

        for (ImageView iview : yoked) {
            if (iview != source) {
                System.out.println("setting location of yoked view : " + iview);
                iview.getCrosshair().setLocation(event.getCrosshair().getLocation());
            }
        }


    }


}
