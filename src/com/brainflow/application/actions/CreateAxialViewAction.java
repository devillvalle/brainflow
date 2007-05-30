package com.brainflow.application.actions;

import com.brainflow.core.*;
import com.brainflow.core.annotations.CrosshairAnnotation;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateAxialViewAction extends BasicAction {


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
            
            ImageView sview = ImageViewFactory.createYokedAxialView(view);
            System.out.println("selected view: " + sview);
            ImageCanvas2 canvas = (ImageCanvas2) getContextValue(ActionContext.SELECTED_CANVAS);
            System.out.println("selected canvas: " + canvas);
            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }

    }
}