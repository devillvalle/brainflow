package com.brainflow.application.actions;

import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.display.SmoothingOp;
import com.jidesoft.dialog.JideOptionPane;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 18, 2006
 * Time: 1:24:21 AM
 * To change this template use File | Settings | File Templates.
 */


public class SmoothingAction extends BasicAction {


    protected void execute(ActionEvent actionEvent) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            int idx = view.getSelectedIndex();
            ImageLayer layer = view.getImageDisplayModel().getImageLayer(idx);


            String result = JideOptionPane.showInputDialog(view, "Smoothing Radius (FWHM): ", 0);

            float fwhm = 0.0f;

            try {
                fwhm = Float.parseFloat(result);
            } catch (NumberFormatException e) {
                e.printStackTrace();

            }

            layer.getImageLayerParameters().addImageOp(SmoothingOp.OP_NAME, new SmoothingOp(fwhm));

        }


    }


}
