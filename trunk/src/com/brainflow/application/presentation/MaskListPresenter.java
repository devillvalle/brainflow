package com.brainflow.application.presentation;

import com.brainflow.core.ImageView;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.MaskList;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 1, 2007
 * Time: 12:34:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskListPresenter extends ImageViewPresenter {

    private MaskListEditor editor;

    public MaskListPresenter() {
        editor = new MaskListEditor(new ImageDisplayModel(""), new MaskList());
    }

    public void allViewsDeselected() {
        editor.setEnabled(false);
    }

    public void viewSelected(ImageView view) {
        editor.setEnabled(true);
        editor.setModel(view.getModel());
    }

    public JComponent getComponent() {
        return editor;
    }
}
