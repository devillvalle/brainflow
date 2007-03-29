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
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.display.Property;
import com.brainflow.display.Opacity;
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

    private BoundedRangeAdapter lowSliderAdapter;
    private BoundedRangeAdapter highSliderAdapter;

    /**
     * Creates a new instance of ColorRangePanel
     */
    public ThresholdRangePresenter() {
        form = new DoubleSliderForm();
        initBinding();
    }


    public void viewSelected(ImageView view) {
        form.setEnabled(true);
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
        Property<ThresholdRange> threshold = layer.getImageLayerParameters().getThresholdRange();


        lowSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(threshold.getModel(ThresholdRange.MIN_PROPERTY),
                new ValueHolder(layer.getImageData().getMinValue()),
                new ValueHolder(layer.getImageData().getMaxValue()), 100), 0, 0, 100);

        highSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(threshold.getModel(ThresholdRange.MAX_PROPERTY),
                new ValueHolder(layer.getImageData().getMinValue()),
                new ValueHolder(layer.getImageData().getMaxValue()), 100), 0, 0, 100);

        form.getSlider1().setModel(highSliderAdapter);
        form.getSlider2().setModel(lowSliderAdapter);

        form.getSliderLabel1().setText("High: ");
        form.getSliderLabel2().setText("Low: ");

     
        Bindings.bind(form.getValueField1(), threshold.getModel(ThresholdRange.MAX_PROPERTY));
        Bindings.bind(form.getValueField2(), threshold.getModel(ThresholdRange.MIN_PROPERTY));


    }


    public static void main(String[] args) {


    }


}
    
    
    
    

