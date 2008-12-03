package com.brainflow.application.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.RProperty;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import com.brainflow.gui.BiSlider;
import com.brainflow.gui.NumberRangeModel;
import com.brainflow.core.ClipRange;
import com.brainflow.core.IClipRange;
import com.brainflow.utils.NumberUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2008
 * Time: 2:20:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class BiSliderAdapter extends SwingAdapter<IClipRange, BiSlider> implements ChangeListener {

    protected void bindListener(BaseProperty<IClipRange> clipRangeBaseProperty, BiSlider component) {
        component.addChangeListener(this);
    }

    protected void unbindListener(BaseProperty<IClipRange> clipRangeBaseProperty, BiSlider component) {
        component.removeChangeListener(this);
    }

    protected void updateUI(IClipRange newValue) {
        //RProperty<IClipRange> prop = (RProperty<IClipRange>)getProperty();
        if (newValue != null) {
            System.out.println("updating slider");
            System.out.println("slider low  " + newValue.getLowClip());
            System.out.println("slider high  " + newValue.getHighClip());
            getComponent().getModel().setRangeProperties(newValue.getLowClip(), newValue.getHighClip(), newValue.getMin(), newValue.getMax(), false);

        }

    }


    public void stateChanged(ChangeEvent e) {
        NumberRangeModel model = getComponent().getModel();
        RProperty<IClipRange> prop = (RProperty<IClipRange>) getProperty();
        IClipRange clip = prop.get();
        IClipRange crange = clip.newClipRange(model.getMin(), model.getMax(), model.getLowValue(), model.getHighValue());
        System.out.println("updating clip");
        System.out.println("clip low  " + crange.getLowClip());
        System.out.println("clip high  " + crange.getHighClip());
        callWhenUIChanged(crange);

        if (!NumberUtils.equals(crange.getLowClip(), model.getLowValue(), .0001) || !NumberUtils.equals(crange.getHighClip(), model.getHighValue(), .0001)) {
            System.out.println("clip changed ... update?");
            updateUI(crange);
        }

    }


    protected Class getType() {

        return IClipRange.class;

    }


    protected Class getComponentType() {

        return BiSlider.class;

    }

}
