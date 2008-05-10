package com.brainflow.application.presentation;

import com.brainflow.core.ImageView;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageLayer3D;
import com.brainflow.image.io.IImageDataSource;
import com.brainflow.image.space.IImageSpace;
import com.jidesoft.grid.*;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.awt.*;

import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 4, 2008
 * Time: 8:36:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayerInfoPresenter extends ImageViewPresenter {

    private PropertyPane propPane = new PropertyPane();

    private PropertyTable propTable = new PropertyTable();

    private JPanel panel = new JPanel(new BorderLayout());

    public ImageLayerInfoPresenter() {
        ObjectConverterManager.initDefaultConverter();
        CellEditorManager.initDefaultEditor();

        if (getSelectedView() != null) {
            buildTable();
        }

        panel.setBorder(new EmptyBorder(2, 8, 2, 5));
        initGUI();
    }

    private PropertyTable buildTable() {
        ImageLayer layer = getSelectedLayer();
        IImageSpace space = layer.getCoordinateSpace();

        ArrayList<Property> list = new ArrayList<Property>();
        IImageDataSource source = layer.getDataSource();

        Property property = createProperty("Path", source.getDataFile(), "Data Source", FileObject.class, "The full path of the image file");
        list.add(property);

        property = createProperty("Name", source.getImageInfo().getImageLabel(), "Data Source", String.class, "The name of the image");
        list.add(property);

        property = createProperty("Index", ""+source.getImageIndex(), "Data Source", String.class, "The index of the image volume in the case of a multi-dimensional data file");
        list.add(property);

        property = createProperty("Image Format", source.getFileFormat(), "Data Source", String.class, "The image file format from which the data was loaded.");
        list.add(property);

        property = createProperty("Dimensions", space.getDimension().toString(), "Image Space", String.class, "The image data dimensions (x, y, z)");
        list.add(property);

        property = createProperty("Spacing", source.getImageInfo().getSpacing(), "Image Space", String.class, "The voxel sizes for each dimension (x, y, z)");
        list.add(property);

        property = createProperty("Data Orientation", space.getAnatomy(), "Image Space", String.class, "The anatomical orientation of the data axes (non-transformed)");
        list.add(property);

        property = createProperty("Origin", space.getOrigin().toString(), "Image Space", String.class, "The origin in world coordinates of the image data");
        list.add(property);
        
        // rotation? translation? origin?




        PropertyTableModel model = new PropertyTableModel(list);
        PropertyTable table = new PropertyTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 500));
        table.expandFirstLevel();
        table.setCellStyleProvider(new RowStripeCellStyleProvider());

        return table;


    }

    private Property createProperty(String name, Object val, String category, Class clazz, String desc) {
        Property property = new DefaultProperty();
        property.setName(name);
        property.setType(clazz);
        property.setCategory(category);
        property.setDescription(desc);
        property.setValue(val);
        property.setEditable(false);

        return property;

    }

    private void initGUI() {
        if (getSelectedView() != null) {
            propTable = buildTable();
            propPane = new PropertyPane(propTable);
            panel.removeAll();
            panel.add(propPane, BorderLayout.CENTER);
            //panel.setPreferredSize(new Dimension(400,600));

        }


    }

    public void viewSelected(ImageView view) {
        initGUI();
        panel.revalidate();
    }

    public void allViewsDeselected() {

    }

    protected void layerSelected(ImageLayer layer) {
        initGUI();
        panel.revalidate();

    }

    public JComponent getComponent() {
        return panel;
    }


}
