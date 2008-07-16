package com.brainflow.application.presentation;

import com.brainflow.application.presentation.binding.Bindable;
import com.brainflow.application.presentation.binding.PercentageRangeConverter;
import com.brainflow.application.presentation.binding.DoubleToStringConverter;
import com.brainflow.application.presentation.binding.ExtBind;
import com.brainflow.application.presentation.forms.DoubleSliderForm;

import com.brainflow.core.ClipRange;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.gui.BiSlider;
import com.brainflow.gui.NumberRangeModel;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author buchs
 */
public class ColorRangePresenter2 extends ImageViewPresenter implements Bindable {



    private JPanel form;

    private BiSlider bislider;


    /**
     * Creates a new instance of ColorRangePanel
     */
    public ColorRangePresenter2() {

        form = new JPanel();
        initGUI();

        if (getSelectedView() != null) {
            bind();
        }

    }




    private void initGUI() {

        bislider = new BiSlider(new NumberRangeModel(0,100,0,100));
        form.setLayout(new BorderLayout());
        form.setBorder(new EmptyBorder(5,1,5,1));
        form.add(bislider, BorderLayout.CENTER);

    }

    public JComponent getComponent() {
        return form;
    }

    public void allViewsDeselected() {
        unbind();
    }

    public void viewSelected(ImageView view) {
        bind();
    }

    @Override
    protected void layerSelected(ImageLayer layer) {
        bind();
    }

    public void unbind() {
        ExtBind.get().unbind(bislider);
    }

    public void bind() {
        ImageLayer layer = getSelectedView().getModel().getSelectedLayer();

        ExtBind.get().bindBiSlider(layer.getImageLayerProperties().clipRange, bislider);

       

    }



    public static void main(String[] args) {


    }


}