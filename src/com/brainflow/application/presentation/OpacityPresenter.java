/*
 * OpacityPresenter.java
 *
 * Created on July 12, 2006, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.OpacityForm;
import com.brainflow.display.Property;
import com.brainflow.display.Opacity;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.AbstractLayer;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;
import java.text.NumberFormat;


/**
 * @author buchs
 */
public class OpacityPresenter extends ImageViewPresenter {


    private OpacityForm form;

   
    /**
     * Creates a new instance of OpacityPresenter
     */
    public OpacityPresenter() {
        form = new OpacityForm();
        initBinding();


    }


    public void viewSelected(ImageView view) {
        initBinding();
        form.setEnabled(true);
    }

    public void allViewsDeselected() {
        form.setEnabled(false);
    }


    protected void layerSelected(AbstractLayer layer) {
        initBinding();
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


        BoundedRangeAdapter opacitySliderAdapter = new BoundedRangeAdapter(
                new PercentageConverter(opacity.getModel(Opacity.OPACITY_PROPERTY),
                        new ValueHolder(0),
                        new ValueHolder(1), 100), 0, 0, 100);


        form.getOpacitySlider().setModel(opacitySliderAdapter);

        Bindings.bind(form.getValueLabel(),
                ConverterFactory.createStringConverter(opacity.getModel(Opacity.OPACITY_PROPERTY),
                        NumberFormat.getInstance()));
    }


}
