package com.brainflow.application.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.BaseProperty;

import com.jidesoft.swing.CheckBoxList;
import com.brainflow.gui.BiSlider;
import com.brainflow.core.ClipRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 8, 2008
 * Time: 7:02:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExtBind extends SwingBind {

    private static ExtBind instance;

    protected ExtBind() {
        addAdapter(new CheckBoxListAdapter());
        addAdapter(new BiSliderAdapter());
    }

    public void bindCheckedIndices(IndexedProperty<Integer> property, CheckBoxList cmp) {
        new CheckBoxListAdapter().bind(property, cmp);
    }

    public void bindBiSlider(BaseProperty<ClipRange> property, BiSlider cmp) {
        new BiSliderAdapter().bind(property, cmp);
    }

    public static ExtBind get() {
        if (instance == null) {
            instance = new ExtBind();
        }
        
        return instance;
    }

}
