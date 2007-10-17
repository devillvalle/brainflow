package com.brainflow.application.actions;

import com.brainflow.application.IImageDataSource;
import com.brainflow.application.toplevel.DataSourceManager;
import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 1:12:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnloadImageAction extends BasicAction {


    protected void execute(ActionEvent actionEvent) throws Exception {
        IImageDataSource limg = (IImageDataSource) getContextValue(ActionContext.SELECTED_LOADABLE_IMAGE);
        if (limg != null) {
            DataSourceManager.getInstance().requestRemoval(limg);
        }

    }

}
