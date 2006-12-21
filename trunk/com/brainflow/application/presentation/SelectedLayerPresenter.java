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
 * Date: Aug 1, 2006
 * Time: 10:27:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectedLayerPresenter extends ImageViewPresenter {


    private FormLayout layout;
    private JLabel label1;

    private JComboBox layerSelector;
    private JPanel form;


    public SelectedLayerPresenter() {
        buildGUI();

    }

    private void buildGUI() {
        form = new JPanel();
        layout = new FormLayout("6dlu, 70dlu:g, 1dlu, 12dlu", "8dlu, p, 5dlu, p, 15dlu");
        form.setLayout(layout);

        CellConstraints cc = new CellConstraints();

        label1 = new JLabel("Selected Layer: ");
        form.add(label1, cc.xy(2, 2));

        layerSelector = new JComboBox();

        form.add(layerSelector, cc.xywh(2, 4, 2, 1));
        layout.addGroupedColumn(2);


    }

    public void viewSelected(ImageView view) {
        if (view == null) {
            Bindings.bind(layerSelector, new SelectionInList());
        } else {
            Bindings.bind(layerSelector, view.getImageDisplayModel().getSelection());

        }
    }

    public void selectedLayerChanged(ImageLayer layer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return form;
    }


}
