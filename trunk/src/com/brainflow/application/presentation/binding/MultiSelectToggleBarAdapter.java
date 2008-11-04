package com.brainflow.application.presentation.binding;

import com.jidesoft.swing.CheckBoxList;
import com.brainflow.gui.MultiSelectToggleBar;
import net.java.dev.properties.binding.swing.adapters.SwingAdapter;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.util.Utils;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 25, 2008
 * Time: 5:50:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiSelectToggleBarAdapter extends SwingAdapter<List<Integer>, MultiSelectToggleBar> implements ListSelectionListener {

    @Override
    protected void bindListener(BaseProperty<List<Integer>> property, MultiSelectToggleBar cmp) {
         cmp.addListSelectionListener(this);
    }


    protected void unbindListener(BaseProperty<List<Integer>> property, MultiSelectToggleBar cmp) {
        cmp.removeListSelectionListener(this);
    }


    protected void updateUI(List<Integer> newValue) {
        getComponent().setSelectedIndices(newValue);
    }

    public void valueChanged(ListSelectionEvent e) {
        callWhenUIChanged(getComponent().getSelectedIndices());


    }

    protected Class getType() {
        return List.class;
    }

    protected Class getComponentType() {
        return MultiSelectToggleBar.class;
    }

    protected boolean isSelectionBind() {
        return true;
    }


}
