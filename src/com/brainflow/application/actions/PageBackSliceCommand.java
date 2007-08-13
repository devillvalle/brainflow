package com.brainflow.application.actions;

import com.brainflow.core.ImageView;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 29, 2007
 * Time: 12:36:45 AM
 * To change this template use File | Settings | File Templates.
 */


public class PageBackSliceCommand extends BrainFlowCommand {


    public PageBackSliceCommand() {
        super("page-back-slice");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {
            view.getSliceController().pageBack();
        }


    }


}