package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.DoubleSliderForm;
import com.brainflow.colormap.AbstractColorMap;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.display.Property;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;

import javax.swing.*;

/**
 * @author buchs
 */
public class ColorRangePresenter extends AbstractColorMapPresenter {

    private IColorMap colorMap;
    private BeanAdapter adapter;
    private DoubleSliderForm form;

    private BoundedRangeAdapter lowValueAdapter;
    private BoundedRangeAdapter highValueAdapter;

    /**
     * Creates a new instance of ColorRangePanel
     */
    public ColorRangePresenter(LinearColorMap cmap) {
        colorMap = cmap;
        form = new DoubleSliderForm();

        if (colorMap != null) {
            initBinding();
        }


    }

    public ColorRangePresenter() {
        colorMap = new LinearColorMap(0, 255, ColorTable.GRAYSCALE);
        form = new DoubleSliderForm();
        initBinding();


    }

    public void setColorMap(Property<IColorMap> param) {
        IColorMap colorMap = param.getProperty();

        form.getSlider1().setEnabled(true);
        form.getSlider2().setEnabled(true);


        if (adapter != null) {
            adapter.setBean(colorMap);

        } else {

            initBinding();
        }


    }

    public JComponent getComponent() {
        return form;
    }


    private void initBinding() {
        adapter = new BeanAdapter(colorMap, true);


        lowValueAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(AbstractColorMap.LOW_CLIP_PROPERTY),
                adapter.getValueModel(IColorMap.MINIMUM_VALUE_PROPERTY),
                adapter.getValueModel(IColorMap.MAXIMUM_VALUE_PROPERTY), 100), 0, 0, 100);

        highValueAdapter = new BoundedRangeAdapter(new PercentageConverter(adapter.getValueModel(AbstractColorMap.HIGH_CLIP_PROPERTY),
                adapter.getValueModel(IColorMap.MINIMUM_VALUE_PROPERTY),
                adapter.getValueModel(IColorMap.MAXIMUM_VALUE_PROPERTY), 100), 0, 0, 100);

        form.getSlider1().setModel(highValueAdapter);
        form.getSlider2().setModel(lowValueAdapter);

        form.getSliderLabel1().setText("High: ");
        form.getSliderLabel2().setText("Low: ");

        /*Bindings.bind(form.getValueField1(), ConverterFactory.createStringConverter(
                adapter.getValueModel(LinearColorMap.HIGH_CLIP_PROPERTY), NumberFormat.getInstance()));

        Bindings.bind(form.getValueField2(), ConverterFactory.createStringConverter(
                adapter.getValueModel(LinearColorMap.LOW_CLIP_PROPERTY), NumberFormat.getInstance()));  */

        Bindings.bind(form.getValueField1(), adapter.getValueModel(IColorMap.HIGH_CLIP_PROPERTY));
        Bindings.bind(form.getValueField2(), adapter.getValueModel(IColorMap.LOW_CLIP_PROPERTY));
    }


    public static void main(String[] args) {


    }


}
