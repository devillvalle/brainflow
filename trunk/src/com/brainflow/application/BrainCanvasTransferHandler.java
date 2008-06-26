package com.brainflow.application;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.core.ImageView;
import com.brainflow.core.IBrainCanvas;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.image.io.IImageDataSource;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
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
public class BrainCanvasTransferHandler extends TransferHandler {


    // need to make it so that different handler can "register"
    private static final Logger log = Logger.getLogger(BrainCanvasTransferHandler.class.getName());

    private List<DataFlavor> dropFlavors;

    public BrainCanvasTransferHandler() {
        try {
            DataFlavor loadableImageFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=com.brainflow.image.io.IImageDataSource");

            DataFlavor imageLayerFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=com.brainflow.core.layer.ImageLayer");

            dropFlavors = new ArrayList<DataFlavor>();
            dropFlavors.add(loadableImageFlavor);
            dropFlavors.add(imageLayerFlavor);



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void importDataSource(IImageDataSource dsource) {

    }

    private void importImageLayer(ImageLayer layer) {

    }

    public boolean importData(TransferSupport support) {
        System.out.println("import data!");
        try {
            IImageDataSource limg = null;

            if (canImport(support)) {
                try {
                    limg = (IImageDataSource) support.getTransferable().getTransferData(dropFlavors.get(0));
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

           

            Component c = support.getComponent();

            if (c instanceof IBrainCanvas) {
                Point p = support.getDropLocation().getDropPoint();

                IBrainCanvas canvas = (IBrainCanvas) c;
                ImageView view = canvas.whichView(c, p);
                if (limg == null) {
                    return false;

                }
                if (view != null) {
                    Brainflow.getInstance().loadAndDisplay(limg, view);
                }
                else {
                    Brainflow.getInstance().loadAndDisplay(limg);
                }

            } else if (c instanceof ImageView) {
                log.info("importing data on to component: c.getClass()");
            } else {
                log.info("unrecognized component");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return true;

    }


    public boolean canImport(TransferSupport support) {

        DataFlavor[] flavors = support.getDataFlavors();

        for (int i = 0; i < flavors.length; i++) {

            Class c = flavors[i].getRepresentationClass();
            for (DataFlavor f : dropFlavors) {
                if (f.getRepresentationClass().isAssignableFrom(c)) {
                    System.out.println("can import is true!");
                    return true;

                }
            }

        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println("" + Number.class.isAssignableFrom(Double.class));
        System.out.println("" + Double.class.isAssignableFrom(Number.class));

    }




}


