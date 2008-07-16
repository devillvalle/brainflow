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
import com.brainflow.application.presentation.binding.PercentageRangeConverter;
import com.brainflow.application.presentation.binding.DoubleToStringConverter;
import com.brainflow.application.presentation.binding.ExtBind;

import com.brainflow.core.ImageView;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.ClipRange;
import com.brainflow.gui.BiSlider;
import com.brainflow.gui.NumberRangeModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

import java.awt.*;


/**
 * @author buchs
 */
public class ThresholdRangePresenter2 extends ImageViewPresenter {


    private JPanel form;

    private BiSlider bislider;


    /**
     * Creates a new instance of ColorRangePanel
     */
    public ThresholdRangePresenter2() {
        form = new JPanel();
        initGUI();

        if (getSelectedView() != null) {
            bind();
        }


    }

    private void initGUI() {

        bislider = new BiSlider(new NumberRangeModel(0, 100, 0, 100));
        form.setLayout(new BorderLayout());
        form.setBorder(new EmptyBorder(5, 1, 5, 1));
        form.add(bislider, BorderLayout.CENTER);

    }


    public void viewSelected(ImageView view) {
        bind();
    }

    @Override
    protected void layerSelected(ImageLayer layer) {
        bind();
    }


    public void allViewsDeselected() {
        unbind();
    }

    public JComponent getComponent() {
        return form;
    }

    public void unbind() {
        ExtBind.get().unbind(bislider);
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();

        ExtBind.get().bindBiSlider(layer.getImageLayerProperties().thresholdRange, bislider);


    }


    public static void main(String[] args) {


    }


}