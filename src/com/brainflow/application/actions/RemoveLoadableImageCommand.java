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

    public static final String SELECTED_DATASOURCE = "selected_datasource";

    public RemoveLoadableImageCommand() {
        getDefaultFace(true).setText("Remove");
    }

    protected void handleExecute() {
       IImageDataSource limg = (IImageDataSource) getParameter(SELECTED_DATASOURCE);
        if (limg != null) {
            DataSourceManager.getInstance().requestRemoval(limg);
        }

    }

    
}
