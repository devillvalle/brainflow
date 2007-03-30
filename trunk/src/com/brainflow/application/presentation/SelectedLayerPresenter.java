package com.brainflow.application.presentation;

import com.brainflow.core.*;
import com.brainflow.display.ImageLayerProperties;
import com.brainflow.display.Property;
import com.brainflow.display.Visibility;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.CheckBoxListSelectionModel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 1, 2006
 * Time: 10:27:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectedLayerPresenter extends ImageViewPresenter {


    private FormLayout layout;

    private JLabel layerLabel;

    //private JComboBox layerSelector;
    private CheckBoxList layerSelector;

    private VisibilitySelection visibilitySelection;

    private JPanel form;
    private JScrollPane formPane;

    private ComboBoxAdapter adapter;

    public SelectedLayerPresenter() {
        buildGUI();

    }

    private void buildGUI() {
        form = new JPanel();
        layout = new FormLayout("6dlu, 70dlu:g, 1dlu, 12dlu", "8dlu, p, 3dlu, 1dlu, max(p;55dlu), 5dlu");
        form.setLayout(layout);

        CellConstraints cc = new CellConstraints();

        layerLabel = new JLabel("Selected Layer: ");
        form.add(layerLabel, cc.xy(2, 2));

        layerSelector = new CheckBoxList();

        layerSelector.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {


            public void valueChanged(ListSelectionEvent e) {
                int i = e.getFirstIndex();
                CheckBoxListSelectionModel model = (CheckBoxListSelectionModel)e.getSource();
                ImageView view = getSelectedView();
                ImageLayer layer = view.getModel().getImageLayer(i);
                Visibility vis = layer.getImageLayerParameters().getVisible().getProperty();
                if (model.isSelectedIndex(i)) {
                    vis.setVisible(true);
                } else {
                    vis.setVisible(false);
                }

            }
        });

        if (getSelectedView() != null) {
            visibilitySelection = new VisibilitySelection(getSelectedView());
        }


        formPane = new JScrollPane(layerSelector);

        form.add(formPane, cc.xywh(2, 4, 2, 2));

        layout.addGroupedColumn(2);


    }


    public void allViewsDeselected() {
        layerSelector.setEnabled(false);

    }



    public void viewSelected(ImageView view) {

        if (visibilitySelection == null) {
            visibilitySelection = new VisibilitySelection(view);
        } else {
            visibilitySelection.setImageView(view);

        }

        //assert view.getModel().getSelectedIndex() >= 0;
        //layerSelector.clearSelection();

        if (layerSelector.getModel() != view.getModel().getSelection()) {
            layerSelector.setSelectionModel(new DefaultListSelectionModel());
            layerSelector.setModel(view.getModel().getSelection());
            layerSelector.setSelectionModel(
                    new SingleListSelectionAdapter(
                            view.getModel().getSelection().getSelectionIndexHolder()));

            int n = view.getModel().getNumLayers();
            for (int i =0; i<n; i++) {
                ImageLayer layer = view.getModel().getImageLayer(i);
                if (layer.isVisible()) {
                    layerSelector.addCheckBoxListSelectedIndex(i);
                } else {
                    layerSelector.removeCheckBoxListSelectedIndex(i);
                }
            }
        }


        layerSelector.setEnabled(true);

    }

    public void layerSelected(ImageLayer layer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return form;
    }


    class VisibilitySelection implements LayerChangeListener  {

        ImageView view;

        public VisibilitySelection(ImageView _view) {
            setImageView(_view);
        }


        public void layerChanged(LayerChangeEvent event) {
            ImageLayer layer = event.getAffectedLayer();
            if (layer != null) {
                int i = getSelectedView().getModel().indexOf(layer);
                boolean vis = layer.isVisible();
                if (vis) {
                    layerSelector.getCheckBoxListSelectionModel().addSelectionInterval(i, i);
                } else {
                    layerSelector.getCheckBoxListSelectionModel().removeSelectionInterval(i, i);

                }

            }
        }

        public void setImageView(ImageView _view) {
            if (view != null)
                view.getModel().removeLayerChangeListener(this);
            view = _view;
            view.getModel().addLayerChangeListener(this);


        }

      


    }


}
