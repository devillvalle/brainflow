package com.brainflow.application.presentation;

import com.brainflow.application.presentation.binding.Bindable;
import com.brainflow.application.presentation.binding.PercentageConverterProperty;
import com.brainflow.application.presentation.forms.DoubleSliderForm;
import com.brainflow.colormap.AbstractColorMap;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMapDeprecated;
import com.brainflow.core.ClipRange;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.core.AbstractLayer;
import com.brainflow.display.Property;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;

/**
 * @author buchs
 */
public class ColorRangePresenter extends ImageViewPresenter implements Bindable {

    private IColorMap colorMap;

    private BeanAdapter adapter;
    
    private DoubleSliderForm form;

    private BoundedRangeAdapter lowValueAdapter;

    private BoundedRangeAdapter highValueAdapter;

    /**
     * Creates a new instance of ColorRangePanel
     */
    public ColorRangePresenter() {

        form = new DoubleSliderForm();
        initGUI();

        if (getSelectedView() != null) {
            bind();
        }




    }



    
    private void initGUI() {
        form.getSliderLabel1().setText("High: ");
        form.getSliderLabel2().setText("Low: ");
    }

    public JComponent getComponent() {
        return form;
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void viewSelected(ImageView view) {
        bind();
    }

    protected void layerSelected(AbstractLayer layer) {
        bind();
    }

    public void unbind() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
        ClipRange clip = layer.getImageLayerProperties().getClipRange();

        
        SwingBind.get().bind(new PercentageConverterProperty(clip.highClip, clip.minValue.get(), clip.maxValue.get(), 100), form.getSlider1());
        SwingBind.get().bind(new PercentageConverterProperty(clip.lowClip, clip.minValue.get(), clip.maxValue.get(), 100), form.getSlider2());
        
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



        /*Bindings.bind(form.getValueField1(), ConverterFactory.createStringConverter(
                adapter.getValueModel(LinearColorMapDeprecated.HIGH_CLIP_PROPERTY), NumberFormat.getInstance()));

        Bindings.bind(form.getValueField2(), ConverterFactory.createStringConverter(
                adapter.getValueModel(LinearColorMapDeprecated.LOW_CLIP_PROPERTY), NumberFormat.getInstance()));  */

        Bindings.bind(form.getValueField1(), adapter.getValueModel(IColorMap.HIGH_CLIP_PROPERTY));
        Bindings.bind(form.getValueField2(), adapter.getValueModel(IColorMap.LOW_CLIP_PROPERTY));
    }


    public static void main(String[] args) {


    }


}
