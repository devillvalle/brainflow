package com.brainflow.application.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import com.brainflow.gui.BiSlider;
import com.brainflow.gui.NumberRangeModel;
import com.brainflow.core.ClipRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2008
 * Time: 2:20:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class BiSliderAdapter extends SwingAdapter<ClipRange, BiSlider> implements ChangeListener {

    protected void bindListener(BaseProperty<ClipRange> clipRangeBaseProperty, BiSlider component) {
        component.addChangeListener(this);
    }

    protected void unbindListener(BaseProperty<ClipRange> clipRangeBaseProperty, BiSlider component) {
        component.removeChangeListener(this);
    }

    protected void updateUI(ClipRange newValue) {
        if(newValue != null) {

            getComponent().getModel().setRangeProperties(newValue.getLowClip(), newValue.getHighClip(), newValue.minValue.get(), newValue.maxValue.get(), false);

        }

    }



    public void stateChanged(ChangeEvent e) {
        NumberRangeModel model = getComponent().getModel();
        ClipRange crange = new ClipRange(model.getMin(), model.getMax(), model.getLowValue(), model.getHighValue());
        callWhenUIChanged(crange);

    }



    protected Class getType() {

        return ClipRange.class;

    }



    protected Class getComponentType() {

        return BiSlider.class;

    }

}
