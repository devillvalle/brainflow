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
import com.brainflow.application.presentation.binding.PercentageRangeConverter;
import com.brainflow.application.presentation.forms.RenderingParamsForm;
import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageLayer;
import com.jgoodies.binding.beans.BeanAdapter;

import javax.swing.*;

import net.java.dev.properties.binding.swing.adapters.SwingBind;


/**
 * @author buchs
 */
public class RenderingParamsPresenter extends ImageViewPresenter implements Bindable {


    private RenderingParamsForm form;

    private BeanAdapter opacityAdapter;

    private BeanAdapter smoothingAdapter;

    /**
     * Creates a new instance of OpacityPresenter
     */
    public RenderingParamsPresenter() {
        form = new RenderingParamsForm();
        if (getSelectedView() != null) {
            bind();
        }


    }


    public void viewSelected(ImageView view) {
        bind();
        form.getInterpolationLabel().setEnabled(true);
        form.getInterpolationChoices().setEnabled(true);
        int idx = view.getSelectedLayerIndex();

        //if (idx >= 0) {
        //    SelectionInList sel = view.getModel().
        //            getLayer(idx).getImageLayerProperties().getInterpolationMethod();
        //
        //
        //    Bindings.bind(form.getInterpolationChoices(), sel);
        //}
    }

    public void allViewsDeselected() {
        form.setEnabled(false);
        form.getInterpolationChoices().setEnabled(false);
        form.getInterpolationLabel().setEnabled(false);
    }


    protected void layerSelected(AbstractLayer layer) {
        bind();
        //SelectionInList sel = layer.getImageLayerProperties().getInterpolationMethod();
        //Bindings.bind(form.getInterpolationChoices(), sel);
    }

    public JComponent getComponent() {
        return form;
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
        SwingBind.get().bind(new PercentageRangeConverter(layer.getImageLayerProperties().opacity, 0, 1, 100), form.getOpacitySlider());

    }

    public void unbind() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /*private void initBinding() {
        ImageView view = getSelectedView();
        if (view == null) return;

        int idx = view.getModel().getSelectedIndex();
        AbstractLayer layer = view.getModel().getLayer(idx);
        float opacity = layer.getImageLayerProperties().getOpacity();
        SmoothingRadius radius = layer.getImageLayerProperties().getSmoothingRadius();

        if (opacityAdapter == null) {

            opacityAdapter = new BeanAdapter(opacity, true);

            BoundedRangeAdapter opacitySliderAdapter = new BoundedRangeAdapter(
                    new PercentageConverter(opacityAdapter.getValueModel(Opacity.OPACITY_PROPERTY),
                            new ValueHolder(0),
                            new ValueHolder(1), 100), 0, 0, 100);


            form.getOpacitySlider().setModel(opacitySliderAdapter);

            Bindings.bind(form.getOpacityValueLabel(),
                    ConverterFactory.createStringConverter(opacityAdapter.getValueModel(Opacity.OPACITY_PROPERTY),
                            NumberFormat.getInstance()));


            smoothingAdapter = new BeanAdapter(radius, true);
            BoundedRangeAdapter smoothingSliderAdapter = new BoundedRangeAdapter(smoothingAdapter.getValueModel(SmoothingRadius.RADIUS_PROPERTY), 0, 0, 15);
            form.getSmoothingSlider().setModel(smoothingSliderAdapter);

            Bindings.bind(form.getSmoothingValueLabel(),
                    ConverterFactory.createStringConverter(smoothingAdapter.getValueModel(SmoothingRadius.RADIUS_PROPERTY),
                            NumberFormat.getInstance()));


        } else {
            opacityAdapter.setBean(opacity);
            smoothingAdapter.setBean(radius);
        }
    } */


}