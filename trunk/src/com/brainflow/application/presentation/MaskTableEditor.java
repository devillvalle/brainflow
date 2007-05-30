package com.brainflow.application.presentation;

import com.brainflow.core.*;
import com.brainflow.utils.Range;
import com.brainflow.utils.RangeModel;
import com.brainflow.utils.IRange;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.MaskedData3D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.io.analyze.AnalyzeIO;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.display.ThresholdRange;
import com.brainflow.application.BrainflowException;
import com.brainflow.colormap.RangeCellEditor;
import com.brainflow.colormap.RangeCellRenderer;

import com.jidesoft.grid.*;
import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.combobox.ListComboBox;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.*;
import javax.swing.event.CellEditorListener;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.*;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.IndexedPropertyChangeEvent;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaSkyMetallicLookAndFeel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 2:03:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskTableEditor extends JComponent {


    private MaskList maskList;

    private JideTable maskTable;

    private MaskTableModel tableModel;


    public MaskTableEditor(MaskList maskList) {
        this.maskList = maskList;

        tableModel = new MaskTableModel();

        maskTable = new CellStyleTable(tableModel);

        maskTable.setRowHeight(23);


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


        maskTable.setDefaultEditor(Integer.class, new GroupCellEditor());
        maskTable.setDefaultEditor(IRange.class, new RangeCellEditor());
        maskTable.setDefaultEditor(IImageData.class, new ImageCellEditor());
        maskTable.setDefaultRenderer(IImageData.class, CellRendererManager.getRenderer(String.class));

        maskTable.setDefaultRenderer(IRange.class, new RangeCellRenderer());
        maskTable.setDefaultEditor(BinaryOperation.class, new OpCellEditor());
        setLayout(new BorderLayout());


        add(new JScrollPane(maskTable), BorderLayout.CENTER);
        final JButton addButton = new JButton("Add Row");


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
        add(panel, BorderLayout.SOUTH);


        initColumns();

        TableUtils.autoResizeAllColumns(maskTable);
    }


    private void initColumns() {
        maskTable.setColumnAutoResizable(true);
        //maskTable.getColumn("Image").setMaxWidth(160);
        maskTable.getColumn("Oper").setMinWidth(50);
        maskTable.getColumn("Oper").setMaxWidth(80);
        maskTable.getColumn("Group").setMaxWidth(55);
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
            SyntheticaLookAndFeel lf = new SyntheticaSkyMetallicLookAndFeel();

            UIManager.setLookAndFeel(lf);
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_temporal.hdr");
            IImageData data = AnalyzeIO.readAnalyzeImage(url);


            IImageDisplayModel model = new ImageDisplayModel("none");
            model.addLayer(new ImageLayer3D(data, new ImageLayerProperties()));

            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            data = AnalyzeIO.readAnalyzeImage(url);
            model.addLayer(new ImageLayer3D(data, new ImageLayerProperties()));

            MaskList mlist = new MaskList(model, new MaskItem((IImageData3D) data, new ThresholdRange(-50, 16000), 1));
            mlist.addMask(new MaskItem((IImageData3D) data, new ThresholdRange(0, 12000), 1));
            mlist.addMask(new MaskItem((IImageData3D) data, new ThresholdRange(0, 18000), 2));
            mlist.addMask(new MaskItem((IImageData3D) data, new ThresholdRange(0, 22000), 2));

            JFrame jf = new JFrame();
            MaskTableEditor editor = new MaskTableEditor(mlist);


            jf.add(editor, BorderLayout.CENTER);
            jf.pack();
            jf.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    class MaskTableModel extends AbstractTableModel {

        private String[] columnNames = new String[]{"Image", "Range", "Group", "Oper", "On/Off"};


        private Map<String, Integer> columnMap = new HashMap<String, Integer>();

        private Class[] colClasses = new Class[]{IImageData.class, IRange.class, Integer.class, BinaryOperation.class, Boolean.class};


        public MaskTableModel() {
            columnMap.put(MaskItem.SOURCE_IMAGE_PROPERTY, 0);
            columnMap.put(MaskItem.THRESHOLD_PREDICATE_PROPERTY, 1);
            columnMap.put(MaskItem.GROUP_PROPERTY, 2);
            columnMap.put(MaskItem.BINARY_OPERATION_PROPERTY, 3);
            columnMap.put(MaskItem.ACTIVE_PROPERTY, 4);


            maskList.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    IndexedPropertyChangeEvent ievt = (IndexedPropertyChangeEvent) evt;

                    int idx = ievt.getIndex();
                    System.out.println("updating cell " + idx + ", " + columnMap.get(ievt.getPropertyName()));
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
            return true;
        }

        public void addRow() {
            MaskItem item = new MaskItem(maskList.getMaskItem(0).getSource(), new ThresholdRange(0, 0), maskList.getLastItem().getGroup());
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
                    System.out.println("setting source " + aValue);
                    maskList.getMaskItem(rowIndex).setSource((IImageData3D) aValue);
                    break;
                case 1:
                    maskList.getMaskItem(rowIndex).setPredicate((ThresholdRange) aValue);
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

    class GroupCellEditor extends ContextSensitiveCellEditor implements ItemListener {

        ListComboBox comboBox = new ListComboBox(new Integer[]{1, 2, 3});

        Integer group;

        public GroupCellEditor() {
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


            comboBox.setConverterContext(getConverterContext());
            MaskItem item = maskList.getMaskItem(row);
            int gnum = item.getGroup();

            if (item == maskList.getLastItem() && row == 0) {
                comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum}));
            } else
            if (item == maskList.getLastItem() && (row > 0) && (gnum == maskList.getMaskItem(row - 1).getGroup())) {
                comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum, gnum + 1}));
            } else {

                comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum}));
            }

            comboBox.setSelectedIndex(0);

            group = gnum;
            comboBox.addItemListener(this);

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


    class OpCellEditor extends ContextSensitiveCellEditor implements ItemListener {

        private ListComboBox comboBox = new ListComboBox(new Object[]{BinaryOperation.AND, BinaryOperation.OR});

        public OpCellEditor() {
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

            comboBox.setConverterContext(getConverterContext());

            comboBox.setSelectedItem(maskList.getMaskItem(row).getOperation());
            
            comboBox.addItemListener(this);
            return comboBox;
        }

        public void itemStateChanged(ItemEvent e) {
            comboBox.removeItemListener(this);
            stopCellEditing();

        }


    }

    class ImageCellEditor extends ContextSensitiveCellEditor implements ItemListener {

        private ListComboBox comboBox = new ListComboBox(new Object[]{});

        public ImageCellEditor() {
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

            List<IImageData> list = maskList.getCongruentImages();
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
            System.out.println("" + e.getStateChange());
            comboBox.removeItemListener(this);
            stopCellEditing();

        }


    }


}
