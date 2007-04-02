/*
 * ThresholdRangePresenter.java
 *
 * Created on July 11, 2006, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.DoubleSliderForm;
import com.brainflow.display.Property;
import com.brainflow.display.ThresholdRange;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageLayer;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;

/**
 * @author buchs
 */
public class ThresholdRangePresenter extends ImageViewPresenter {


    private DoubleSliderForm form;

    private BeanAdapter adapter;

    /**
     * Creates a new instance of ColorRangePanel
     */
    public ThresholdRangePresenter() {
        form = new DoubleSliderForm();
        form.getSliderLabel1().setText("High: ");
        form.getSliderLabel2().setText("Low: ");

        initBinding();
    }


    public void viewSelected(ImageView view) {
        form.setEnabled(true);
        initBinding();
    }


    protected void layerSelected(ImageLayer layer) {
        initBinding();
    }

    public void allViewsDeselected() {
        form.setEnabled(false);
    }

    public JComponent getComponent() {
        return form;
    }


    private void initBinding() {
        ImageView view = getSelectedView();
        if (view == null) return;

        int idx = view.getModel().getSelectedIndex();
        ImageLayer layer = view.getModel().getImageLayer(idx);
        System.out.println("layer : " + idx);
        Property<ThresholdRange> threshold = layer.getImageLayerProperties().getThresholdRange();

        if (adapter != null) {
            adapter.setBean(threshold.getProperty());

        } else {

            adapter = new BeanAdapter(threshold.getProperty());

            System.out.println("threshold : " + threshold.getProperty());
            Bindings.bind(form.getValueField1(), adapter.getValueModel(ThresholdRange.MAX_PROPERTY));
            Bindings.bind(form.getValueField2(), adapter.getValueModel(ThresholdRange.MIN_PROPERTY));


            BoundedRangeAdapter lowSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(ThresholdRange.MIN_PROPERTY),
                    new ValueHolder(layer.getImageData().getMinValue()),
                    new ValueHolder(layer.getImageData().getMaxValue()), 100), 0, 0, 100);

            BoundedRangeAdapter highSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(ThresholdRange.MAX_PROPERTY),
                    new ValueHolder(layer.getImageData().getMinValue()),
                    new ValueHolder(layer.getImageData().getMaxValue()), 100), 0, 0, 100);

            form.getSlider1().setModel(highSliderAdapter);
            form.getSlider2().setModel(lowSliderAdapter);
        }


    }


    public static void main(String[] args) {


    }


}
    
    
    
    

