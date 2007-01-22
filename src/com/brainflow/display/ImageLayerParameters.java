package com.brainflow.display;

import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.colormap.ColorTable;
import com.brainflow.core.DisplayAction;
import com.brainflow.core.DisplayChangeEvent;
import com.brainflow.core.ImageLayer;
import com.brainflow.utils.Range;
import com.jgoodies.binding.list.SelectionInList;


import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.image.IndexColorModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ImageLayerParameters implements Serializable {

    public static final String COLOR_MAP_PARAMETER = "colormap";
    public static final String SCREEN_INTERPOLATION_PARAMETER = "screenInterpolation";
    public static final String RESAMPLE_INTERPOLATION_PARAMETER = "resampleInterpolation";


    private ImageLayer layer;

    private DisplayParameter<IColorMap> colorMap;
    private DisplayParameter<InterpolationProperty> resampleInterpolation;
    private DisplayParameter<InterpolationProperty> screenInterpolation;
    private DisplayParameter<VisibleProperty> visible;
    private DisplayParameter<AlphaMaskProperty> alphaMask;
    private DisplayParameter<ImageOpListProperty> imageOpList;

    private EventListenerList listeners = new EventListenerList();

    private SelectionInList resampleSelection;



     public ImageLayerParameters(ImageLayer _layer) {
        layer = _layer;
        IColorMap imap = new LinearColorMap(0, 255, ColorTable.GRAYSCALE);
        init(imap);

    }

    public ImageLayerParameters(ImageLayer _layer, IndexColorModel _icm, Range _dataRange) {
        layer = _layer;
        IColorMap imap = new LinearColorMap(_dataRange.getMin(), _dataRange.getMax(), _icm);
        init(imap);

    }




    public ImageLayerParameters(ImageLayer _layer, IColorMap map) {
        layer = _layer;
        init(map);

    }


    public ImageLayer getLayer() {
        return layer;
    }

    private void init(IColorMap map) {

        colorMap = new DisplayParameter<IColorMap>(map);
        resampleInterpolation = new DisplayParameter<InterpolationProperty>(new InterpolationProperty(InterpolationHint.NEAREST_NEIGHBOR));
        screenInterpolation = new DisplayParameter<InterpolationProperty>(new InterpolationProperty(InterpolationHint.NEAREST_NEIGHBOR));
        visible = new DisplayParameter<VisibleProperty>(new VisibleProperty(layer, true));
        alphaMask = new DisplayParameter<AlphaMaskProperty>(new AlphaMaskProperty());
        imageOpList = new DisplayParameter<ImageOpListProperty>(new ImageOpListProperty());

        resampleSelection = new SelectionInList(InterpolationHint.values(), resampleInterpolation.getModel(InterpolationProperty.INTERPOLATION_PROPERTY));


        colorMap.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(colorMap, DisplayAction.COLOR_MAP_CHANGED));
            }
        });

        visible.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(visible, DisplayAction.RECOMPOSE));
            }
        });

        resampleInterpolation.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(resampleInterpolation, DisplayAction.INTERPOLATION_CHANGED));
            }
        });

        screenInterpolation.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(screenInterpolation, DisplayAction.REFIT));
            }
        });

        alphaMask.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(alphaMask, DisplayAction.COLOR_MAP_CHANGED));
            }
        });

        imageOpList.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(imageOpList, DisplayAction.FILTER_LAYER_CHANGED));
            }
        });

        visible.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fireChangeEvent(new DisplayChangeEvent(visible, DisplayAction.RECOMPOSE));
            }
        });


    }

    public void addImageOp(String filterName, ImageOp op) {
        imageOpList.getParameter().addImageOp(filterName, op);

    }


    public SelectionInList getResampleSelection() {
        return resampleSelection;
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }


    private void fireChangeEvent(DisplayChangeEvent event) {
        ChangeListener[] list = listeners.getListeners(ChangeListener.class);
        for (ChangeListener listener : list) {
            listener.stateChanged(event);
        }
    }

    public DisplayParameter<VisibleProperty> getVisiblility() {
        return visible;
    }

    public DisplayParameter<IColorMap> getColorMap() {
        return colorMap;
    }

    public DisplayParameter<InterpolationProperty> getScreenInterpolation() {
        return screenInterpolation;
    }

    public DisplayParameter<InterpolationProperty> getResampleInterpolation() {
        return resampleInterpolation;
    }

    public DisplayParameter<ImageOpListProperty> getImageOpList() {
        return imageOpList;
    }

    public DisplayParameter<AlphaMaskProperty> getAlphaMask() {
        return alphaMask;
    }


    public void setColorMap(IColorMap imap) {
        colorMap.setParameter(imap);
    }

    public void setScreenInterpolation(InterpolationProperty interp) {
        screenInterpolation.setParameter(interp);
    }

    public void setResampleInterpolation(InterpolationProperty interp) {
        resampleInterpolation.setParameter(interp);
    }


}