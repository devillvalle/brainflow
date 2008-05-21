package com.brainflow.application.presentation;

import com.brainflow.colormap.RangeCellEditor;
import com.brainflow.colormap.RangeCellRenderer;
import com.brainflow.core.*;
import com.brainflow.core.layer.*;
import com.brainflow.display.ThresholdRange;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.BrainIO;
import com.brainflow.image.operations.Operations;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.utils.IRange;
import com.brainflow.utils.Range;
import com.jidesoft.combobox.ListComboBox;
import com.jidesoft.grid.*;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 2:03:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskListEditor {


    private ImageMaskList maskList;

    private JideTable maskTable;

    private MaskTableModel tableModel;

    private IImageDisplayModel model;


    public MaskListEditor(IImageDisplayModel model, ImageMaskList maskList) {
        this.model = model;

        this.maskList = maskList;

        tableModel = new MaskTableModel();

        maskTable = new CellStyleTable(tableModel);
        maskTable.setRowHeight(23);
        maskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        CellEditorManager.initDefaultEditor();
        CellEditorFactory editorFactory = new CellEditorFactory() {
            public CellEditor create() {
                return new BooleanCheckBoxCellEditor() {
                    protected void configureCheckBox() {
                        super.configureCheckBox();
                        _checkBox.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                };
            }
        };

        CellEditorManager.registerEditor(Boolean.class, editorFactory, BooleanCheckBoxCellEditor.CONTEXT);
        CellEditorManager.registerEditor(boolean.class, editorFactory, BooleanCheckBoxCellEditor.CONTEXT);


        maskTable.setDefaultEditor(Integer.class, new GroupCellEditor(new DefaultComboBoxModel()));
        maskTable.setDefaultEditor(IRange.class, new RangeCellEditor());
        maskTable.setDefaultEditor(AbstractLayer.class, new MaskCellEditor());
        maskTable.setDefaultRenderer(AbstractLayer.class, CellRendererManager.getRenderer(String.class));

        maskTable.setDefaultRenderer(IRange.class, new RangeCellRenderer());
        maskTable.setDefaultEditor(Operations.class, new OpCellEditor());

        /*final JButton addButton = new JButton("Add Row");
     final JButton removeButton = new JButton("Remove Row");
     removeButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             if (tableModel.getRowCount() > 1) {
                 tableModel.deleteRow();
             }

             if (tableModel.getRowCount() == 1) {
                 removeButton.setEnabled(false);
             }
         }
     });

     addButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             tableModel.addRow();
             if (tableModel.getRowCount() > 1) {
                 removeButton.setEnabled(true);

             }
         }
     });

     JPanel panel = new JPanel();
     panel.add(addButton);
     panel.add(removeButton);
     add(panel, BorderLayout.SOUTH);  */


        initColumns();

        TableUtils.autoResizeAllColumns(maskTable);
    }


    public JTable getTable() {
        return maskTable;
    }

    private void initColumns() {
        maskTable.setColumnAutoResizable(true);
        //maskTable.getColumn("Image").setMaxWidth(160);
        //maskTable.getColumn("Op").setMinWidth(50);
        maskTable.getColumn("Oper").setMaxWidth(80);
        maskTable.getColumn("Group").setMaxWidth(55);
    }


    public IImageDisplayModel getModel() {
        return model;
    }

    public void setModel(IImageDisplayModel model) {
        this.model = model;
        AbstractLayer layer = model.getLayer(model.getSelectedIndex());
        if (layer instanceof ImageLayer) {
            ImageLayer ilayer = (ImageLayer) layer;
            maskList = ilayer.getMaskList();
            tableModel.fireTableDataChanged();
        } else {
            // empy list ...
            //maskList = new ImageMaskList();
        }

    }

    public void addMaskItem() {
        tableModel.addRow();

    }

    public void deleteLastItem() {
        if (maskList.size() > 1) {
            maskList.removeMaskItem(maskList.size() - 1);
        }
    }

    public static void main(String[] args) {
        try {
            com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");

            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_temporal.hdr");
            IImageData data = BrainIO.readAnalyzeImage(url);


            IImageDisplayModel model = new ImageDisplayModel("none");
            model.addLayer(new ImageLayer3D(data, new ImageLayerProperties(new Range(0, 256))));

            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            data = BrainIO.readAnalyzeImage(url);
            model.addLayer(new ImageLayer3D(data, new ImageLayerProperties(new Range(0, 256))));

            ImageMaskList mlist = new ImageMaskList(new ImageLayer3D(data));
            mlist.addMask(new ImageMaskItem(new ImageLayer3D(data), new ThresholdRange(0, 12000), 1));
            mlist.addMask(new ImageMaskItem(new ImageLayer3D(data), new ThresholdRange(0, 18000), 2));
            mlist.addMask(new ImageMaskItem(new ImageLayer3D(data), new ThresholdRange(0, 22000), 2));

            JFrame jf = new JFrame();
            MaskListEditor editor = new MaskListEditor(model, mlist);


            jf.add(editor.getTable(), BorderLayout.CENTER);
            jf.pack();
            jf.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    class MaskTableModel extends AbstractTableModel {

        private String[] columnNames = new String[]{"Mask", "Range", "Group", "Oper", "On"};


        private Map<String, Integer> columnMap = new HashMap<String, Integer>();

        private Class[] colClasses = new Class[]{AbstractLayer.class, IRange.class, Integer.class, Operations.class, Boolean.class};


        public MaskTableModel() {
            columnMap.put(IMaskItem.SOURCE_IMAGE_PROPERTY, 0);
            columnMap.put(IMaskItem.THRESHOLD_PREDICATE_PROPERTY, 1);
            columnMap.put(IMaskItem.GROUP_PROPERTY, 2);
            columnMap.put(IMaskItem.BINARY_OPERATION_PROPERTY, 3);
            columnMap.put(IMaskItem.ACTIVE_PROPERTY, 4);


            maskList.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    IndexedPropertyChangeEvent ievt = (IndexedPropertyChangeEvent) evt;

                    int idx = ievt.getIndex();
                    fireTableCellUpdated(idx, columnMap.get(ievt.getPropertyName()));

                }
            });
        }


        public int getRowCount() {
            return maskList.size();

        }


        public Class getColumnClass(int c) {
            return colClasses[c];
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (rowIndex == 0) return false;

            return true;
        }

        public void addRow() {
            ImageMaskItem item = new ImageMaskItem(maskList.getMaskItem(0).getSource(), new ThresholdRange(0, 0), maskList.getLastItem().getGroup());
            item.setActive(false);
            maskList.addMask(item);

            this.fireTableRowsInserted(maskList.size() - 1, maskList.size() - 1);
        }

        public void deleteRow() {
            maskList.removeMaskItem(maskList.size() - 1);
            this.fireTableRowsDeleted(maskList.size() - 1, maskList.size() - 1);

        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    IMaskItem item = maskList.getMaskItem(rowIndex);
                    item.setSource((ImageLayer)aValue);
                    //maskList.getMaskItem(rowIndex).setSource((IImageData3D) aValue);
                    break;
                case 1:
                    //maskList.getMaskItem(rowIndex).setPredicate((ThresholdRange) aValue);
                    break;
                case 2:
                    maskList.getMaskItem(rowIndex).setGroup((Integer) aValue);
                    break;
                case 3:
                    maskList.getMaskItem(rowIndex).setOperation((BinaryOperation) aValue);
                    break;
                case 4:
                    maskList.getMaskItem(rowIndex).setActive((Boolean) aValue);
                    break;


                default:
                    throw new AssertionError();
            }
        }


        public Object getValueAt(int rowIndex, int columnIndex) {

            switch (columnIndex) {
                case 0:
                    if (rowIndex == 0) return "This";
                    return maskList.getMaskItem(rowIndex).getSource();
                case 1:
                    return maskList.getMaskItem(rowIndex).getPredicate();
                case 2:
                    return maskList.getMaskItem(rowIndex).getGroup();
                case 3:
                    return maskList.getMaskItem(rowIndex).getOperation();
                case 4:
                    return maskList.getMaskItem(rowIndex).isActive();

                default:
                    throw new AssertionError();
            }

        }

        public int getColumnCount() {
            return 5;
        }
    }

    class GroupCellEditor extends ListComboBoxCellEditor implements ItemListener {


        Integer group;

        public GroupCellEditor(ComboBoxModel model) {
            super(model);


        }


        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            IMaskItem item = maskList.getMaskItem(row);
            int gnum = item.getGroup();

            if (item == maskList.getLastItem() && row == 0) {
                _comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum}));
            } else
            if (item == maskList.getLastItem() && (row > 0) && (gnum == maskList.getMaskItem(row - 1).getGroup())) {
                _comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum, gnum + 1}));
            } else {

                _comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum}));
            }

            return super.getTableCellEditorComponent(table, value, isSelected, row, column);

        }


    }


    class OpCellEditor extends ListComboBoxCellEditor implements ItemListener {

        public OpCellEditor() {
            super(new DefaultComboBoxModel(new BinaryOperation[]{Operations.AND, Operations.OR}));
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            _comboBox.setSelectedItem(maskList.getMaskItem(row).getOperation());
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }


    }

    class MaskCellEditor extends ContextSensitiveCellEditor implements ItemListener {

        private ListComboBox comboBox = new ListComboBox(new Object[]{});


        public MaskCellEditor() {
            comboBox.setBorder(BorderFactory.createEmptyBorder());
            comboBox.setEditable(false);

        }

        public Object getCellEditorValue() {

            return comboBox.getSelectedItem();
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            if (table != null) {
                JideSwingUtilities.installColorsAndFont(comboBox, table.getBackground(), table.getForeground(), table.getFont());
            }

            List<? extends AbstractLayer> list = maskList.getCongruentLayers(model);
            System.out.println("num congruent layers : " + list.size());
            ComboBoxModel model = new DefaultComboBoxModel(list.toArray());
            //comboBox.setConverterContext(getConverterContext());

            comboBox.setModel(model);
            comboBox.setSelectedItem(maskList.getMaskItem(row).getSource());
            comboBox.addItemListener(this);

            //comboBox.addActionListener(this);
            return comboBox;
        }

        public boolean stopCellEditing() {
            comboBox.setPopupVisible(false);

            return super.stopCellEditing();
        }

        public void itemStateChanged(ItemEvent e) {

            comboBox.removeItemListener(this);
            stopCellEditing();

        }


    }


}
