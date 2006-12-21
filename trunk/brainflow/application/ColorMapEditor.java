package com.brainflow.application;


import com.brainflow.colormap.ColorInterval;
import com.brainflow.colormap.IColorMap;
import com.brainflow.utils.Range;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Oct 15, 2003
 * Time: 4:11:16 PM
 * To change this template use Options | File Templates.
 */
public class ColorMapEditor extends JPanel {

    JTable colorTable;
    ColorTableModel tableModel;

    JSpinner rowSpinner;
    JFormattedTextField incField;
    JButton jbClear = new JButton("Clear");
    JButton jbApply = new JButton("Apply");
    JButton jbSave = new JButton("Save");
    JButton jbDismiss = new JButton("Dismiss");

    double defaultIncrement = .500;

    public static final String APPLY_PROPERTY = "APPLY";
    public static final String SAVE_PROPERTY = "SAVE";
    public static final String DISMISS_PROPERTY = "DISMISS";

    //String[] columnNames = new String[]{"Min", "Max", "Transparency", "Color"};

    public ColorMapEditor(Range startRange) {

        tableModel = new ColorTableModel(startRange, defaultIncrement);
        colorTable = new JTable(tableModel);

        init();

    }


    public ColorMapEditor(IColorMap icm) {
        int numRows = icm.getMapSize();
        ColorTableRow[] crows = new ColorTableRow[numRows];

        ColorInterval firstInterval = icm.getInterval(0);

        Range drange = new Range(icm.getMaximumValue(), icm.getMaximumValue());
        crows[0] = new ColorTableRow(firstInterval.getMinimum(), firstInterval.getMaximum(),
                (double) firstInterval.getAlpha(), firstInterval.getColor());

        for (int i = 1; i < numRows; i++) {
            ColorInterval curInterval = icm.getInterval(i);
            crows[i] = new ColorTableRow(crows[i - 1], curInterval.getMaximum() - curInterval.getMinimum());
            crows[i].setClr(curInterval.getColor());
        }

        tableModel = new ColorTableModel(crows);
        colorTable = new JTable(tableModel);

        init();
    }


    private void init() {
        setLayout(new BorderLayout());
        setUpColorRenderer(colorTable);
        setUpColorEditor(colorTable);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(colorTable);
        add(scrollPane, BorderLayout.CENTER);


        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 256, 1);
        rowSpinner = new JSpinner(model);
        rowSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSpinner spin = (JSpinner) e.getSource();
                Number ival = (Number) spin.getValue();
                System.out.println("row is: " + ival);
                int rowCount = tableModel.getRowCount();
                System.out.println("row count is: " + rowCount);
                if (rowCount < ival.intValue()) {
                    tableModel.addEmptyRow();
                    System.out.println("added row");
                } else if (rowCount > ival.intValue()) {
                    System.out.println("deleted row");
                    tableModel.deleteLastRow();
                } else {
                    System.out.println("did nothing");
                }

