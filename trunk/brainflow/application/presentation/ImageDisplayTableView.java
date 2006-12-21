package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.ImageLayerEditor;
import com.brainflow.application.services.ImageViewSelectionEvent;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.*;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.io.BasicImageReader;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.NiftiInfoReader;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.lipstikLF.LipstikLookAndFeel;
import com.lipstikLF.theme.LipstikColorTheme;
import org.bushe.swing.event.EventBus;

import javax.media.jai.JAI;
import javax.media.jai.RenderedImageAdapter;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 31, 2006
 * Time: 12:50:52 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageDisplayTableView extends ImageViewPresenter {

    public static final int SMALL_ICON_WIDTH = 24;
    public static final int SMALL_ICON_HEIGHT = 24;

    private HierarchicalTable table;
    private LayerModel layerModel = new LayerModel();

    public void viewSelected(ImageView view) {
        layerModel.fireTableDataChanged();
    }

    public JComponent getComponent() {
        if (table == null) {
            table = createTable();
        }

        return table;
    }

    // create property table


    private HierarchicalTable createTable() {
        layerModel = new LayerModel();

        table = new HierarchicalTable() {
            public TableModel getStyleModel() {
                return layerModel;
            }
        };
        table.setModel(layerModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setExpandableColumn(-1);

        table.setSingleExpansion(false);


        table.setShowGrid(false);
        table.setRowHeight(24);
        table.getTableHeader().setPreferredSize(new Dimension(0, 0));
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setMaxWidth(50);
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageLayerCellRenderer());

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setComponentFactory(new HierarchicalTableComponentFactory() {
            public Component createChildComponent(HierarchicalTable table, Object value, int row) {
                if (value instanceof ImageLayer) {
                    ImageLayerEditor editor = new ImageLayerEditor();
                    editor.setBorder(BorderFactory.createEmptyBorder(2, 2, 3, 2));

                    editor.setBackground(UIManager.getColor("Table.selectionBackground"));
                    editor.setForeground(UIManager.getColor("Table.selectionForeground"));
                    return editor;
                }
                return null;
            }

            public void destroyChildComponent(HierarchicalTable table, Component component, int row) {
            }
        });


        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    table.expandRow(row);
                }
            }
        });


        return table;
    }


    private ImageIcon getSnapshot(ImageLayer layer, int width, int height) {


        ImageDisplayModel dmodel = new ImageDisplayModel("snapshot");
        dmodel.addLayer(layer);

        AnatomicalPoint1D point = dmodel.getImageAxis(AnatomicalVolume.getCanonicalAxial().ZAXIS).getRange().getCenter();
        SnapShooter shooter = new SnapShooter(dmodel, AnatomicalVolume.getCanonicalAxial());
        RenderedImage rimg = shooter.shoot(point.getX());
        float sx = (float) width / (float) rimg.getWidth();
        float sy = (float) height / (float) rimg.getHeight();

        ParameterBlock pb = new ParameterBlock();

        pb.add(sx);
        pb.add(sy);
        pb.addSource(rimg);

        rimg = JAI.create("scale", pb);

        RenderedImageAdapter rapter = new RenderedImageAdapter(rimg);
        ImageIcon icon = new ImageIcon(rapter.getAsBufferedImage());


        return icon;
    }


    public static void main(String[] args) {
        try {
            com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
            java.util.List installedThemes = LipstikLookAndFeel.getInstalledThemes();


            javax.swing.LookAndFeel lipstikLnF = new com.lipstikLF.LipstikLookAndFeel();
            LipstikLookAndFeel.setMyCurrentTheme((LipstikColorTheme) installedThemes.get(1));
            javax.swing.UIManager.setLookAndFeel(lipstikLnF);


            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);
            ImageDisplayModel model = new ImageDisplayModel("model");
            NiftiInfoReader niftiReader = new NiftiInfoReader();
            ImageInfo info = niftiReader.readInfo(new File("/home/surge/anyback/anova_novel2/mean_epi_mni.nii"));
            BasicImageReader breader = new BasicImageReader(info);
            IImageData data = breader.getOutput();
            System.out.println(data.getImageLabel());
            model.addLayer(new ImageLayer3D((IImageData3D) data,
                    new ImageLayerParameters(
                            new LinearColorMap(0, 5000, ColorTable.GRAYSCALE))));


            model.getSelection().setSelectionIndex(0);
            info = niftiReader.readInfo(new File("/home/surge/anyback/anova_novel2/FLag.nii"));
            breader = new BasicImageReader(info);
            data = breader.getOutput();
            model.addLayer(new ImageLayer3D((IImageData3D) data,
                    new ImageLayerParameters(
                            new LinearColorMap(0, 34, ColorTable.SPECTRUM))));


            ImageDisplayTableView tableView = new ImageDisplayTableView();
            SimpleImageView view = new SimpleImageView(model);
            EventBus.publish(new ImageViewSelectionEvent(view));


            JFrame jf = new JFrame();
            jf.add(tableView.getComponent());
            jf.pack();
            jf.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ImageLayerCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageLayer) {
                ImageLayer layer = (ImageLayer) value;
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, layer.toString(), isSelected, hasFocus, row, column);
                label.setIcon(getSnapshot(layer, SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT));
                return label;
            } else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }


    class LayerModel extends AbstractTableModel implements HierarchicalTableModel, StyleModel {

        CellStyle cellStyle = new CellStyle();

        public LayerModel() {
            cellStyle.setHorizontalAlignment(SwingConstants.TRAILING);
        }

        public int getRowCount() {
            ImageView view = ImageDisplayTableView.this.getSelectedView();
            if (view == null) return 0;

            return view.getImageDisplayModel().getNumLayers();
        }

        public int getColumnCount() {
            return 3;
        }


        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ImageLayer.class;
                case 1:
                    return String.class;
                case 2:
                    return Boolean.class;
            }

            return null;
        }

        public void update() {
            fireTableDataChanged();
        }


        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                ImageView view = ImageDisplayTableView.this.getSelectedView();
                ImageLayer layer = view.getImageDisplayModel().getImageLayer(rowIndex);
                layer.getImageLayerParameters().getVisiblility().getParameter().setVisible((Boolean) aValue);

            }
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            ImageView view = ImageDisplayTableView.this.getSelectedView();
            assert view != null;

            switch (columnIndex) {
                case 0:
                    return view.getImageDisplayModel().getImageLayer(rowIndex);
                case 1:
                    return "visible";
                case 2:
                    return view.getImageDisplayModel().getImageLayer(rowIndex).
                            getImageLayerParameters().getVisiblility().getParameter().isVisible();

            }

            return null;
        }


        public boolean isCellEditable(int row, int column) {
            if (column == 2) {
                return true;
            }

            return false;
        }

        public boolean hasChild(int row) {
            return true;
        }

        public boolean isExpandable(int row) {
            return true;
        }

        public boolean isHierarchical(int row) {
            return true;
        }

        public Object getChildValueAt(int i) {
            ImageView view = ImageDisplayTableView.this.getSelectedView();
            return view.getImageDisplayModel().getImageLayer(i);
        }

        public boolean isCellStyleOn() {
            return true;
        }


        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == 2)
                return cellStyle;

            return null;
        }


    }


}
