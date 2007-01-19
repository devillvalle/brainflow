package com.brainflow.application.actions;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.managers.LoadableImageManager;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 6, 2006
 * Time: 8:22:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveLoadableImageAction extends BasicAction {


    protected void execute(ActionEvent actionEvent) throws Exception {
        ILoadableImage limg = (ILoadableImage) getContextValue(ActionContext.SELECTED_LOADABLE_IMAGE);
        if (limg != null) {
            LoadableImageManager.getInstance().requestRemoval(limg);
        }
    }
}
