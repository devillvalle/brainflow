package com.brainflow.application.actions;

import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageViewFactory;
import com.brainflow.core.ImageCanvas2;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateSagittalViewAction extends BasicAction {


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
            //IImageDisplayModel displayModel = view.getModel();
            ImageView sview = ImageViewFactory.createYokedSagittalView(view);
            ImageCanvas2 canvas = (ImageCanvas2) getContextValue(ActionContext.SELECTED_CANVAS);

            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }

    }
}
