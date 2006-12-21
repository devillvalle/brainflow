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
import com.brainflow.display.DisplayParameter;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ConverterFactory;

import javax.swing.*;
import java.text.NumberFormat;

/**
 * @author buchs
 */
public class ThresholdRangePresenter extends AbstractColorMapPresenter {

    private LinearColorMap colorMap;
    private BeanAdapter adapter;
    private DoubleSliderForm form;

    private BoundedRangeAdapter lowSliderAdapter;
    private BoundedRangeAdapter highSliderAdapter;

    /**
     * Creates a new instance of ColorRangePanel
     */
    public ThresholdRangePresenter(LinearColorMap cmap) {
        colorMap = cmap;
        form = new DoubleSliderForm();

        if (colorMap != null) {
            initBinding();
        }


    }

    public ThresholdRangePresenter() {
        colorMap = new LinearColorMap(0, 255, ColorTable.GRAYSCALE);

        form = new DoubleSliderForm();
        initBinding();


    }

    public void setColorMap(DisplayParameter<IColorMap> param) {
        IColorMap _colorMap = param.getParameter();

        if (_colorMap instanceof LinearColorMap) {
            colorMap = (LinearColorMap) _colorMap;
            if (adapter != null) {
                adapter.setBean(colorMap);
            } else {
                initBinding();
            }
        } else {
            //adapter.setBean(null);

        }

    }


    public JComponent getComponent() {
        return form;
    }


    private void initBinding() {
        adapter = new BeanAdapter(colorMap, true);


        lowSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(LinearColorMap.LOWER_ALPHA_PROPERTY),
                adapter.getValueModel(LinearColorMap.MINIMUM_VALUE_PROPERTY),
                adapter.getValueModel(LinearColorMap.MAXIMUM_VALUE_PROPERTY), 100), 0, 0, 100);

        highSliderAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(LinearColorMap.UPPER_ALPHA_PROPERTY),
                adapter.getValueModel(LinearColorMap.MINIMUM_VALUE_PROPERTY),
                adapter.getValueModel(LinearColorMap.MAXIMUM_VALUE_PROPERTY), 100), 0, 0, 100);

        form.getSlider1().setModel(highSliderAdapter);
        form.getSlider2().setModel(lowSliderAdapter);

        form.getSliderLabel1().setText("High: ");
        form.getSliderLabel2().setText("Low: ");

        Bindings.bind(form.getValueLabel1(), ConverterFactory.createStringConverter(
                adapter.getValueModel(LinearColorMap.UPPER_ALPHA_PROPERTY), NumberFormat.getInstance()));

        Bindings.bind(form.getValueLabel2(), ConverterFactory.createStringConverter(
                adapter.getValueModel(LinearColorMap.LOWER_ALPHA_PROPERTY), NumberFormat.getInstance()));

    }


    public static void main(String[] args) {


    }


}
    
    
    
    

