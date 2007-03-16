package com.brainflow.application.presentation;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.display.ImageLayerProperties;
import com.brainflow.display.Property;
import com.brainflow.display.VisibleProperty;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxList;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
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
    private JLabel label1;

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

        label1 = new JLabel("Selected Layer: ");
        form.add(label1, cc.xy(2, 2));

        layerSelector = new CheckBoxList();
        layerSelector.setBorder(BorderFactory.createEtchedBorder());

        if (getSelectedView() != null) {
            visibilitySelection = new VisibilitySelection(getSelectedView());
        }


        formPane = new JScrollPane(layerSelector);
        //formPane.setPreferredSize(form.getPreferredSize());

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

        if (layerSelector.getModel() == view.getModel().getSelection()) {
            //SingleListSelectionAdapter adapter = (SingleListSelectionAdapter)layerSelector.getSelectionModel();
            //adapter.setSelectionInterval(view.getModel().getSelectedIndex(), view.getModel().getSelectedIndex());

        } else {
            layerSelector.setSelectionModel(new DefaultListSelectionModel());
            layerSelector.setModel(view.getModel().getSelection());
            layerSelector.setSelectionModel(
                    new SingleListSelectionAdapter(
                            view.getModel().getSelection().getSelectionIndexHolder()));
        }


        layerSelector.setEnabled(true);

    }

    public void layerSelected(ImageLayer layer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return form;
    }


    class VisibilitySelection implements PropertyChangeListener {

        ImageView view;

        public VisibilitySelection(ImageView _view) {
            view = _view;


            IImageDisplayModel model = view.getModel();
            model.addListDataListener(new ListDataListener() {

                public void intervalAdded(ListDataEvent e) {
                    unlisten();
                    listen();

                }

                public void intervalRemoved(ListDataEvent e) {
                    unlisten();
                    listen();
                }

                public void contentsChanged(ListDataEvent e) {
                    unlisten();
                    listen();
                }
            });

            listen();
        }

        public void setImageView(ImageView _view) {
            unlisten();
            view = _view;
            listen();


        }

        public void propertyChange(PropertyChangeEvent evt) {

            VisibleProperty property = (VisibleProperty) evt.getSource();
            ImageLayerProperties params = property.getLayerParameters();
            ImageLayer layer = getSelectedView().getModel().getLayer(params);
            int i = getSelectedView().getModel().indexOf(layer);
            if (property.isVisible()) {
                layerSelector.getCheckBoxListSelectionModel().addSelectionInterval(i, i);
            } else {
                layerSelector.getCheckBoxListSelectionModel().removeSelectionInterval(i, i);
            }


        }


        public void listen() {
            for (int i = 0; i < view.getModel().getNumLayers(); i++) {
                Property<VisibleProperty> param = view.getModel().getImageLayer(i).getImageLayerParameters().getVisible();
                param.addPropertyChangeListener(this);

                VisibleProperty property = param.getProperty();
                if (property.isVisible()) {
                    layerSelector.getCheckBoxListSelectionModel().addSelectionInterval(i, i);
                } else {
                    layerSelector.getCheckBoxListSelectionModel().removeSelectionInterval(i, i);
                }

            }
        }

        public void unlisten() {
            for (int i = 0; i < view.getModel().getNumLayers(); i++) {
                view.getModel().getImageLayer(i).getImageLayerParameters().getVisible().removePropertyChangeListener(this);

            }

        }

        public boolean isSelectedIndex(int index) {
            IImageDisplayModel model = view.getModel();
            ImageLayer layer = model.getImageLayer(index);
            return layer.getImageLayerParameters().getVisible().getProperty().isVisible();

        }


    }


}