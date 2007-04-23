package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;
import com.brainflow.core.*;
import com.brainflow.image.anatomy.Anatomy3D;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 5:57:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateExpandedViewAction extends BasicAction {

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
        ImageCanvas2 canvas = (ImageCanvas2) getContextValue(ActionContext.SELECTED_CANVAS);

        if ( (view != null) && (canvas != null) ) {

            canvas.addImageView(ImageViewFactory.createExpandedView(view.getModel()));

        }

    }
}
