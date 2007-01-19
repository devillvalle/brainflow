package com.brainflow.application.presentation;

import com.brainflow.application.CompositeFileSelector;
import com.brainflow.application.LoadableImageProvider;
import com.brainflow.application.SoftLoadableImage;
import com.brainflow.application.managers.ImageIOManager;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.gui.FileExplorer;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 11:16:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageFileExplorer extends AbstractPresenter implements TreeSelectionListener, LoadableImageProvider, MouseListener, MouseMotionListener {

    private FileExplorer explorer;


    private FileSelector selector;

    public ImageFileExplorer(FileObject _rootObject) {


        selector = new CompositeFileSelector(ImageIOManager.getInstance().descriptorArray());


        explorer = new FileExplorer(_rootObject, selector) {
            protected DefaultMutableTreeNode createTreeNode(FileObject fobj) {
                return new ImageFileObjectNode(fobj, selector);
            }

        };


        explorer.addTreeSelectionListener(this);


    }

    public TreeModel getTreeModel() {
        return explorer.getJTree().getModel();

    }

    public JComponent getComponent() {
        return explorer.getJTree();
    }

    public JTree getJTree() {
        return explorer.getJTree();

    }

    public void addFileRoot(FileObject fobj) {
        explorer.addFileRoot(fobj);

    }

    public void valueChanged(TreeSelectionEvent e) {
        requestLoadableImages();
    }

    public void setDragEnabled(boolean e) {
        if (e) {
            explorer.getJTree().setDragEnabled(true);
            explorer.getJTree().addMouseListener(this);
            explorer.getJTree().addMouseMotionListener(this);
        } else {
            explorer.getJTree().setDragEnabled(false);
            explorer.getJTree().removeMouseListener(this);
            explorer.getJTree().removeMouseMotionListener(this);
        }
    }


    public FileSelector getSelector() {
        return selector;
    }

    public void setTransferHandler(TransferHandler handler) {
        explorer.getJTree().setTransferHandler(handler);
    }

    public SoftLoadableImage[] requestLoadableImages() {
        TreePath[] paths = explorer.getJTree().getSelectionPaths();
        if (paths == null) return new SoftLoadableImage[0];
        List<SoftLoadableImage> list = new ArrayList<SoftLoadableImage>();
        for (int p = 0; p < paths.length; p++) {
            Object[] obj = paths[p].getPath();

            for (int i = 0; i < obj.length; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj[i];
                if (node.isLeaf()) {
                    Object userObject = node.getUserObject();
                    if (userObject instanceof SoftLoadableImage) {
                        list.add((SoftLoadableImage) userObject);

                    }
                }
            }
        }

        SoftLoadableImage[] ret = new SoftLoadableImage[list.size()];
        list.toArray(ret);
        return ret;
    }

    MouseEvent firstMouseEvent = null;

    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        firstMouseEvent = e;
        e.consume();
    }

    public void mouseDragged(MouseEvent e) {

        if (firstMouseEvent != null) {
            e.consume();
            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
            //Arbitrarily define a 5-pixel shift as the
            //official beginning of a drag.
            if (dx > 5 || dy > 5) {

                //This is a drag, not a click.
                JComponent c = (JComponent) e.getSource();
                //Tell the transfer handler to initiate the drag.
                TransferHandler handler = c.getTransferHandler();
                handler.exportAsDrag(c, firstMouseEvent, -1);
                firstMouseEvent = null;
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        firstMouseEvent = null;
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public static class ImageFileObjectNode extends DefaultMutableTreeNode {

        private boolean areChildrenDefined = false;
        private boolean leaf;
        private FileObject fileObject;
        private FileSelector selector;

        public ImageFileObjectNode(FileObject _fobj, FileSelector _selector) {
            selector = _selector;
            fileObject = _fobj;

            try {
                if (fileObject.getType() == FileType.FOLDER) {
                    leaf = false;
                } else {
                    leaf = true;
                }
            } catch (FileSystemException e) {
                throw new RuntimeException(e);
            }

        }


        public FileObject getFileObject() {
            return fileObject;
        }

        public boolean isLeaf() {
            return leaf;
        }

        public boolean areChildrenDefined() {
            return areChildrenDefined;
        }

        public int getChildCount() {
            /*int length = 0;
            try {
                length = fileObject.getChildren().length;
            } catch(FileSystemException e) {
                throw new RuntimeException(e);
            }

            return length; */

            if (!areChildrenDefined()) {
                defineChildNodes();
            }

            return super.getChildCount();

        }


        private void defineChildNodes() {
            areChildrenDefined = true;
            System.out.println("leaf: " + isLeaf() + " defining child nodes for: " + fileObject.getName().getPath());
            try {

                FileObject[] children = fileObject.findFiles(selector);
                SoftLoadableImage[] limgs = ImageIOManager.getInstance().findLoadableImages(children);

                // first add folders
                for (int i = 0; i < children.length; i++) {
                    if (children[i].getType() == FileType.FOLDER) {
                        System.out.println("adding folder node " + children[i].getName().getPath());
                        ImageFileObjectNode node = new ImageFileObjectNode(children[i], selector);
                        add(node);
                    }
                }

                // then add confirmed loadable images
                for (int i = 0; i < limgs.length; i++) {
                    ImageFileObjectNode node = new ImageFileObjectNode(limgs[i].getHeaderFile(), selector);
                    node.setUserObject(limgs[i]);
                    add(node);
                }
            } catch (FileSystemException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            if (this == getRoot()) {
                try {
                    URI uri = new URI(fileObject.getName().getURI());

                    return uri.getHost() + ":" + uri.getPath();
                } catch (URISyntaxException e) {
                    return fileObject.getName().getURI();
                }
            } else
                return fileObject.getName().getBaseName();
        }
    }


}

