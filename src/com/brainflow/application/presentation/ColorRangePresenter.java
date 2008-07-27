package com.brainflow.application.presentation;

import com.brainflow.application.presentation.binding.Bindable;
import com.brainflow.application.presentation.binding.PercentageRangeConverter;
import com.brainflow.application.presentation.binding.DoubleToStringConverter;
import com.brainflow.application.presentation.controls.DoubleSliderForm;

import com.brainflow.core.ClipRange;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.ImageView;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;

/**
 * @author buchs
 */
public class ColorRangePresenter extends ImageViewPresenter implements Bindable {

  
    
    private DoubleSliderForm form;


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

    @Override
    protected void layerSelected(ImageLayer layer) {
        bind();
    }

    public void unbind() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
        ClipRange clip = layer.getImageLayerProperties().getClipRange();

        
        SwingBind.get().bind(new PercentageRangeConverter(clip.highClip, clip.minValue.get(), clip.maxValue.get(), 100), form.getSlider1());
        SwingBind.get().bind(new PercentageRangeConverter(clip.lowClip, clip.minValue.get(), clip.maxValue.get(), 100), form.getSlider2());
        SwingBind.get().bind(new DoubleToStringConverter(clip.highClip), form.getValueField1());
        SwingBind.get().bind(new DoubleToStringConverter(clip.lowClip), form.getValueField2());
        
    }



    public static void main(String[] args) {


    }


}
