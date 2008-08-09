package com.brainflow.application.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.binding.swing.adapters.ComboAndListModel;
import net.java.dev.properties.binding.Adapter;

import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.BaseProperty;

import com.jidesoft.swing.CheckBoxList;
import com.brainflow.gui.BiSlider;
import com.brainflow.gui.ToggleBar;
import com.brainflow.core.ClipRange;

import javax.swing.*;

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
        addAdapter(new MyListIndexAdapterX());
        addAdapter(new CheckBoxListAdapter());
        addAdapter(new BiSliderAdapter());
        addAdapter(new ToggleBarAdapter());

    }

    public void bindCheckedIndices(IndexedProperty<Integer> property, CheckBoxList cmp) {
        new CheckBoxListAdapter().bind(property, cmp);
    }

    public void bindBiSlider(BaseProperty<ClipRange> property, BiSlider cmp) {
        new BiSliderAdapter().bind(property, cmp);
    }

    public void bindToggleBar(BaseProperty<Integer> property, ToggleBar cmp) {
        new ToggleBarAdapter().bind(property, cmp);
    }

    public void bindSelectionIndex(BaseProperty<Integer> property, CheckBoxList cmp) {
        new MyListIndexAdapterX().bind(property, cmp);

    }

    public static void unbindComponent(JComponent c) {

        Adapter a = (Adapter) c.getClientProperty("Adapter");

        if (a != null) {

            a.unbind(c);

        }

        a = (Adapter) c.getClientProperty("SelectionAdapter");

        if (a != null) {

            a.unbind(c);

        }

        a = (Adapter) c.getClientProperty("SelectionAdapter2");

        if (a != null) {

            a.unbind(c);

        }

    }

    public void unbind(JComponent cmp) {
        ExtBind.unbindComponent(cmp);
    }


    public void bindContent(IndexedProperty<?> property, ToggleBar cmp) {
        cmp.putClientProperty("Property", property);
        cmp.setModel(new ComboAndListModel(property));
    }

    public static ExtBind get() {
        if (instance == null) {
            instance = new ExtBind();
        }

        return instance;
    }

}
