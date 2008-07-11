package com.brainflow.application.presentation.binding;

import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.IndexedProperty;

import javax.swing.*;

import com.jidesoft.swing.CheckBoxList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 8, 2008
 * Time: 7:02:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class JideBind extends SwingBind {

    private static JideBind instance;

    protected JideBind() {
        addAdapter(new CheckBoxListAdapter());
    }

    public void bindCheckedIndices(IndexedProperty<Integer> property, CheckBoxList cmp) {
        new CheckBoxListAdapter().bind(property, cmp);
    }

    public static JideBind get() {
        if (instance == null) {
            instance = new JideBind();
        }
        
        return instance;
    }

}
