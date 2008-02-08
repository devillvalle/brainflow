/*
 * ThresholdRangePresenter.java
 *
 * Created on July 11, 2006, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.ThresholdRangeForm;
import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageLayer;
import com.brainflow.display.Property;
import com.brainflow.display.ThresholdRange;
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


    private ThresholdRangeForm thresholdForm;

    private BeanAdapter adapter;

    private PercentageConverter lowConverter;

    private PercentageConverter highConverter;


    /**
     * Creates a new instance of ColorRangePanel
     */
    public ThresholdRangePresenter() {
        thresholdForm = new ThresholdRangeForm();
        thresholdForm.getSliderLabel1().setText("High: ");
        thresholdForm.getSliderLabel2().setText("Low: ");


        initBinding();
    }


    public void viewSelected(ImageView view) {
        if (view != null & view.getModel().getNumLayers() > 0) {
            thresholdForm.setEnabled(true);
            initBinding();
        } else {
            thresholdForm.setEnabled(false);

        }
    }

    @Override
    protected void layerSelected(ImageLayer layer) {
        initBinding();
    }

    public void allViewsDeselected() {
        thresholdForm.setEnabled(false);
    }

    public JComponent getComponent() {
        return thresholdForm;
    }


    private void initBinding() {
        ImageView view = getSelectedView();

        if (view == null) return;

        int idx = view.getModel().getSelectedIndex();
        AbstractLayer layer = view.getModel().getLayer(idx);

       ThresholdRange threshold = layer.getImageLayerProperties().getThresholdRange();


        if (adapter != null) {

            lowConverter.setMin(new ValueHolder(layer.getMinValue()));
            lowConverter.setMax(new ValueHolder(layer.getMaxValue()));
            highConverter.setMin(new ValueHolder(layer.getMinValue()));
            highConverter.setMax(new ValueHolder(layer.getMaxValue()));
            adapter.setBean(threshold);


        } else {

            adapter = new BeanAdapter(threshold, true);

            ValueModel highThresh = adapter.getValueModel(ThresholdRange.MAX_PROPERTY);
            ValueModel lowThresh = adapter.getValueModel(ThresholdRange.MIN_PROPERTY);


            Bindings.bind(thresholdForm.getValueField1(), highThresh);
            Bindings.bind(thresholdForm.getValueField2(), lowThresh);

            //todo this is fucked up


            lowConverter = new PercentageConverter(lowThresh,
                    new ValueHolder(layer.getMinValue()),
                    new ValueHolder(layer.getMaxValue()), 100);
            BoundedRangeAdapter lowSliderAdapter = new BoundedRangeAdapter(lowConverter, 0, 0, 100);


            highConverter = new PercentageConverter(highThresh,
                    new ValueHolder(layer.getMinValue()),
                    new ValueHolder(layer.getMaxValue()), 100);

            BoundedRangeAdapter highSliderAdapter = new BoundedRangeAdapter(highConverter, 0, 0, 100);

            thresholdForm.getSlider1().setModel(highSliderAdapter);
            thresholdForm.getSlider2().setModel(lowSliderAdapter);

            ValueModel inclusiveMask = adapter.getValueModel(ThresholdRange.INCLUSIVE_PROPERTY);
            Bindings.bind(thresholdForm.getInclusiveCheckBox(), inclusiveMask);

            ValueModel symValue = adapter.getValueModel(ThresholdRange.SYMMETRICAL_PROPERTY);
            Bindings.bind(thresholdForm.getSymmetricalCheckBox(), symValue);


        }


    }


    public static void main(String[] args) {


    }


}
    
    
    
    

