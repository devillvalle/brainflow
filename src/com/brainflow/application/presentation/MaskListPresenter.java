package com.brainflow.application.presentation;

import com.brainflow.core.*;
import com.jidesoft.grid.*;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 1, 2007
 * Time: 12:34:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskListPresenter extends ImageViewPresenter {

    protected static final Color BG1 = new Color(232, 237, 230);
    protected static final Color BG2 = new Color(243, 234, 217);
    protected static final Color BG3 = new Color(214, 231, 247);


    private HashMap<ImageMaskList, MaskListEditor> editorMap = new HashMap<ImageMaskList, MaskListEditor>();

    private HierarchicalTable hierTable;

    private ImageLayerTableModel layerModel;

    private ListSelectionModelGroup group = new ListSelectionModelGroup();

    private JPanel form;


    public MaskListPresenter() {
        //editor = new MaskListEditor(new ImageDisplayModel(""), new ImageMaskList());
        hierTable = new HierarchicalTable();
        layerModel = new ImageLayerTableModel();
        hierTable.setModel(layerModel);
        hierTable.setBackground(BG1);
        hierTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        hierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //hierTable.getTableHeader().setVisible(false);
        CellRendererManager.initDefaultRenderer();
        hierTable.setDefaultCellRenderer(new LayerCellRenderer());
        CellRendererManager.registerRenderer(AbstractLayer.class, new LayerCellRenderer());
        CellRendererManager.registerRenderer(ImageLayer3D.class, new LayerCellRenderer());
        CellRendererManager.registerRenderer(ImageLayer.class, new LayerCellRenderer());


        hierTable.setComponentFactory(new HierarchicalTableComponentFactory() {
            public Component createChildComponent(HierarchicalTable hierarchicalTable, Object o, int i) {
                //MaskListEditor editor = editorMap.get(o);
                ImageMaskList list = (ImageMaskList) o;
                MaskListEditor editor = editorMap.get(list);
                if (editor == null) {
                    editor = new MaskListEditor(getSelectedView().getModel(), list);
                }

                editor.getTable().setOpaque(true);
                editor.getTable().setBorder(BorderFactory.createEmptyBorder());
                editor.getTable().setBackground(BG2);
                TreeLikeHierarchicalPanel treeLikeHierarchicalPanel = new TreeLikeHierarchicalPanel(new FitScrollPane(editor.getTable()));

                //treeLikeHierarchicalPanel.setBackground(editor.getTable().getBackground());
                //treeLikeHierarchicalPanel.setBackground(BG2);

                group.add(editor.getTable().getSelectionModel());


                treeLikeHierarchicalPanel.setBorder(BorderFactory.createEmptyBorder());
                return treeLikeHierarchicalPanel;

            }

            public void destroyChildComponent(HierarchicalTable hierarchicalTable, Component component, int i) {
                Component t = JideSwingUtilities.getFirstChildOf(JTable.class, component);
                if (t instanceof JTable) {
                    group.remove(((JTable) t).getSelectionModel());
                }


            }
        });


        group.add(hierTable.getSelectionModel());


        hierTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                int f1 = e.getFirstIndex();
                int f2 = e.getLastIndex();
                ImageView view = getSelectedView();

                ListSelectionModel model = (ListSelectionModel) e.getSource();

                if (model == hierTable.getSelectionModel()) {
                    for (int i = f1; i <= f2; i++) {
                        if (model.isSelectedIndex(i)) {
                            view.setSelectedLayerIndex(i);

                        }
                    }
                } 
            }

        });



        form = new JPanel();
        form.setLayout(new JideBoxLayout(form, JideBoxLayout.Y_AXIS));

        form.add(createToolBar(), JideBoxLayout.FIX);
        form.add(new JScrollPane(hierTable), JideBoxLayout.VARY);




    }


    private JToolBar createToolBar() {

        JToolBar tbar;

        try {
            tbar = new JToolBar();
            BufferedImage icon = ImageIO.read(getClass().getClassLoader().getResource("resources/icons/add.png"));
            Action addAction = new AddItemAction("add mask", new ImageIcon(icon));
            tbar.add(addAction);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        return tbar;



    }

    protected void layerSelected(AbstractLayer layer) {
        IImageDisplayModel model = getSelectedView().getModel();

        int idx = model.getSelectedIndex();
        hierTable.getSelectionModel().setSelectionInterval(idx, idx);
    }

    public void allViewsDeselected() {
        hierTable.setEnabled(false);
    }

    public void viewSelected(ImageView view) {
        hierTable.setEnabled(true);
        layerModel.fireTableDataChanged();
        //hierTable.revalidate();
        //editor.setModel(view.getModel());
    }

    public JComponent getComponent() {
        return form;
    }

    class LayerCellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            AbstractLayer layer = (AbstractLayer) value;
            String text = "Layer " + (row + 1) + " : " + layer.toString();
            label.setText(text);

            return label;


        }
    }


    class ImageLayerTableModel extends DefaultTableModel implements HierarchicalTableModel {
        public Object getChildValueAt(int i) {
            ImageView view = getSelectedView();
            AbstractLayer layer = view.getModel().getLayer(i);
            if (layer instanceof ImageLayer) {
                return ((ImageLayer) layer).getMaskList();
            } else {

                return null;
            }

        }

        public String getColumnName(int column) {
            return "Layer";
        }

        public int getColumnCount() {
            return 1;
        }

        public int getRowCount() {
            if (getSelectedView() == null) {
                return 0;
            } else {
                return getSelectedView().getModel().getNumLayers();
            }
        }

        public Object getValueAt(int row, int column) {
            ImageView view = getSelectedView();
            return view.getModel().getLayer(row);
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public boolean hasChild(int i) {
            return true;
        }

        public boolean isExpandable(int i) {
            return true;
        }

        public boolean isHierarchical(int i) {
            return true;
        }
    }

    static class FitScrollPane extends JScrollPane implements ComponentListener {
        public FitScrollPane() {
            initScrollPane();
        }

        public FitScrollPane(Component view) {
            super(view);
            initScrollPane();
        }

        public FitScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
            super(view, vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        public FitScrollPane(int vsbPolicy, int hsbPolicy) {
            super(vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        private void initScrollPane() {
            //setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setBorder(BorderFactory.createEmptyBorder());
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            getViewport().getView().addComponentListener(this);
            removeMouseWheelListeners();
        }

        // remove MouseWheelListener as there is no need for it in FitScrollPane.
        private void removeMouseWheelListeners() {
            MouseWheelListener[] listeners = getMouseWheelListeners();
            for (int i = 0; i < listeners.length; i++) {
                MouseWheelListener listener = listeners[i];
                removeMouseWheelListener(listener);
            }
        }

        public void updateUI() {
            super.updateUI();
            removeMouseWheelListeners();
        }

        public void componentResized(ComponentEvent e) {
            setSize(getSize().width, getPreferredSize().height);
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentHidden(ComponentEvent e) {
        }

        public Dimension getPreferredSize() {
            getViewport().setPreferredSize(getViewport().getView().getPreferredSize());
            return super.getPreferredSize();
        }
    }

    public static void scrollRectToVisible(Component component, Rectangle aRect) {
        Container parent;
        int dx = component.getX(), dy = component.getY();

        for (parent = component.getParent();
             parent != null && (!(parent instanceof JViewport) || (((JViewport) parent).getClientProperty("HierarchicalTable.mainViewport") == null));
             parent = parent.getParent()) {
            Rectangle bounds = parent.getBounds();

            dx += bounds.x;
            dy += bounds.y;
        }

        if (parent != null) {
            aRect.x += dx;
            aRect.y += dy;

            ((JComponent) parent).scrollRectToVisible(aRect);
            aRect.x -= dx;
            aRect.y -= dy;
        }
    }




    class MaskItemRow extends DefaultExpandableRow {
        public Object getValueAt(int i) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        
    }


    class AddItemAction extends AbstractAction {

        public AddItemAction(String name, Icon icon) {
            super(name, icon);
        }

        public void actionPerformed(ActionEvent e) {

            if (hierTable.getSelectedRow() < 0) {
                System.out.println("slected row : " + hierTable.getSelectedRow());
                return;
            }
            
            System.out.println("selected row : " + hierTable.getSelectedRow());
            Class c = hierTable.getModel().getValueAt(hierTable.getSelectedRow(), hierTable.getSelectedColumn()).getClass();
            System.out.println("class : " + c.getCanonicalName());
        }
    }

}
