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
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.colormap.AbstractColorMap;
import com.brainflow.display.Property;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;

import javax.swing.*;
import java.text.NumberFormat;


/**
 * @author buchs
 */
public class OpacityPresenter extends AbstractColorMapPresenter {


    private OpacityForm form;
    private LinearColorMap colorMap;
    private BeanAdapter adapter;

    /**
     * Creates a new instance of OpacityPresenter
     */
    public OpacityPresenter() {
        form = new OpacityForm();
        colorMap = new LinearColorMap(0, 255, ColorTable.GRAYSCALE);
        initBinding();


    }

    public void setColorMap(Property<IColorMap> param) {
        IColorMap _colorMap = param.getProperty();
        if (_colorMap instanceof LinearColorMap) {
            colorMap = (LinearColorMap) _colorMap;
            if (adapter != null) {
                adapter.setBean(colorMap);
            } else {
                initBinding();
            }
        } else {
            colorMap = null;
            //adapter.setBean(null);
        }

    }


    public OpacityPresenter(LinearColorMap _colorMap) {
        form = new OpacityForm();
        colorMap = _colorMap;
        initBinding();
    }

    public void setColorMap(LinearColorMap cmap) {
        colorMap = cmap;
        adapter.setBean(colorMap);
    }

    public JComponent getComponent() {
        return form;
    }

    private void initBinding() {
        adapter = new BeanAdapter(colorMap, true);


        BoundedRangeAdapter opacitySliderAdapter = new BoundedRangeAdapter(
                new PercentageConverter(adapter.getValueModel(AbstractColorMap.ALPHA_MULTIPLIER_PROPERTY),
                new ValueHolder(0),
                new ValueHolder(1), 100), 0, 0, 100);


        form.getOpacitySlider().setModel(opacitySliderAdapter);

        Bindings.bind(form.getValueLabel(),
                ConverterFactory.createStringConverter(adapter.getValueModel(AbstractColorMap.ALPHA_MULTIPLIER_PROPERTY),
                        NumberFormat.getInstance()));
    }


}
