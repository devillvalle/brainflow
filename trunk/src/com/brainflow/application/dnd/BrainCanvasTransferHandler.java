package com.brainflow.application.dnd;

import com.brainflow.application.toplevel.*;
import com.brainflow.core.ImageView;
import com.brainflow.core.IBrainCanvas;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.image.io.IImageDataSource;

import java.awt.*;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 4:32:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrainCanvasTransferHandler extends ImageDropHandler {


    // need to make it so that different handler can "register"
    private static final Logger log = Logger.getLogger(BrainCanvasTransferHandler.class.getName());


    public BrainCanvasTransferHandler() {
        super();


    }


    private void importDataSource(IImageDataSource dsource, TransferSupport support) {
        Component c = support.getComponent();

        if (c instanceof IBrainCanvas) {
            Point p = support.getDropLocation().getDropPoint();

            IBrainCanvas canvas = (IBrainCanvas) c;
            ImageView view = canvas.whichView(c, p);

            if (view != null) {
                BrainFlow.getInstance().loadAndDisplay(dsource, view);
            } else {

                BrainFlow.getInstance().loadAndDisplay(dsource);
            }

        }

    }

    private void importImageLayer(ImageLayer layer, TransferSupport support) {
        Component c = support.getComponent();
        if (c instanceof IBrainCanvas) {
            Point p = support.getDropLocation().getDropPoint();


            IBrainCanvas canvas = (IBrainCanvas) c;
            ImageView view = canvas.whichView(c, p);

            if (view == null) {
                IImageDisplayModel model = ProjectManager.getInstance().addToActiveProject(layer);
                DisplayManager.getInstance().displayView(ImageViewFactory.createAxialView(model));
            } else {
                view.getModel().addLayer(layer);
            }


        }


    }

    @Override
    public void dispatchOnObject(Object obj, TransferSupport support) {
        if (obj instanceof IImageDataSource) {
            importDataSource((IImageDataSource) obj, support);
        } else if (obj instanceof ImageLayer) {
            importImageLayer((ImageLayer) obj, support);

        }

    }


    public static void main(String[] args) {
        
    }


}


