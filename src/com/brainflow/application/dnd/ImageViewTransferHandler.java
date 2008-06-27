package com.brainflow.application.dnd;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.application.toplevel.ProjectManager;
import com.brainflow.application.toplevel.ImageViewFactory;
import com.brainflow.application.toplevel.DisplayManager;
import com.brainflow.application.LoadableImageTransferable;
import com.brainflow.core.ImageView;
import com.brainflow.core.IBrainCanvas;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.image.io.IImageDataSource;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 4:32:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewTransferHandler extends ImageDropHandler {


    // need to make it so that different handler can "register"
    final static Logger log = Logger.getLogger(ImageViewTransferHandler.class.getName());


    public ImageViewTransferHandler() {
        super();
    }

    private void importDataSource(IImageDataSource dsource, TransferSupport support) {
        Component c = support.getComponent();

        if (c instanceof ImageView) {
            ImageView view = (ImageView) c;
            Brainflow.getInstance().loadAndDisplay(dsource, view);
        }

    }

    private void importImageLayer(ImageLayer layer, TransferSupport support) {
        Component c = support.getComponent();

        if (c instanceof ImageView) {
            ImageView view = (ImageView) c;
            IImageDisplayModel dset = view.getModel();
            dset.addLayer(layer);
        }

    }


    public void dispatchOnObject(Object obj, TransferSupport support) {
        if (obj instanceof IImageDataSource) {
            importDataSource((IImageDataSource) obj, support);
        } else if (obj instanceof ImageLayer) {
            importImageLayer((ImageLayer) obj, support);

        }
    }
}
