package com.brainflow.display;

import com.brainflow.core.DisplayAction;
import com.brainflow.core.DisplayChangeEvent;
import com.brainflow.core.IImageDisplayModel;

import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 15, 2006
 * Time: 5:16:35 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageDisplayParameters {

    private Map<String, DisplayParameter> parameters = new HashMap<String, DisplayParameter>();
    private EventListenerList listeners = new EventListenerList();

    private IImageDisplayModel displayModel;

    public static final String CROSSHAIR_PARAMETER = "crosshair";
    public static final String VIEWPORT_PARAMETER = "viewport";

    private DisplayParameter<Crosshair> crosshair;
    private DisplayParameter<Viewport3D> viewport;


    public ImageDisplayParameters(IImageDisplayModel model) {
        displayModel = model;

        viewport = new DisplayParameter<Viewport3D>(new Viewport3D(model.getCompositeImageSpace()));
        crosshair = new DisplayParameter<Crosshair>(new Crosshair(viewport.getParameter()));


        crosshair.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(crosshair, DisplayAction.SLICE_CHANGED));
            }
        });

        viewport.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(viewport, DisplayAction.VIEWPORT_CHANGED));
            }
        });

    }


    public void addChangeListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }

    public DisplayParameter<Crosshair> getCrosshair() {
        return crosshair;
    }

    public DisplayParameter<Viewport3D> getViewport() {
        return viewport;
    }

    private void fireChangeEvent(DisplayChangeEvent e) {
        ChangeListener[] list = listeners.getListeners(ChangeListener.class);
        for (ChangeListener listener : list) {
            listener.stateChanged(e);
        }
    }


}
