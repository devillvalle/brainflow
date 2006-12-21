package com.brainflow.colormap;

import com.brainflow.utils.Range;

import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 12:13:48 PM
 * To change this template use File | Settings | File Templates.
 */


public class ColorMapTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Range", "Opacity", "Color"};


    private RaggedColorMap colorMap;
    private IColorMap sourceMap;

    public ColorMapTableModel(IColorMap _sourceMap) {
        sourceMap = _sourceMap;
        colorMap = new RaggedColorMap(sourceMap);
    }


    public RaggedColorMap getEditedColorMap() {
        return colorMap;
    }

    public IColorMap getSourceColorMap() {
        return sourceMap;
    }


    public void setTableSize(int newSize) {
        colorMap.setMapSize(newSize);
        fireTableDataChanged();
    }


    public void addEmptyRow(double increment) {

        Range lastRange = colorMap.getRange();

        Double newMax = new Double(lastRange.getMax() + increment);
        Color lastClr = colorMap.getInterval(colorMap.getMapSize() - 1).getColor();

        colorMap.extendHigher(newMax, lastClr.brighter());

        fireTableDataChanged();


    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return colorMap.getMapSize();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        ColorInterval interval = colorMap.getInterval(row);
        if (col == 0) {
            return new Range(interval.getMinimum(), interval.getMaximum());
        } else if (col == 1) {
            return interval.getAlpha();
        } else if (col == 2) {
            return interval.getColor();
        } else {
            throw new IllegalArgumentException();
        }

    }

    /*
    * JTable uses this method to determine the default renderer/
    * editor for each cell.  If we didn't implement this method,
    * then the last column would contain text ("true"/"false"),
    * rather than a check box.
    */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
    * Don't need to implement this method unless your table's
    * editable.
    */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.

        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        ColorInterval interval = colorMap.getInterval(row);
        if (col == 0) {
            Range range = (Range) value;
            ColorInterval ival = new ColorInterval(range.getMin(), range.getMax(), interval.getColor());
            int idx = colorMap.squeezeInterval(row, ival);
            fireTableStructureChanged();

            return;
            //
        } else if (col == 1) {
            int opacity = Math.min((Integer) value, 255);
            opacity = Math.max(0, opacity);
            Color newColor = new Color(interval.getRed(), interval.getGreen(), interval.getBlue(), opacity);
            colorMap.setColor(row, newColor);
            fireTableCellUpdated(row, col);
            fireTableCellUpdated(row, 2);
        } else if (col == 2) {
            Color clr = (Color) value;
            colorMap.setColor(row, clr);
            fireTableCellUpdated(row, col);

        }


    }

    public void clear() {

        fireTableDataChanged();
    }

    private void printDebugData() {

    }
}
