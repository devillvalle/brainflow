package com.brainflow.application.actions;

import com.brainflow.application.toplevel.ImageViewFactory;
import com.brainflow.core.ImageView;
import com.brainflow.core.IBrainCanvas;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateAxialViewCommand extends BrainFlowCommand {

    public CreateAxialViewCommand() {
        super("create-axial");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {

            ImageView sview = ImageViewFactory.createAxialView(view);

            IBrainCanvas canvas = getSelectedCanvas();

            if (canvas != null) {
                canvas.addImageView(sview);
                canvas.getImageCanvasModel().yoke(sview, view);
            }
        }


    }


}
