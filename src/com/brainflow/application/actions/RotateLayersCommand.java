package com.brainflow.application.actions;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageView;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 18, 2007
 * Time: 12:35:48 PM
 */
public class RotateLayersCommand extends BrainFlowCommand {

    public RotateLayersCommand() {
        super("rotate-layers");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null && view.getModel().getNumLayers() > 1) {
            IImageDisplayModel model = view.getModel();

            //model.swapLayers(model.getNumLayers() -1, );
        }
    }
}
