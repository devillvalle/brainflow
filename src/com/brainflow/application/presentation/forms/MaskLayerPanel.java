package com.brainflow.application.presentation.forms;

import com.brainflow.core.*;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.layer.IMaskList;
import com.brainflow.core.layer.IMaskItem;
import com.brainflow.image.data.IImageData3D;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 24, 2007
 * Time: 10:11:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskLayerPanel extends JPanel {


    private ImageLayer layer;

    private IMaskItem maskItem;

    private ImageView sourceView;

    private ImageView currentMaskView;

    private ImageView compositeMaskView;

    private ImageView finalSourceView;

    private JTabbedPane tabbedPane;

   // "#66C2A5" "#FC8D62" "#8DA0CB" "#E78AC3" "#A6D854" "#FFD92F" "#E5C494"
   // "#B3B3B3"

    private java.util.List<Color> maskColorList = Arrays.asList(new Color(0x66C2A5), new Color(0xFC8D62), new Color(0x8DA0CB),
                                                      new Color(0xE78AC3), new Color(0xA6D854), new Color(0xFFD92F),
                                                      new Color(0xE5C494), new Color(0xB3B3B3));

    public MaskLayerPanel(IMaskItem item) {
        maskItem = item;
        layer = item.getSource();
        buildGUI();

    }

    public void setMaskItem(IMaskItem item) {
        if (item != maskItem) {
            maskItem = item;
            layer = item.getSource();
            updateGUI();
            
        }
    }


    public ImageLayer getLayer() {
        return layer;
    }

    private void updateGUI() {
        sourceView.setModel(buildSourceModel());
        currentMaskView.setModel(buildMaskModel());
        compositeMaskView.setModel(buildCompositeMaskModel());
        revalidate();
    }


    private void buildGUI() {
        sourceView = buildSourceView();
        currentMaskView = buildMaskView();
        compositeMaskView = buildCompositeMaskView();
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Masked Source", sourceView);
        tabbedPane.addTab("Active Mask", currentMaskView);
        tabbedPane.addTab("Composite Mask", compositeMaskView);
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private ImageView buildSourceView() {

        sourceView = new ImageView(buildSourceModel());
        sourceView.clearAnnotations();
        sourceView.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        return sourceView;
    }

    private ImageView buildCompositeMaskView() {
        compositeMaskView = new ImageView(buildCompositeMaskModel());
        compositeMaskView.clearAnnotations();
        compositeMaskView.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        return compositeMaskView;

    }

    private IImageDisplayModel buildSourceModel() {
        IImageDisplayModel model = new ImageDisplayModel("source model");
        model.addLayer(layer);
        return model;
    }

    private IImageDisplayModel buildMaskModel() {
        ImageLayer sourceLayer = maskItem.getSource();
        IImageData3D data = (IImageData3D) sourceLayer.getData();
        //IMaskedData3D mask = new MaskedData3D(data, maskItem.getPredicate());
        IImageDisplayModel model = new ImageDisplayModel("mask model");
        //ImageLayerProperties props = new ImageLayerProperties(new Range(0, 1), maskItem.getPredicate());

        //MaskLayer layer = new MaskLayer(maskItem, props, maskColorList.get(sourceLayer.getMaskList().indexOf(maskItem)));
        model.addLayer(layer);
        return model;

    }

    private IImageDisplayModel buildCompositeMaskModel() {
        ImageLayer sourceLayer = maskItem.getSource();
        IMaskList maskList = sourceLayer.getMaskList();
        IImageDisplayModel model = new ImageDisplayModel("composite mask model");
        for (int i=0; i < maskList.size(); i++) {
            IMaskItem item = maskList.getMaskItem(i);
           // ImageLayerProperties props = new ImageLayerProperties(new Range(0, 1), item.getPredicate());
            //props.getOpacity().setOpacity(.8);
          //  MaskLayer layer = new MaskLayer(item, props, maskColorList.get(sourceLayer.getMaskList().indexOf(item)));
            model.addLayer(layer);
            System.out.println("adding layer " + layer + " to composite mask");
        }

        return model;

    }

    private ImageView buildMaskView() {

        currentMaskView = new ImageView(buildMaskModel());
        currentMaskView.clearAnnotations();
        currentMaskView.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        return currentMaskView;
    }


}
