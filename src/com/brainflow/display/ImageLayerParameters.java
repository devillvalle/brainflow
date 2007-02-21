package com.brainflow.display;

import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.DisplayAction;
import com.brainflow.core.DisplayChangeEvent;
import com.brainflow.utils.Range;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.event.ChangeListener;
import java.awt.image.IndexColorModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


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




    private Property<IColorMap> colorMap;
    private Property<InterpolationProperty> resampleInterpolation;
    private Property<InterpolationProperty> screenInterpolation;
    private Property<VisibleProperty> visible;
    private Property<AlphaMaskProperty> alphaMask;
    private Property<ImageOpListProperty> imageOpList;

    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private SelectionInList resampleSelection;


    public ImageLayerParameters() {
        IColorMap imap = new LinearColorMap(0, 255, ColorTable.GRAYSCALE);
        init(imap);

    }

    public ImageLayerParameters(IndexColorModel _icm, Range _dataRange) {
        IColorMap imap = new LinearColorMap(_dataRange.getMin(), _dataRange.getMax(), _icm);
        init(imap);

    }

    public ImageLayerParameters(IColorMap map) {
        init(map);
    }


    private void init(IColorMap map) {

        colorMap = new Property<IColorMap>(map);
        resampleInterpolation = new Property<InterpolationProperty>(new InterpolationProperty(InterpolationHint.CUBIC));
        screenInterpolation = new Property<InterpolationProperty>(new InterpolationProperty(InterpolationHint.CUBIC));
        visible = new Property<VisibleProperty>(new VisibleProperty(this, true));
        alphaMask = new Property<AlphaMaskProperty>(new AlphaMaskProperty());
        imageOpList = new Property<ImageOpListProperty>(new ImageOpListProperty());

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
        imageOpList.getProperty().addImageOp(filterName, op);

    }


    public SelectionInList getResampleSelection() {
        return resampleSelection;
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);

    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }


    private void fireChangeEvent(DisplayChangeEvent event) {
        for (ChangeListener listener : listeners) {
            listener.stateChanged(event);
        }
    }

    public Property<VisibleProperty> getVisiblility() {
        return visible;
    }

    public Property<IColorMap> getColorMap() {
        return colorMap;
    }

    public Property<InterpolationProperty> getScreenInterpolation() {
        return screenInterpolation;
    }

    public Property<InterpolationProperty> getResampleInterpolation() {
        return resampleInterpolation;
    }

    public Property<ImageOpListProperty> getImageOpList() {
        return imageOpList;
    }

    public Property<AlphaMaskProperty> getAlphaMask() {
        return alphaMask;
    }


    public void setColorMap(IColorMap imap) {
        colorMap.setProperty(imap);
    }

    public void setScreenInterpolation(InterpolationProperty interp) {
        screenInterpolation.setProperty(interp);
    }

    public void setResampleInterpolation(InterpolationProperty interp) {
        resampleInterpolation.setProperty(interp);
    }


}