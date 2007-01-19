package com.brainflow.application.actions;

import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 4:08:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class CloseViewAction extends BasicAction {


    protected void execute(ActionEvent actionEvent) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        ImageCanvas canvas = (ImageCanvas) getContextValue(ActionContext.SELECTED_CANVAS);

        if (view != null && canvas != null) {
            view.setVisible(false);
            canvas.remove(view);
        }


    }

}
