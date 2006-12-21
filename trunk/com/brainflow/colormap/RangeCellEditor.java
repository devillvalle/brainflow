package com.brainflow.colormap;

import com.brainflow.gui.RangeComboBox;
import com.brainflow.utils.Range;
import com.brainflow.utils.RangeModel;
import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 1:01:53 PM
 * To change this template use File | Settings | File Templates.
 */
class RangeCellEditor extends ContextSensitiveCellEditor implements ItemListener, ActionListener {

    private Range currentRange;
    private RangeComboBox comboBox = new RangeComboBox();


    public RangeCellEditor() {
        super();
        this.setClickCountToStart(1);
    }


    public Object getCellEditorValue() {
        return currentRange;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        if (table != null) {
            JideSwingUtilities.installColorsAndFont(comboBox, table.getBackground(), table.getForeground(), table.getFont());
        }

        currentRange = (Range) value;
        comboBox.setRange(new RangeModel(currentRange));
        comboBox.setConverterContext(getConverterContext());
        comboBox.addItemListener(this);
        comboBox.addTextFieldActionListener(this);
        return comboBox;


    }

    public boolean stopCellEditing() {
        comboBox.setPopupVisible(false);
        return super.stopCellEditing();
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            comboBox.removeItemListener(this);
            stopCellEditing();
        }
    }


    public void actionPerformed(ActionEvent e) {
        comboBox.removeTextFieldActionListener(this);
        stopCellEditing();
    }
}
