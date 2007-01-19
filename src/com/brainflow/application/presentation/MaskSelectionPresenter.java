package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.MaskSelectionForm;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.CheckBoxListSelectionModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 1, 2006
 * Time: 5:38:59 PM
 * To change this template use File | Settings | File Templates.
 */


public class MaskSelectionPresenter extends ImageViewPresenter {

    private MaskSelectionForm maskForm;


    public MaskSelectionPresenter() {
        maskForm = new MaskSelectionForm();
    }


    public void allViewsDeselected() {
        maskForm.getMaskList().setEnabled(false);
    }

    public void viewSelected(ImageView view) {
        populateList();
        maskForm.getMaskList().setEnabled(true);
    }

    public JComponent getComponent() {
        return maskForm;
    }

    private void populateList() {
        final CheckBoxList clist = maskForm.getMaskList();
        CheckBoxListSelectionModel selectionModel = new CheckBoxListSelectionModel();

        DefaultListModel listModel = new DefaultListModel();
        ImageView view = getSelectedView();


        if (view != null) {
            int selIndex = view.getImageDisplayModel().getSelectedIndex();
            if (selIndex > 0) {
                ImageLayer selectedLayer = view.getImageDisplayModel().getImageLayer(selIndex);
                Collection<ImageLayer> maskSet = selectedLayer.getImageLayerParameters().getAlphaMask().
                        getParameter().getAlphaMaskSet();
                int count = 0;
                for (int i = 0; i < view.getImageDisplayModel().getNumLayers(); i++) {
                    ImageLayer layer = view.getImageDisplayModel().getImageLayer(i);
                    if (layer == selectedLayer) continue;

                    listModel.addElement(layer);
                    if (maskSet.contains(layer)) {
                        selectionModel.addSelectionInterval(count, count);
                    }

                    count++;

                }
            }

            selectionModel.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    int i = e.getFirstIndex();
                    ImageView view = getSelectedView();
                    int selIndex = view.getImageDisplayModel().getSelectedIndex();
                    ImageLayer selectedLayer = view.getImageDisplayModel().getImageLayer(selIndex);


                    ImageLayer layer = (ImageLayer) clist.getModel().getElementAt(i);
                    if (clist.getCheckBoxListSelectionModel().isSelectedIndex(i)) {
                        selectedLayer.getImageLayerParameters().getAlphaMask().getParameter().addAlphaMask(layer);
                    } else {

                        selectedLayer.getImageLayerParameters().getAlphaMask().getParameter().removeAlphaMask(layer);
                    }


                }
            });


        }

        clist.setModel(listModel);
        clist.setCheckBoxListSelectionModel(selectionModel);
    }

    protected void layerSelected(ImageLayer layer) {
        if (layer != null) {
            populateList();
        } else {
            CheckBoxList clist = maskForm.getMaskList();
            clist.clearSelection();

        }
    }
}
