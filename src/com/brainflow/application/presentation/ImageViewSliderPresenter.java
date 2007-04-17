package com.brainflow.application.presentation;

import com.brainflow.core.ImageView;
import com.brainflow.display.Crosshair;
import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.space.Axis;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 6:49:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewSliderPresenter extends ImageViewPresenter {

    private JSlider slider;
    private ICrosshair crosshair;

    private BeanAdapter crosshairAdapter;
    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter sliderAdapter;
    
    private Axis sliderAxis = Axis.Z_AXIS;
    private Axis displayAxis = null;

    private ValueModel valueLabel;

    private CoordinateToIndexConverter imodel;
    private PropertyConnector labelConnector;

    public ImageViewSliderPresenter(ICrosshair _crosshair) {
        slider = new JSlider();

        crosshair = _crosshair;

        if (crosshair != null) {
            initBinding();
            bindSlider();
        }


    }

    public ImageViewSliderPresenter() {
        slider = new JSlider();

    }

    public void setValueLabel(ValueModel model) {
        valueLabel = model;
    }

    private void resetBinding() {
        if (labelConnector != null) {
            labelConnector.release();
        }

        bindSlider();
    }

    private Axis getDisplayAxis() {
        Anatomy3D displayAnatomy = getSelectedView().getSelectedPlot().getDisplayAnatomy();
        return crosshair.getViewport().getBounds().findAxis(displayAnatomy.ZAXIS);

    }


    private void initBinding() {
        crosshairAdapter = new BeanAdapter(crosshair, true);
        viewportAdapter = new BeanAdapter(crosshair.getViewport(), true);
    }

    private void bindSlider() {

        displayAxis = getDisplayAxis();

        if (sliderAxis == Axis.X_AXIS) {
            imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY),
                    crosshair.getViewport().getBounds().getImageAxis(sliderAxis));
        } else if (sliderAxis == Axis.Y_AXIS) {
            imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY),
                    crosshair.getViewport().getBounds().getImageAxis(sliderAxis));
        } else if (sliderAxis == Axis.Z_AXIS) {
            if (displayAxis == Axis.X_AXIS) {
                imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY),
                        crosshair.getViewport().getBounds().getImageAxis(Axis.X_AXIS));
            } else if (displayAxis == Axis.Y_AXIS) {
                imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY),
                        crosshair.getViewport().getBounds().getImageAxis(Axis.Y_AXIS));
            } else if (displayAxis == Axis.Z_AXIS) {
                imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.Z_VALUE_PROPERTY),
                        crosshair.getViewport().getBounds().getImageAxis(Axis.Z_AXIS));
            } else {
                throw new RuntimeException("Cannot initialize slider for display axis " + displayAxis);

            }
        } else {
            throw new RuntimeException("Cannot initialize slider for " + sliderAxis + " axis");
        }

        sliderAdapter = new BoundedRangeAdapter(imodel, 0, 0,
                crosshair.getViewport().getBounds().getImageAxis(displayAxis).getNumSamples() - 1);


        slider.setModel(sliderAdapter);

        labelConnector = new PropertyConnector(ConverterFactory.createStringConverter(imodel, NumberFormat.getIntegerInstance()),
                "value", valueLabel, "value");
    }


    public void allViewsDeselected() {
        slider.setEnabled(false);
    }


    public void viewSelected(ImageView view) {
        slider.setEnabled(true);
        crosshair = view.getCrosshair().getProperty();

        if (crosshairAdapter == null) {
            initBinding();
            resetBinding();
        } else {
            crosshairAdapter.setBean(crosshair);
            viewportAdapter.setBean(crosshair.getViewport());
            resetBinding();
        }
    }

    public JComponent getComponent() {
        return slider;
    }
}
