package com.brainflow.application.actions;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.application.toplevel.DataSourceManager;


import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 6, 2006
 * Time: 8:22:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveLoadableImageCommand extends BrainFlowCommand {

    public RemoveLoadableImageCommand() {
        getDefaultFace(true).setText("Remove");
    }

    protected void handleExecute() {
        Object o = getParameter(ActionContext.SELECTED_LOADABLE_IMAGE);
        System.out.println("loadable image? " + o);
        //super.getSelectedView()

        IImageDataSource limg = (IImageDataSource) getParameter(ActionContext.SELECTED_LOADABLE_IMAGE);
        if (limg != null) {
            DataSourceManager.getInstance().requestRemoval(limg);
        }

    }

    
}
