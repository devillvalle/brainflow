package com.brainflow.application;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageView;

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
public class ImageCanvasTransferHandler extends TransferHandler {


    // need to make it so that different handler can "register"
    private final static Logger log = Logger.getLogger(ImageCanvasTransferHandler.class.getName());
    DataFlavor loadableImageFlavor;

    public ImageCanvasTransferHandler() {
        try {
            loadableImageFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=com.brainflow.application.ILoadableImage");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        super.exportAsDrag(comp, e, action);


    }

    public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
        super.exportToClipboard(comp, clip, action);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean importData(TransferSupport support) {
        try {
            ILoadableImage[] limg = null;
            if (canImport(support)) {
                try {
                    limg = (ILoadableImage[]) support.getTransferable().getTransferData(loadableImageFlavor);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            assert limg != null;

            Component c = support.getComponent();

            if (c instanceof ImageCanvas) {
                Point p = support.getDropLocation().getDropPoint();

                ImageCanvas canvas = (ImageCanvas) c;
                ImageView view = canvas.whichView(c, p);
                if (view != null)
                    Brainflow.getInstance().loadAndDisplay(limg[0], view, true);
                else {
                    Brainflow.getInstance().loadAndDisplay(limg[0]);
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
            if (flavors[i].equals(loadableImageFlavor)) {
                return true;
            }
        }

        return false;
    }


    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    public Icon getVisualRepresentation(Transferable t) {
        return super.getVisualRepresentation(t);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Transferable createTransferable(JComponent c) {
        log.info("creating Transferable");

        if (c instanceof JTree) {
            JTree tree = (JTree) c;
            TreePath path = tree.getSelectionPath();
            Object[] obj = path.getPath();
            List<SoftLoadableImage> list = new ArrayList<SoftLoadableImage>();
            for (int i = 0; i < obj.length; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj[i];
                if (node.isLeaf()) {
                    Object userObject = node.getUserObject();
                    if (userObject instanceof SoftLoadableImage) {
                        list.add((SoftLoadableImage) userObject);

                    }
                }
            }

            ILoadableImage[] ret = new ILoadableImage[list.size()];
            list.toArray(ret);


            return new LoadableImageTransferable(ret);
        }

        return null;

    }


    protected void exportDone(JComponent source, Transferable data, int action) {
        log.info("export done ..");

    }
}