                System.out.println("hello");


            }
        });


        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(3);
        format.setMaximumIntegerDigits(5);
        incField = new JFormattedTextField(format);
        incField.setValue(new Double(defaultIncrement));

        incField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (incField.isEditValid()) {
                    defaultIncrement = ((Number) incField.getValue()).doubleValue();
                    tableModel.setDefaultIncrement(defaultIncrement);
                }
            }
        });

        FormLayout flayout = new FormLayout("4dlu, r:p, p, 5dlu, l:p, 25dlu, 10dlu", "p");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(flayout);
        CellConstraints cc = new CellConstraints();
        bottomPanel.add(new JLabel("Segments:"), cc.xy(2, 1));

        JLabel fieldLabel = new JLabel("Increment: ");
        bottomPanel.add(rowSpinner, cc.xy(3, 1));
        bottomPanel.add(fieldLabel, cc.xy(5, 1));
        bottomPanel.add(incField, cc.xy(6, 1));
        //bottomPanel.add(jbClear, cc.xy(8,1));
        //bottomPanel.add(jbApply, cc.xy(10,1));
        //bottomPanel.add(jbDismiss, cc.xy(12,1));
        add(bottomPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel();
        topPanel.add(jbClear);
        topPanel.add(jbApply);
        topPanel.add(jbSave);
        add(topPanel, BorderLayout.NORTH);

        jbClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableModel.clear();
                rowSpinner.setValue(new Integer(0));
            }
        });

        //colorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //colorTable.setPreferredScrollableViewportSize(new Dimension(400,400));
        //colorTable.getColumnModel().getColumn(2).setPreferredWidth(60);


    }


    class ColorTableRow {

        Double min;
        Double max;

        Range range;

        Double alpha;
        Color clr;

        public ColorTableRow(ColorTableRow _previous, double increment) {
            min = _previous.max;
            max = new Double(min.doubleValue() + increment);
            alpha = new Double(1.0);
            clr = Color.WHITE;
            range = new Range(min.doubleValue(), max.doubleValue());
        }

        public ColorTableRow(Double min, Double max, Double alpha, Color clr) {
            this.min = min;
            this.max = max;
            this.alpha = alpha;
            this.clr = clr;
            range = new Range(min.doubleValue(), max.doubleValue());
        }

        public Double getAlpha() {
            return alpha;
        }

        public Color getClr() {
            return clr;
        }

        public Double getMax() {
            return max;
        }

        public Double getMin() {
            return min;
        }

        public boolean inRange(double val) {
            if (range.contains(val))
                return true;
            else
                return false;
        }

        public void setAlpha(Double alpha) {
            this.alpha = alpha;
        }

        public void setClr(Color clr) {
            this.clr = clr;
        }

        public void setMax(Double max) {
            this.max = max;
        }

        public void setMin(Double min) {
            this.min = min;
        }

        public Object[] toArray() {
            return new Object[]{min, max, alpha, clr};
        }


    }


    class ColorRenderer extends JLabel implements TableCellRenderer {
        Border unselectedBorder = null;
        Border selectedBorder = null;
        boolean isBordered = true;

        public ColorRenderer(boolean isBordered) {
            super();
            this.isBordered = isBordered;
            setOpaque(true); //MUST do this for background to show up.
        }

        public Component getTableCellRendererComponent(
                JTable table, Object color,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            setBackground((Color) color);
            if (isBordered) {
                if (isSelected) {
                    if (selectedBorder == null) {
                        selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5,
                                table.getSelectionBackground());
                    }
                    setBorder(selectedBorder);
                } else {
                    if (unselectedBorder == null) {
                        unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5,
                                table.getBackground());
                    }
                    setBorder(unselectedBorder);
                }
            }
            return this;
        }
    }

    private void setUpColorRenderer(JTable table) {
        table.setDefaultRenderer(Color.class,
                new ColorRenderer(true));
    }


    private void setUpColorEditor(JTable table) {
        //First, set up the button that brings up the dialog.
        final JButton button = new JButton("") {
            public void setText(String s) {
                //Button never shows text -- only color.
            }
        };

        button.setBackground(Color.white);
        button.setBorderPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));

        //Now create an editor to encapsulate the button, and
        //set it up as the editor for all Color cells.
        final ColorEditor colorEditor = new ColorEditor(button);
        table.setDefaultEditor(Color.class, colorEditor);

        //Set up the dialog that the button brings up.
        final JColorChooser colorChooser = new JColorChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorEditor.currentColor = colorChooser.getColor();
            }
        };
        final JDialog dialog = JColorChooser.createDialog(button,
                "Pick a Color",
                true,
                colorChooser,
                okListener,
                null); //XXXDoublecheck this is OK

        //Here's the code that brings up the dialog.
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button.setBackground(colorEditor.currentColor);
                colorChooser.setColor(colorEditor.currentColor);
                //Without the following line, the dialog comes up
                //in the middle of the screen.
                //dialog.setLocationRelativeTo(button);
                dialog.show();
            }
        });
    }

    /*
     * The editor button that brings up the dialog.
     * We extend DefaultCellEditor for convenience,
     * even though it mean we have to create a dummy
     * check box.  Another approach would be to copy
     * the implementation of TableCellEditor methods
     * from the source code for DefaultCellEditor.
     */
    class ColorEditor extends DefaultCellEditor {
        Color currentColor = null;

        public ColorEditor(JButton b) {
            super(new JCheckBox()); //Unfortunately, the constructor
            //expects a check box, combo box,
            //or text field.
            editorComponent = b;
            setClickCountToStart(1); //This is usually 1 or 2.

            //Must do this so that editing stops when appropriate.
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        public Object getCellEditorValue() {
            return currentColor;
        }

        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            ((JButton) editorComponent).setText(value.toString());
            currentColor = (Color) value;
            return editorComponent;
        }
    }

    class ColorTableModel extends AbstractTableModel {
        final String[] columnNames = {"Min",
                "Max",
                "Opacity",
                "Color"};

        Object[][] data = null;
        Range absoluteRange;
        double increment;

        public ColorTableModel(ColorTableRow[] crows) {
            data = new Object[crows.length][];
            for (int i = 0; i < crows.length; i++) {
                data[i] = crows[i].toArray();
            }
            absoluteRange = new Range(crows[0].getMin().doubleValue(), crows[crows.length - 1].getMax().doubleValue());
            increment = crows[0].getMax().doubleValue() - crows[0].getMin().doubleValue();
        }

        public ColorTableModel(Range _absoluteRange, double _increment) {
            absoluteRange = _absoluteRange;
            increment = _increment;
            data = new Object[1][];
            data[0] = new Object[]{new Double(absoluteRange.getMin()), new Double(absoluteRange.getMin() + increment),
                    new Double(1), Color.red};
        }

        public ColorTableRow[] createColorTableRows() {
            ColorTableRow[] crows = new ColorTableRow[data.length];
            for (int i = 0; i < crows.length; i++) {
                crows[i] = new ColorTableRow((Double) data[i][0], (Double) data[i][1], (Double) data[i][2], (Color) data[i][3]);
            }
            return crows;
        }


        public void setDefaultIncrement(double _increment) {
            increment = _increment;
        }

        public void deleteLastRow() {
            if (data == null) return;

            if (data.length == 1) {
                data = null;
                fireTableDataChanged();
                return;
            }


            Object[][] ndata = new Object[data.length - 1][];
            for (int i = 0; i < ndata.length; i++) {
                ndata[i] = data[i];
            }

            data = ndata;
            fireTableDataChanged();

        }

        public void addEmptyRow() {
            System.out.println("called addEmptyRow");
            if (data == null) {
                data = new Object[][]{{new Double(absoluteRange.getMin()), new Double(absoluteRange.getMin() + increment), new Double(1), Color.WHITE}};
            } else {
                System.out.println("adding row!");
                Object[][] ndata = new Object[data.length + 1][columnNames.length];
                for (int i = 0; i < data.length; i++) {
                    ndata[i] = data[i];
                }

                Double lastMax = (Double) ndata[data.length - 1][1];
                Double newMax = new Double(lastMax.doubleValue() + increment);
                Color lastClr = (Color) ndata[data.length - 1][3];
                if (newMax.doubleValue() > absoluteRange.getMax()) {
                    newMax = new Double(absoluteRange.getMax());
                }

                ndata[data.length] = new Object[]{lastMax, newMax, new Double(1), lastClr.brighter()};

                data = ndata;
            }

            fireTableDataChanged();


        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (data == null) {
                return 0;
            }
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (data == null) {
                return "";
            }

            return data[row][col];

        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            if (data == null) {
                return "".getClass();
            }
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.

            if ((row > 0) && (col == 0)) {
                return false;
            } else {
                return true;
            }
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);

        }

        public void clear() {
            data = null;
            fireTableDataChanged();
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }


}
