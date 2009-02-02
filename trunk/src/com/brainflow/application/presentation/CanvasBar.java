package com.brainflow.application.presentation;

import com.brainflow.application.actions.RotateLayersCommand;
import com.brainflow.application.presentation.binding.ExtBind;
import com.brainflow.application.toplevel.BrainFlow;
import com.brainflow.application.toplevel.DataSourceManager;
import com.brainflow.core.ImageView;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.layer.ImageLayer3D;
import com.brainflow.gui.ToggleBar;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.IImageDataSource;
import com.pietschy.command.ActionCommand;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.*;
import java.awt.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 12, 2007
 * Time: 7:36:06 AM
 */
public class CanvasBar extends ImageViewPresenter {


    private JToolBar canvasBar;

    private ToggleBar toggleBar;



    //private TransferHandler transferHandler = new CanvasBarTransferHandler();

    private MouseAdapter dragListener = new DragListener();

    private ActionCommand rotateCommand = new RotateLayersCommand();

    private JSpinner imageSpinner = new JSpinner();

    private JLabel imageSpinnerLabel = new JLabel("::");




    public CanvasBar() {
        super();

        buildGUI();


    }

    private void buildGUI() {
        canvasBar = new JToolBar();
        imageSpinner.setEnabled(false);
        imageSpinnerLabel.setEnabled(false);

        Dimension d = imageSpinner.getPreferredSize();
        d.setSize(200, d.getHeight());

        imageSpinner.setPreferredSize(d);

        JideBoxLayout layout = new JideBoxLayout(canvasBar, BoxLayout.X_AXIS);
        canvasBar.setLayout(layout);

        AbstractButton rotateButton = rotateCommand.createButton();

        canvasBar.add(imageSpinnerLabel, JideBoxLayout.FIX);
        canvasBar.add(imageSpinner, JideBoxLayout.FIX);
        canvasBar.add(new JToolBar.Separator(), JideBoxLayout.FIX);
        canvasBar.add(rotateButton, JideBoxLayout.FIX);

        toggleBar = new ToggleBar(Arrays.asList("Tabula Rasa"));
        canvasBar.add(toggleBar, JideBoxLayout.FIX);

        initSpinnerListener();
    }


    private void initSpinnerListener() {
        imageSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                String label = (String)imageSpinner.getValue();

                //todo hack cast
                final ImageLayer3D layer = (ImageLayer3D)getSelectedLayer();
                IImageDataSource dsource = layer.getDataSource();

                //todo List<ImageInfo> might be Map<String, ImageInfo> (or something?)
                List<String> labels = extractLabels(dsource.getImageInfoList());
                int index = labels.indexOf(label);

                assert index >= 0;

                final IImageDataSource dsource2 = DataSourceManager.getInstance().createDataSource(dsource.getDescriptor(), dsource.getImageInfoList(), index, true);
                //todo progress mechanism needed here

                SwingWorker worker = new SwingWorker() {
                    protected Object doInBackground() throws Exception {
                        Object ret =  dsource2.getData();
                        ImageLayer3D newlayer = new ImageLayer3D(dsource2, layer.getImageLayerProperties());
                        BrainFlow.get().replaceLayer(layer, newlayer, getSelectedView());
                        return ret;
                    }

                    @Override
                    protected void done() {
                        imageSpinner.setEnabled(true);
                    }
                };

                imageSpinner.setEnabled(false);
                worker.execute();



            }
        });

    }
    private void unbind() {
        ExtBind.get().unbind(toggleBar);
    }

    private List<String> extractLabels(List<ImageInfo> list) {

        List<String> labels = new ArrayList<String>();
        for (ImageInfo info : list) {
            labels.add(info.getImageLabel());

        }

        return labels;

    }

    private SpinnerModel createSpinnerModel(List<String> labels) {
        return new SpinnerListModel(labels);
    }

    private void updateImageSpinner() {
        ImageLayer layer = getSelectedLayer();
        if (layer.getDataSource().getImageInfoList().size() > 1) {
            imageSpinner.setEnabled(true);
            imageSpinnerLabel.setEnabled(true);

            List<String> labels = extractLabels(layer.getDataSource().getImageInfoList());
            imageSpinner.setModel(createSpinnerModel(labels));
            System.out.println(Arrays.toString(labels.toArray()));
        } else {
            imageSpinner.setModel(new SpinnerNumberModel());
            imageSpinner.setEnabled(false);
            imageSpinnerLabel.setEnabled(false);


        }

    }


    private void bind() {
        updateImageSpinner();


        ExtBind.get().bindContent(getSelectedView().getModel().getListModel(), toggleBar);
        ExtBind.get().bindToggleBar(getSelectedView().getModel().getListSelection(), toggleBar);
        
        
    }


    public void viewSelected(ImageView view) {
        bind();
    }

    public void viewDeselected(ImageView view) {
        ExtBind.get().unbind(toggleBar);
    }

    public JComponent getComponent() {
        return canvasBar;

    }

    public void allViewsDeselected() {
        unbind();
    }














    class DragListener extends MouseAdapter {


        public void mouseDragged(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler th = c.getTransferHandler();
            th.exportAsDrag(c, e, TransferHandler.MOVE);
        }


    }


}