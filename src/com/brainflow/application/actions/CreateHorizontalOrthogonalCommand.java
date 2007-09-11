package com.brainflow.application.actions;

import com.brainflow.application.toplevel.ImageViewFactory;
import com.brainflow.core.ImageCanvas2;
import com.brainflow.core.ImageView;
import com.brainflow.core.OrthoPlotLayout;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 9, 2007
 * Time: 10:30:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateHorizontalOrthogonalCommand extends BrainFlowCommand {

    public CreateHorizontalOrthogonalCommand() {
        super("create-ortho-horizontal");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {

            ImageView sview = ImageViewFactory.createOrthogonalView(view, OrthoPlotLayout.ORIENTATION.HORIZONTAL);
            ImageCanvas2 canvas = getSelectedCanvas();

            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }

    }



}
