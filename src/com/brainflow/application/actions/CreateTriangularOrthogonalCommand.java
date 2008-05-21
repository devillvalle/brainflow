package com.brainflow.application.actions;

import com.brainflow.application.toplevel.ImageViewFactory;
import com.brainflow.core.ImageView;
import com.brainflow.core.OrthoPlotLayout;
import com.brainflow.core.IBrainCanvas;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 9, 2007
 * Time: 10:30:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateTriangularOrthogonalCommand extends BrainFlowCommand {

    public CreateTriangularOrthogonalCommand() {
        super("create-ortho-triangular");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {

            ImageView sview = ImageViewFactory.createOrthogonalView(view, OrthoPlotLayout.ORIENTATION.TRIANGULAR);
            IBrainCanvas canvas = getSelectedCanvas();

            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }

    }



}