package com.brainflow.application.presentation;

import com.brainflow.core.*;
import com.brainflow.display.Visibility;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.CheckBoxListSelectionModel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListDataEvent;

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


        if (getSelectedView() != null) {
            visibilitySelection = new VisibilitySelection(getSelectedView());
        }

        initVisibilityModel();


        formPane = new JScrollPane(layerSelector);

        form.add(formPane, cc.xywh(2, 4, 2, 2));

        layout.addGroupedColumn(2);

    }

    private void initVisibilityModel() {
        CheckBoxListSelectionModel model = new CheckBoxListSelectionModel();
        layerSelector.setCheckBoxListSelectionModel(model);
        model.addListSelectionListener(new ListSelectionListener() {


            public void valueChanged(ListSelectionEvent e) {
                int f1 = e.getFirstIndex();
                int f2 = e.getLastIndex();
                CheckBoxListSelectionModel model = (CheckBoxListSelectionModel) e.getSource();
                ImageView view = getSelectedView();

                for (int i = f1; i <= f2; i++) {
                    AbstractLayer layer = view.getModel().getLayer(i);
                    Visibility vis = layer.getImageLayerProperties().getVisible().getProperty();

                    if (model.isSelectedIndex(i)) {

                        vis.setVisible(true);
                    } else {

                        vis.setVisible(false);
                    }
                }

            }
        });

        ImageView view = getSelectedView();
        if (view != null) {

            int n = view.getModel().getNumLayers();
            for (int i = 0; i < n; i++) {
                AbstractLayer layer = view.getModel().getLayer(i);
                if (layer.isVisible()) {
                    layerSelector.addCheckBoxListSelectedIndex(i);
                } else {
                    layerSelector.removeCheckBoxListSelectedIndex(i);
                }
            }
        }


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

            initVisibilityModel();
        }


        layerSelector.setEnabled(true);

    }

    public void layerSelected(AbstractLayer layer) {

    }

    public void intervalAdded(ListDataEvent e) {
        int index = e.getIndex0();
        ImageView view = getSelectedView();
        AbstractLayer layer = view.getModel().getLayer(index);
        boolean vis = layer.getImageLayerProperties().getVisible().getProperty().isVisible();
        if (vis) {
            layerSelector.getCheckBoxListSelectionModel().addSelectionInterval(index, index);
        } else {
            layerSelector.getCheckBoxListSelectionModel().removeSelectionInterval(index, index);

        }
    }

    public JComponent getComponent() {
        return form;
    }


    class VisibilitySelection implements ImageLayerListener {

        ImageView view;

        public VisibilitySelection(ImageView _view) {
            setImageView(_view);
        }


        public void thresholdChanged(ImageLayerEvent event) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void colorMapChanged(ImageLayerEvent event) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void opacityChanged(ImageLayerEvent event) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void interpolationMethodChanged(ImageLayerEvent event) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void visibilityChanged(ImageLayerEvent event) {
            AbstractLayer layer = event.getAffectedLayer();
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
                view.getModel().removeImageLayerListener(this);
            view = _view;
            view.getModel().addImageLayerListener(this);


        }


    }


}
