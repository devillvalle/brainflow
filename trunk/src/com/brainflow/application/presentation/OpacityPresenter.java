/*
 * OpacityPresenter.java
 *
 * Created on July 12, 2006, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.presentation.binding.Bindable;
import com.brainflow.application.presentation.binding.PercentageConverterProperty;
import com.brainflow.application.presentation.forms.OpacityForm;
import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ClipRange;
import com.brainflow.display.Opacity;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;
import java.text.NumberFormat;

import net.java.dev.properties.binding.swing.adapters.SwingBind;


/**
 * @author buchs
 */
public class OpacityPresenter extends ImageViewPresenter implements Bindable {


    private OpacityForm form;

    private BeanAdapter adapter;


    /**
     * Creates a new instance of OpacityPresenter
     */
    public OpacityPresenter() {
        form = new OpacityForm();

        if (getSelectedView() != null) {
            bind();
        }


    }


    public void viewSelected(ImageView view) {
        bind();
        form.setEnabled(true);
    }

    public void allViewsDeselected() {
        form.setEnabled(false);
    }


    protected void layerSelected(AbstractLayer layer) {
        bind();
    }

    public JComponent getComponent() {
        return form;
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
        SwingBind.get().bind(new PercentageConverterProperty(layer.getImageLayerProperties().opacity, 0, 1, 100), form.getOpacitySlider());

    }

    public void unbind() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /*private void initBinding() {
        ImageView view = getSelectedView();
        if (view == null) return;

        int idx = view.getModel().getSelectedIndex();
        AbstractLayer layer = view.getModel().getLayer(idx);
        Opacity opacity = layer.getImageLayerProperties().getOpacity();

        if (adapter == null) {

            adapter = new BeanAdapter(opacity, true);

            BoundedRangeAdapter opacitySliderAdapter = new BoundedRangeAdapter(
                new PercentageConverter(adapter.getValueModel(Opacity.OPACITY_PROPERTY),
                        new ValueHolder(0),
                        new ValueHolder(1), 100), 0, 0, 100);


            form.getOpacitySlider().setModel(opacitySliderAdapter);

            Bindings.bind(form.getValueLabel(),
                ConverterFactory.createStringConverter(adapter.getValueModel(Opacity.OPACITY_PROPERTY),
                        NumberFormat.getInstance()));
        } else {
            adapter.setBean(opacity);
        }
    }   */


}
