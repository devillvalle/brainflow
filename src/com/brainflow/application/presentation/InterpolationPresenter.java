package com.brainflow.application.presentation;

import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 10:45:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterpolationPresenter extends ImageViewPresenter {

    private JComboBox choices;
    private JPanel form;
    private FormLayout layout;
    private JLabel label1;

    public InterpolationPresenter() {
        buildGUI();
    }

    private void buildGUI() {
        form = new JPanel();
        layout = new FormLayout("6dlu, 70dlu:g, 1dlu, 12dlu", "8dlu, p, 5dlu, p, 15dlu");
        form.setLayout(layout);

        CellConstraints cc = new CellConstraints();

        label1 = new JLabel("Interpolation: ");
        form.add(label1, cc.xy(2, 2));

        choices = new JComboBox();

        form.add(choices, cc.xywh(2, 4, 2, 1));

        layout.addGroupedColumn(2);


    }


    public JComponent getComponent() {
        return form;
    }


    protected void layerSelected(ImageLayer layer) {

        SelectionInList sel = layer.getImageLayerParameters().getResampleSelection();
        Bindings.bind(choices, sel);

    }


    public void allViewsDeselected() {
        label1.setEnabled(false);
        choices.setEnabled(false);

    }

    public void viewSelected(ImageView view) {
        label1.setEnabled(true);
        choices.setEnabled(true);
        int idx = view.getSelectedIndex();

        if (idx > 0) {
            SelectionInList sel = view.getModel().
                    getImageLayer(idx).getImageLayerParameters().getResampleSelection();

            Bindings.bind(choices, sel);
        }


    }

}