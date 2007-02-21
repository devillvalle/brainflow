package com.brainflow.application.presentation;

import com.brainflow.colormap.IColorMap;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.display.Property;
import com.jgoodies.binding.beans.DelayedPropertyChangeHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 10, 2006
 * Time: 8:06:43 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractColorMapPresenter extends ImageViewPresenter {

    private Property<IColorMap> colorMapParameter;
    private PropertyChangeListener substitutionHandler = new ParameterHandler();

    private long delay = 0;

    public AbstractColorMapPresenter() {

    }


    public AbstractColorMapPresenter(Property<IColorMap> _colorMapParameter) {
        colorMapParameter = _colorMapParameter;
        setColorMap(colorMapParameter);

    }

    public abstract void setColorMap(Property<IColorMap> colorMapParameter);


    public void viewSelected(ImageView view) {
        int idx = view.getSelectedIndex();

        if (idx >= 0) {
            updateColorMapParameter(view.getModel().
                    getImageLayer(idx).getImageLayerParameters().
                    getColorMap());
        }


    }


    public void allViewsDeselected() {

    }

    protected Property<IColorMap> getColorMapParameter() {
        return colorMapParameter;
    }


    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    private void updateColorMapParameter(Property<IColorMap> _colorMapParameter) {


        if (colorMapParameter != null) {
            colorMapParameter.removeSubstitutionListener(substitutionHandler);

        }

        colorMapParameter = _colorMapParameter;

        if (delay > 0) {
            substitutionHandler = new DelayedParameterHandler((int) delay);
        } else {
            substitutionHandler = new ParameterHandler();
        }


        colorMapParameter.addSubstitutionListener(substitutionHandler);
        setColorMap(colorMapParameter);


    }


    public void layerSelected(ImageLayer layer) {
        if (layer != null) {
            updateColorMapParameter(layer.getImageLayerParameters().getColorMap());
        }
    }


    class ParameterHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            setColorMap(colorMapParameter);
        }

    }

    class DelayedParameterHandler extends DelayedPropertyChangeHandler {

        public DelayedParameterHandler(int i) {
            super(i);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void delayedPropertyChange(PropertyChangeEvent propertyChangeEvent) {
            setColorMap(colorMapParameter);
        }
    }
}
