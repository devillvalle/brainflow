package com.brainflow.application.presentation;

import com.brainflow.core.ImageView;
import com.brainflow.display.Crosshair;
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
    private Crosshair crosshair;

    private BeanAdapter crosshairAdapter;
    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter sliderAdapter;
    private Axis sliderAxis = Axis.Z_AXIS;

    private ValueModel valueLabel;

    public ImageViewSliderPresenter(Crosshair _crosshair) {
        slider = new JSlider();

        crosshair = _crosshair;

        if (crosshair != null)
            initBinding();


    }

    public ImageViewSliderPresenter() {
        slider = new JSlider();

    }

    public void setValueLabel(ValueModel model) {
        valueLabel = model;
    }


    private void initBinding() {
        crosshairAdapter = new BeanAdapter(crosshair, true);
        viewportAdapter = new BeanAdapter(crosshair.getViewport(), true);

        ValueModel imodel = null;
        if (sliderAxis == Axis.X_AXIS) {
            imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.X_VALUE_PROPERTY),
                    crosshair.getViewport().getBounds().getImageAxis(sliderAxis));
        } else if (sliderAxis == Axis.Y_AXIS) {
            imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.Y_VALUE_PROPERTY),
                    crosshair.getViewport().getBounds().getImageAxis(sliderAxis));
        } else if (sliderAxis == Axis.Z_AXIS) {
            imodel = new CoordinateToIndexConverter(crosshairAdapter.getValueModel(Crosshair.Z_VALUE_PROPERTY),
                    crosshair.getViewport().getBounds().getImageAxis(sliderAxis));
        } else {
            throw new RuntimeException("Cannot initialize slider for " + sliderAxis + " axis");
        }

        sliderAdapter = new BoundedRangeAdapter(imodel, 0, 0,
                crosshair.getViewport().getBounds().getImageAxis(sliderAxis).getNumSamples() - 1);

        slider.setModel(sliderAdapter);

        if (valueLabel != null) {
            PropertyConnector.connect(ConverterFactory.createStringConverter(imodel, NumberFormat.getIntegerInstance()),
                    "value", valueLabel, "value");
        }

        //Bindings.bind(valueLabel, ConverterFactory.createStringConverter(imodel, NumberFormat.getIntegerInstance()));


    }


    public void viewSelected(ImageView view) {
        crosshair = view.getCrosshair();

        if (crosshairAdapter == null) {
            initBinding();
        } else {
            crosshairAdapter.setBean(crosshair);
            viewportAdapter.setBean(crosshair.getViewport());
        }
    }

    public JComponent getComponent() {
        return slider;
    }
}
