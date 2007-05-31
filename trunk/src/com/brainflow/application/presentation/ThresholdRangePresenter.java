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
import com.brainflow.core.AbstractLayer;
import com.brainflow.colormap.LinearColorMap;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

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


    protected void layerSelected(AbstractLayer layer) {
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
        AbstractLayer layer = view.getModel().getLayer(idx);

        Property<ThresholdRange> threshold = layer.getImageLayerProperties().getThresholdRange();

        if (adapter != null) {
            adapter.setBean(threshold.getProperty());

        } else {

            adapter = new BeanAdapter(threshold.getProperty(), true);

            ValueModel highThresh = adapter.getValueModel(ThresholdRange.MAX_PROPERTY);
            ValueModel lowThresh = adapter.getValueModel(ThresholdRange.MIN_PROPERTY);

            Bindings.bind(form.getValueField1(), highThresh);
            Bindings.bind(form.getValueField2(), lowThresh);


            BoundedRangeAdapter lowSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(lowThresh,
                    new ValueHolder(layer.getMinValue()),
                    new ValueHolder(layer.getMaxValue()), 100), 0, 0, 100);

            BoundedRangeAdapter highSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(highThresh,
                    new ValueHolder(layer.getMinValue()),
                    new ValueHolder(layer.getMaxValue()), 100), 0, 0, 100);

            form.getSlider1().setModel(highSliderAdapter);
            form.getSlider2().setModel(lowSliderAdapter);


        }


    }


    public static void main(String[] args) {


    }


}
    
    
    
    

