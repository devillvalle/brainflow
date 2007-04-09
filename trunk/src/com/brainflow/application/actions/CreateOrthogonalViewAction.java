package com.brainflow.application.actions;

import com.brainflow.core.*;
import com.brainflow.image.anatomy.AnatomicalVolume;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateOrthogonalViewAction extends BasicAction {


    protected void contextChanged() {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }

    protected void execute(ActionEvent actionEvent) throws Exception {
        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
            IImageDisplayModel displayModel = view.getModel();
            ImageView sview = ImageViewFactory.createOrthogonalView(displayModel);
            //ImageView sview = ImageViewFactory.createMontageView(displayModel);
            ImageCanvas2 canvas = (ImageCanvas2) getContextValue(ActionContext.SELECTED_CANVAS);

            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }

    }
}