package com.brainflow.application.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.util.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.CheckBoxListSelectionModel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 8, 2008
 * Time: 7:05:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxListAdapter extends SwingAdapter<List<Integer>, CheckBoxList> implements ListSelectionListener {



    protected void bindListener(BaseProperty<List<Integer>> objectBaseProperty, CheckBoxList component) {

        component.getCheckBoxListSelectionModel().addListSelectionListener(this);
    }

    protected void unbindListener(BaseProperty<List<Integer>> objectBaseProperty, CheckBoxList component) {
        component.getCheckBoxListSelectionModel().removeListSelectionListener(this);
    }

    protected Class getType() {
        return List.class;
    }

    protected Class getComponentType() {
        return CheckBoxList.class;
    }

    protected void updateUI(List<Integer> newValue) {
         getComponent().setCheckBoxListSelectedIndices((int[]) Utils.asArray((IndexedProperty<Integer>) getProperty(), Integer.TYPE));

    }

    public void valueChanged(ListSelectionEvent e) {
       

        callWhenUIChanged((List<Integer>) Utils.addToCollection(getComponent().getCheckBoxListSelectedIndices(),
                new ArrayList<Integer>()));

    }
}
