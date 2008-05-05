package com.brainflow.application.presentation;

import com.brainflow.core.*;
import com.brainflow.utils.IRange;
import com.brainflow.image.operations.Operations;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.display.ThresholdRange;
import com.brainflow.colormap.RangeCellEditor;
import com.brainflow.colormap.RangeCellRenderer;
import com.jidesoft.grid.*;
import com.jidesoft.combobox.ListComboBox;
import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.swing.JideBoxLayout;
import com.xduke.xswing.DataTipManager;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.util.*;
import java.util.List;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 8, 2007
 * Time: 7:24:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskTablePresenter extends ImageViewPresenter {


    private JPanel form;

    private TreeTableModel tableModel;

    private TreeTable maskTable;

    public MaskTablePresenter() {
        maskTable = new TreeTable();
        tableModel = createTableModel();
        maskTable.setModel(tableModel);


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


        maskTable.setDefaultEditor(Operations.class, new OpCellEditor());
        maskTable.setDefaultEditor(Integer.class, new GroupCellEditor());

        maskTable.setDefaultEditor(IRange.class, new RangeCellEditor());
        maskTable.setDefaultEditor(AbstractLayer.class, new MaskCellEditor());
        //maskTable.setDefaultRenderer(AbstractLayer.class, CellRendererManager.getRenderer(String.class));

        maskTable.setDefaultRenderer(IRange.class, new RangeCellRenderer());


        maskTable.setRowHeight(22);
        maskTable.setShowTreeLines(true);
        DataTipManager.get().register(maskTable);
        maskTable.setShowGrid(true);
        //maskTable.setIntercellSpacing(new Dimension(0, 0));

        // do not select row when expanding a row.
        //maskTable.setSelectRowWhenToggling(false);

        form = new JPanel();
        form.setLayout(new JideBoxLayout(form, JideBoxLayout.Y_AXIS));
        form.add(createToolBar(), JideBoxLayout.FIX);
        form.add(new JScrollPane(maskTable), JideBoxLayout.VARY);


        TableUtils.autoResizeAllColumns(maskTable);

    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        Action addAction = createAddItemAction();
        addAction.putValue(Action.NAME, "add item");
        toolBar.add(addAction);

        return toolBar;

    }


    private Action createAddItemAction() {

        return new AbstractAction() {


            public void actionPerformed(ActionEvent e) {
                int selRow = maskTable.getSelectedRow();
                if (selRow >= 0) {

                    Row row = maskTable.getRowAt(selRow);
                    MaskLayerRow parentRow = null;
                    if (row instanceof MaskLayerRow) {
                        parentRow = (MaskLayerRow) row;
                    } else if (row instanceof MaskChildRow) {
                        parentRow = ((MaskChildRow) row).getParent();
                    } else {
                        throw new AssertionError();
                    }

                    IMaskItem lastItem = parentRow.getLayer().getMaskList().dupMask();

                    MaskChildRow childRow = new MaskChildRow(parentRow, lastItem);
                    tableModel.addRow(parentRow, childRow);
                    tableModel.expandRow(parentRow, true);
                }


            }

        };


    }

    private void updateModel() {
        tableModel = createTableModel();
        maskTable.setModel(tableModel);

    }

    public void intervalAdded(ListDataEvent e) {
        int i = e.getIndex0();
        AbstractLayer layer = getSelectedView().getModel().getLayer(i);
        MaskLayerRow row = new MaskLayerRow(layer);
        tableModel.addRow(row);
    }

    public void intervalRemoved(ListDataEvent e) {
        tableModel.removeRow(e.getIndex0());
    }

    public void contentsChanged(ListDataEvent e) {
        updateModel();
    }

    private TreeTableModel createTableModel() {
        ImageView view = getSelectedView();

        if (view == null) {
            return new MaskRowTableModel(new ArrayList<MaskLayerRow>());
        }

        int numLayers = view.getModel().getNumLayers();
        ArrayList<MaskLayerRow> rootList = new ArrayList<MaskLayerRow>();

        for (int i = 0; i < numLayers; i++) {
            rootList.add(new MaskLayerRow(view.getModel().getLayer(i)));

        }
        return new MaskRowTableModel(rootList);
    }


    public void allViewsDeselected() {
        maskTable.setEnabled(false);
    }

    public void viewSelected(ImageView view) {
        maskTable.setEnabled(true);
        updateModel();

    }

    public JComponent getComponent() {
        return form;
    }


    class MaskRowTableModel extends TreeTableModel {
        private String[] columnNames = new String[]{"Mask", "Range", "Group", "Oper", "On"};


        private Map<String, Integer> columnMap = new HashMap<String, Integer>();

        private Class[] colClasses = new Class[]{AbstractLayer.class, IRange.class, Integer.class, Operations.class, Boolean.class};


        public MaskRowTableModel(List<MaskLayerRow> list) {
            super(list);
            columnMap.put(IMaskItem.SOURCE_IMAGE_PROPERTY, 0);
            columnMap.put(IMaskItem.THRESHOLD_PREDICATE_PROPERTY, 1);
            columnMap.put(IMaskItem.GROUP_PROPERTY, 2);
            columnMap.put(IMaskItem.BINARY_OPERATION_PROPERTY, 3);
            columnMap.put(IMaskItem.ACTIVE_PROPERTY, 4);

            /*maskList.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    IndexedPropertyChangeEvent ievt = (IndexedPropertyChangeEvent) evt;

                    int idx = ievt.getIndex();
                    fireTableCellUpdated(idx, columnMap.get(ievt.getPropertyName()));

                }
            });*/
        }


        public String getColumnName(int column) {
            return columnNames[column];    //To change body of overridden methods use File | Settings | File Templates.
        }

        public Class getColumnClass(int columnIndex) {
            return colClasses[columnIndex];
        }

        public boolean isCellEditable(int row, int col) {
            RowItem rowItem = (RowItem) getRowAt(row);
            if (rowItem.isRoot()) {
                switch(col) {
                    case 0:
                        return false;
                    case 1:
                        return true;
                    case 2:
                        return false;
                    case 3:
                        return false;
                    case 4:
                        return true;
                    default:
                        return false;
                }
            }

            return true;

        }

        public int getColumnCount() {
            return columnNames.length;
        }


    }

    interface RowItem {
        public IMaskItem getMaskItem();

        public AbstractLayer getLayer();

        public boolean isRoot();
    }


    class MaskLayerRow extends AbstractExpandableRow implements RowItem {

        private AbstractLayer rootLayer;

        private List<MaskChildRow> children;


        public MaskLayerRow(AbstractLayer layer) {

            rootLayer = layer;
            initChildren();

        }


        public void initChildren() {
            IMaskList maskList = rootLayer.getMaskList();
            children = new ArrayList<MaskChildRow>();
            for (int i = 1; i < maskList.size(); i++) {
                children.add(new MaskChildRow(this, maskList.getMaskItem(i)));
            }


        }

        public AbstractLayer getLayer() {
            return rootLayer;
        }

        public boolean isRoot() {
            return true;
        }

        public List getChildren() {
            if (children == null) initChildren();
            return children;
        }

        public void setChildren(List list) {
            throw new UnsupportedOperationException();
        }

        public IMaskItem getMaskItem() {
            return rootLayer.getMaskList().getMaskItem(0);
        }


        public Object getValueAt(int columnIndex) {
            IMaskList list = rootLayer.getMaskList();

            switch (columnIndex) {
                case 0:
                    return list.getMaskItem(0).getSource();
                case 1:
                    return list.getMaskItem(0).getPredicate();
                case 2:
                    return list.getMaskItem(0).getGroup();
                case 3:
                    return list.getMaskItem(0).getOperation();
                case 4:
                    return list.getMaskItem(0).isActive();

                default:
                    throw new AssertionError();
            }

        }
    }

    class MaskChildRow extends AbstractRow implements RowItem {


        private IMaskItem item;

        public MaskChildRow(MaskLayerRow parentRow, IMaskItem item) {
            setParent(parentRow);
            this.item = item;

        }

        public AbstractLayer getLayer() {
            MaskLayerRow prow = (MaskLayerRow) getParent();
            return prow.getLayer();
        }

        public boolean isRoot() {
            return false;
        }

        public MaskLayerRow getParent() {
            return (MaskLayerRow) super.getParent();    //To change body of overridden methods use File | Settings | File Templates.
        }

        public IMaskItem getMaskItem() {
            return item;
        }

        public void setValueAt(Object o, int col) {
            switch (col) {
                case 0:
                    item.setSource((ImageLayer) o);
                    break;
                case 1:
                    //item.setPredicate((ThresholdRange) o);
                    break;
                case 2:
                    item.setGroup((Integer) o);
                    break;
                case 3:
                    item.setOperation((BinaryOperation) o);
                    break;
                case 4:
                    item.setActive((Boolean) o);
                    break;


                default:
                    throw new AssertionError();
            }
        }

        public Object getValueAt(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return item.getSource();
                case 1:
                    return item.getPredicate();
                case 2:
                    return item.getGroup();
                case 3:
                    return item.getOperation();
                case 4:
                    return item.isActive();

                default:
                    throw new AssertionError();
            }
        }
    }

    class GroupCellEditor extends AbstractComboBoxCellEditor implements ItemListener {


        private ListComboBox _comboBox;

        public GroupCellEditor() {
            super();
            setClickCountToStart(1);
        }

        public AbstractComboBox createAbstractComboBox() {
            if (_comboBox == null) {
                _comboBox = new ListComboBox();
            }

            return _comboBox;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            RowItem rowItem = (RowItem) maskTable.getRowAt(row);
            IMaskItem item = rowItem.getMaskItem();
            IMaskList maskList = rowItem.getLayer().getMaskList();

            int gnum = item.getGroup();

            if (item == maskList.getLastItem() && rowItem.isRoot()) {
                _comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum}));
            } else if (item == maskList.getLastItem() && (gnum == maskList.getMaskItem(row - 1).getGroup())) {
                _comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum, gnum + 1}));
            } else {

                _comboBox.setModel(new DefaultComboBoxModel(new Integer[]{gnum}));
            }

            return super.getTableCellEditorComponent(table, value, isSelected, row, column);

        }

        public boolean shouldSelectCell(EventObject anEvent) {
            System.out.println("should selected cell " + anEvent);
            return super.shouldSelectCell(anEvent);
        }
    }

    class OpCellEditor extends AbstractComboBoxCellEditor {

        private ListComboBox _comboBox;

        public OpCellEditor() {
            super();
            setClickCountToStart(1);
        }

        public AbstractComboBox createAbstractComboBox() {
            if (_comboBox == null) {
                _comboBox = new ListComboBox(
                        new DefaultComboBoxModel(new BinaryOperation[]{Operations.AND, Operations.OR}));
            }

            return _comboBox;
        }


        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            RowItem rowItem = (RowItem) maskTable.getRowAt(row);
            IMaskItem item = rowItem.getMaskItem();
            _comboBox.setSelectedItem(item.getOperation());
            _comboBox.setConverterContext(getConverterContext());
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }


    }

    class MaskCellEditor extends AbstractComboBoxCellEditor implements ItemListener {
        private ListComboBox _comboBox;


        public MaskCellEditor() {
            super();
            setClickCountToStart(1);
        }

        public AbstractComboBox createAbstractComboBox() {
            if (_comboBox == null) {
                _comboBox = new ListComboBox();
            }

            return _comboBox;

        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            RowItem rowItem = (RowItem) maskTable.getRowAt(row);
            IMaskItem item = rowItem.getMaskItem();
            IMaskList maskList = rowItem.getLayer().getMaskList();
            List<? extends AbstractLayer> list = maskList.getCongruentLayers(getSelectedView().getModel());
            System.out.println("num congruent layers : " + list.size());
            ComboBoxModel model = new DefaultComboBoxModel(list.toArray());
            //comboBox.setConverterContext(getConverterContext());

            _comboBox.setModel(model);
            _comboBox.setSelectedItem(item.getSource());
            _comboBox.addItemListener(this);
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);

        }


    }


}
