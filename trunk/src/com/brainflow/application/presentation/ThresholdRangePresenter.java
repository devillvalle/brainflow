/*
 * ThresholdRangePresenter.java
 *
 * Created on July 11, 2006, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.presentation.controls.ThresholdRangeForm;
import com.brainflow.application.presentation.binding.PercentageRangeConverter;
import com.brainflow.application.presentation.binding.DoubleToStringConverter;

import com.brainflow.core.ImageView;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.ClipRange;

import javax.swing.*;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

/**
 * @author buchs
 */
public class ThresholdRangePresenter extends ImageViewPresenter {


    private ThresholdRangeForm form;




    /**
     * Creates a new instance of ColorRangePanel
     */
    public ThresholdRangePresenter() {
        form = new ThresholdRangeForm();
        form.getSliderLabel1().setText("High: ");
        form.getSliderLabel2().setText("Low: ");

        if (getSelectedView() != null) {
            bind();
        }


    }


    public void viewSelected(ImageView view) {
       bind();
    }

    @Override
    protected void layerSelected(ImageLayer layer) {
        bind();
    }

    public void allViewsDeselected() {
        //form.setEnabled(false);
    }

    public JComponent getComponent() {
        return form;
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
        ClipRange clip = layer.getImageLayerProperties().thresholdRange.get();


        SwingBind.get().bind(new PercentageRangeConverter(clip.highClip, clip.minValue.get(), clip.maxValue.get(), 100), form.getSlider1());
        SwingBind.get().bind(new PercentageRangeConverter(clip.lowClip, clip.minValue.get(), clip.maxValue.get(), 100), form.getSlider2());
        SwingBind.get().bind(new DoubleToStringConverter(clip.highClip), form.getValueField1());
        SwingBind.get().bind(new DoubleToStringConverter(clip.lowClip), form.getValueField2());

       

    }







    public static void main(String[] args) {


    }


}
    
    
    
    

