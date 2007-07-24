package com.brainflow.application.actions;

import com.brainflow.application.toplevel.ImageViewFactory;
import com.brainflow.core.ImageCanvas2;
import com.brainflow.core.ImageView;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:42:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateCoronalViewCommand extends BrainFlowCommand {

    public CreateCoronalViewCommand() {
        super("create-coronal");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();

        if (view != null) {

            ImageView sview = ImageViewFactory.createYokedCoronalView(view);

            ImageCanvas2 canvas = getSelectedCanvas();

            if (canvas != null) {
                canvas.addImageView(sview);
            }
        }


    }


}