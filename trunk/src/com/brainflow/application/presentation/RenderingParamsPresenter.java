/*
 * OpacityPresenter.java
 *
 * Created on July 12, 2006, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.RenderingParamsForm;
import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageView;
import com.brainflow.display.Opacity;
import com.brainflow.display.Property;
import com.brainflow.display.SmoothingRadius;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;
import java.text.NumberFormat;


/**
 * @author buchs
 */
public class RenderingParamsPresenter extends ImageViewPresenter {


    private RenderingParamsForm form;

    private BeanAdapter opacityAdapter;

    private BeanAdapter smoothingAdapter;

    /**
     * Creates a new instance of OpacityPresenter
     */
    public RenderingParamsPresenter() {
        form = new RenderingParamsForm();
        initBinding();


    }


    public void viewSelected(ImageView view) {
        initBinding();
        form.getInterpolationLabel().setEnabled(true);
        form.getInterpolationChoices().setEnabled(true);
        int idx = view.getSelectedIndex();

        if (idx >= 0) {
            SelectionInList sel = view.getModel().
                    getLayer(idx).getImageLayerProperties().getInterpolationMethod();


            Bindings.bind(form.getInterpolationChoices(), sel);
        }
    }

    public void allViewsDeselected() {
        form.setEnabled(false);
        form.getInterpolationChoices().setEnabled(false);
        form.getInterpolationLabel().setEnabled(false);
    }


    protected void layerSelected(AbstractLayer layer) {
        initBinding();
        SelectionInList sel = layer.getImageLayerProperties().getInterpolationMethod();
        Bindings.bind(form.getInterpolationChoices(), sel);
    }

    public JComponent getComponent() {
        return form;
    }

    private void initBinding() {
        ImageView view = getSelectedView();
        if (view == null) return;

        int idx = view.getModel().getSelectedIndex();
        AbstractLayer layer = view.getModel().getLayer(idx);
        Property<Opacity> opacity = layer.getImageLayerProperties().getOpacity();
        Property<SmoothingRadius> radius = layer.getImageLayerProperties().getSmoothing();

        if (opacityAdapter == null) {

            opacityAdapter = new BeanAdapter(opacity.getProperty(), true);

            BoundedRangeAdapter opacitySliderAdapter = new BoundedRangeAdapter(
                    new PercentageConverter(opacityAdapter.getValueModel(Opacity.OPACITY_PROPERTY),
                            new ValueHolder(0),
                            new ValueHolder(1), 100), 0, 0, 100);


            form.getOpacitySlider().setModel(opacitySliderAdapter);

            Bindings.bind(form.getOpacityValueLabel(),
                    ConverterFactory.createStringConverter(opacityAdapter.getValueModel(Opacity.OPACITY_PROPERTY),
                            NumberFormat.getInstance()));


            smoothingAdapter = new BeanAdapter(radius.getProperty(), true);
            BoundedRangeAdapter smoothingSliderAdapter = new BoundedRangeAdapter(smoothingAdapter.getValueModel(SmoothingRadius.RADIUS_PROPERTY), 0, 0, 15);
            form.getSmoothingSlider().setModel(smoothingSliderAdapter);

            Bindings.bind(form.getSmoothingValueLabel(),
                    ConverterFactory.createStringConverter(smoothingAdapter.getValueModel(SmoothingRadius.RADIUS_PROPERTY),
                            NumberFormat.getInstance()));


        } else {
            opacityAdapter.setBean(opacity.getProperty());
            smoothingAdapter.setBean(radius.getProperty());
        }
    }


}