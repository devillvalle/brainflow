package com.brainflow.colormap;

import com.brainflow.gui.AbstractPresenter;
import com.brainflow.utils.Range;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.JideTable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 8, 2006
 * Time: 10:35:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorMapTable extends AbstractPresenter {

    private IColorMap colorMap;
    private JideTable colorTable;
    private ColorMapTableModel tableModel;


    public ColorMapTable(IColorMap sourceMap) {
        colorMap = sourceMap;
        init();
    }

    public JTable getComponent() {

        return colorTable;
    }

    private void init() {

        CellEditorManager.initDefaultEditor();
        CellRendererManager.initDefaultRenderer();
        ObjectConverterManager.initDefaultConverter();

        tableModel = new ColorMapTableModel(colorMap);
        colorTable = new CellStyleTable(tableModel);
        colorTable.setDefaultEditor(Color.class, (TableCellEditor) CellEditorManager.getEditor(Color.class));
        colorTable.setDefaultEditor(Range.class, new RangeCellEditor());
        colorTable.setDefaultRenderer(Range.class, new RangeCellRenderer());
        colorTable.setDefaultRenderer(Color.class, CellRendererManager.getRenderer(Color.class));
        setUpColorRenderer(colorTable);


    }

    public ColorMapTableModel getModel() {
        return tableModel;
    }

    public void setColorMap(IColorMap cmap) {

        if (cmap != tableModel.getEditedColorMap()) {
            colorMap = cmap;
            tableModel = new ColorMapTableModel(colorMap);
            colorTable.setModel(tableModel);
        }
    }

    public IColorMap getEditedColorMap() {
        return tableModel.getEditedColorMap();

    }


    private void setUpColorRenderer(JTable table) {
        table.setDefaultRenderer(Color.class,
                new ColorCellRenderer(true));
    }


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "Brainflow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");

        System.out.println(CellRendererManager.getRenderer(Color.class).getClass());
        ColorMapTable table = new ColorMapTable(new LinearColorMap(0, 255, ColorTable.SPECTRUM));
        JFrame jf = new JFrame();
        jf.add(new JScrollPane(table.getComponent()));
        jf.pack();
        jf.setVisible(true);

    }

}