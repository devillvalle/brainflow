package com.brainflow.application.presentation;

import com.brainflow.core.*;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.CheckBoxListSelectionModel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.java.dev.properties.binding.swing.adapters.SwingBind;

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


    private CheckBoxList layerSelector;

    private VisibilitySelection visibilitySelection;

    private JPanel form;

    private JScrollPane formPane;



    public SelectedLayerPresenter() {
        buildGUI();

    }

    private void buildGUI() {
        form = new JPanel();
        layout = new FormLayout("6dlu, 70dlu:g, 1dlu, 12dlu", "8dlu, p, 3dlu, 1dlu, max(p;55dlu), 5dlu");
        form.setLayout(layout);

        CellConstraints cc = new CellConstraints();

        layerLabel = new JLabel("Selected Layer: ");



        layerSelector = new CheckBoxList();
        initVisibilityModel();


        formPane = new JScrollPane(layerSelector);
        form.add(layerLabel, cc.xy(2, 2));
        form.add(formPane, cc.xywh(2, 4, 2, 2));
        layout.addGroupedColumn(2);

        if (getSelectedView() != null) {
            bind();
        }

    }

    private void bind() {

        visibilitySelection = new VisibilitySelection(getSelectedView());
        SwingBind.get().bindSelectionIndex(getSelectedView().getModel().getListSelection(), layerSelector);
        SwingBind.get().bindContent(getSelectedView().getModel().getListModel(), layerSelector);

        initVisibilityModel();

    }

    private void initVisibilityModel() {

        CheckBoxListSelectionModel model = new CheckBoxListSelectionModel() {

            public void insertIndexInterval(int i, int i1, boolean b) {
                //do nothing    
            }
        };


        layerSelector.setCheckBoxListSelectionModel(model);

       
        model.addListSelectionListener(new ListSelectionListener() {


            public void valueChanged(ListSelectionEvent e) {
                int f1 = e.getFirstIndex();
                int f2 = e.getLastIndex();
                CheckBoxListSelectionModel model = (CheckBoxListSelectionModel) e.getSource();
                ImageView view = getSelectedView();

                System.out.println("layer selection getValue changed !");
                for (int i = f1; i <= f2; i++) {
                    AbstractLayer layer = view.getModel().getLayer(i);
                    boolean vis = layer.getImageLayerProperties().visible.get();
                    if (model.isSelectedIndex(i)) {
                        if (!vis) { layer.getImageLayerProperties().visible.set(true); }
                    } else {
                        if (vis)  { layer.getImageLayerProperties().visible.set(false); }

                    }
                }

            }
        });

        ImageView view = getSelectedView();
        if (view != null) {
            // todo make model iterable
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

        bind();
        layerSelector.setEnabled(true);

    }

    @Override
    public void layerSelected(ImageLayer layer) {

    }

    public void intervalAdded(ListDataEvent e) {
        //System.out.println("layer selection interval added !");
        int index = e.getIndex0();
        ImageView view = getSelectedView();
        AbstractLayer layer = view.getModel().getLayer(index);
        boolean vis = layer.isVisible();
        if (vis) {
            layerSelector.getCheckBoxListSelectionModel().addSelectionInterval(index, index);
        } else {
            layerSelector.getCheckBoxListSelectionModel().removeSelectionInterval(index, index);

        }
    }

    public JComponent getComponent() {
        return form;
    }


    class VisibilitySelection extends ImageLayerListenerImpl {

        ImageView view;

        public VisibilitySelection(ImageView _view) {
            setImageView(_view);
        }

        public void setImageView(ImageView _view) {
            if (view != null)
                view.getModel().removeImageLayerListener(this);
            view = _view;
            view.getModel().addImageLayerListener(this);

        }

        public void visibilityChanged(ImageLayerEvent event) {
            ImageLayer layer = event.getAffectedLayer();
            if (layer != null) {
                int i = getSelectedView().getModel().indexOf(layer);
                boolean vis = layer.isVisible();
                if (vis) {
                    //layerSelector.addCheckBoxListSelectedIndex(i);
                    layerSelector.getCheckBoxListSelectionModel().addSelectionInterval(i, i);
                } else {
                    //layerSelector.removeCheckBoxListSelectedIndex(i);
                    layerSelector.getCheckBoxListSelectionModel().removeSelectionInterval(i, i);

                }

            }

        }

       
    }


}
