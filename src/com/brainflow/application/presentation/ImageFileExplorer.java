package com.brainflow.application.presentation;

import com.brainflow.application.*;
import com.brainflow.application.toplevel.ImageIOManager;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.gui.FileExplorer;
import com.brainflow.utils.ResourceLoader;
import com.brainflow.image.io.ImageInfo;
import com.jidesoft.tree.AbstractTreeModel;
import com.jidesoft.tree.DefaultTreeModelWrapper;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.jvnet.substance.SubstanceDefaultTreeCellRenderer;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 19, 2004
 * Time: 11:16:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageFileExplorer extends AbstractPresenter implements TreeSelectionListener, LoadableImageProvider, MouseListener, MouseMotionListener {


    private static final Logger log = Logger.getLogger(ImageFileExplorer.class.getName());

    private FileExplorer explorer;

    private FileSelector selector;

    private ImageIcon folderIcon = new ImageIcon(ResourceLoader.getResource("resources/icons/folder.png"));

    private ImageIcon folderOpenIcon = new ImageIcon(ResourceLoader.getResource("resources/icons/folderOpen.png"));


    public ImageFileExplorer(FileObject _rootObject) {


        selector = new CompositeFileSelector(ImageIOManager.getInstance().descriptorArray());


        explorer = new FileExplorer(_rootObject, selector) {
            protected DefaultMutableTreeNode createTreeNode(FileObject fobj) {
                return new ImageFileObjectNode(fobj, selector);
            }

        };

        explorer.addTreeExpansionListener(new TreeExpansionListener() {

            public void treeExpanded(TreeExpansionEvent event) {
                ImageFileObjectNode node = (ImageFileObjectNode) event.getPath().getLastPathComponent();
                if (!node.areChildrenDefined()) {
                    ImageNodeWorker worker = new ImageNodeWorker(node);
                    try {
                        worker.execute();
                    } catch (Exception e) {
                        log.severe("Error encountered discovering file nodes in ImageNodeWorker thread");
                        throw new RuntimeException(e);
                    }


                }

            }

            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        explorer.getJTree().setCellRenderer(new SubstanceDefaultTreeCellRenderer() {


            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

                if (value instanceof ImageFileObjectNode) {
                    ImageFileObjectNode node = (ImageFileObjectNode) value;
                    try {
                        //System.out.println("node type = " + node.getFileObject().getType());
                        if (node.getFileObject().getType() == FileType.FOLDER && !expanded) {
                            label.setIcon(folderIcon);
                        } else if (node.getFileObject().getType() == FileType.FOLDER && expanded) {
                            label.setIcon(folderOpenIcon);
                        } else {
                            //label.setIcon(leafIcon);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);

                    }


                    Object userObject = node.getUserObject();
                    if (userObject instanceof IImageDataSource) {
                        IImageDataSource limg = (IImageDataSource) userObject;
                        if (limg.isLoaded() && !selected) {
                            label.setForeground(Color.GREEN.darker().darker());
                        }

                        //explorer.getJTree().isExpanded()
                        // boolean expanded = explorer.getJTree().isExpanded(new TreePath(node.getPath()));


                    }

                }

                return label;
            }


        });


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

    public IImageDataSource[] requestLoadableImages() {
        TreePath[] paths = explorer.getJTree().getSelectionPaths();
        if (paths == null) return new SoftImageDataSource[0];
        List<IImageDataSource> list = new ArrayList<IImageDataSource>();
        for (int p = 0; p < paths.length; p++) {
            Object[] obj = paths[p].getPath();

            for (int i = 0; i < obj.length; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj[i];
                if (node.isLeaf()) {
                    Object userObject = node.getUserObject();
                    if (userObject instanceof IImageDataSource) {
                        list.add((IImageDataSource) userObject);

                    }
                }
            }
        }

        IImageDataSource[] ret = new IImageDataSource[list.size()];
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


    class ImageNodeWorker extends SwingWorker<List<ImageFileObjectNode>, ImageFileObjectNode> {

        ImageFileObjectNode parent;


        public ImageNodeWorker(ImageFileObjectNode parent) {
            this.parent = parent;
        }

        protected List<ImageFileObjectNode> doInBackground() throws Exception {
            log.fine("fetching image file nodes in background");
            ImageFileExplorer.this.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            List<ImageFileObjectNode> nodes = parent.fetchChildNodes(this);
            log.fine("number of nodes " + nodes.size());
            ImageFileExplorer.this.getComponent().setCursor(Cursor.getDefaultCursor());

            return nodes;
        }

        public void addNode(ImageFileObjectNode node) {
            publish(node);
        }


        protected void done() {
            AbstractTreeModel model = (AbstractTreeModel) (explorer.getJTree().getModel());
            model.nodeChanged(parent);
        }

        protected void process(List<ImageFileObjectNode> chunks) {
            for (ImageFileObjectNode node : chunks) {
                parent.add(node);
            }


            TreeModel model = explorer.getJTree().getModel();
            DefaultTreeModel dtm = null;
            if (model instanceof DefaultTreeModelWrapper) {
                DefaultTreeModelWrapper wrapper = (DefaultTreeModelWrapper) model;
                dtm = (DefaultTreeModel) wrapper.getActualModel();
            } else if (model instanceof DefaultTreeModel) {
                dtm = (DefaultTreeModel) model;
            }

            if (dtm != null) {

                dtm.nodeStructureChanged(parent);
            }

        }
    }

    /*public static class ImageFileObjectNode extends DefaultMutableTreeNode {

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

            if (!areChildrenDefined()) {
                defineChildNodes();
            }


            return super.getChildCount();

        }


        public void defineChildNodes() {
            areChildrenDefined = true;
            System.out.println("leaf: " + isLeaf() + " defining child nodes for: " + fileObject.getName().getPath());
            try {

                FileObject[] children = fileObject.findFiles(selector);
                SoftImageDataSource[] limgs = ImageIOManager.getInstance().findLoadableImages(children);

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
    }*/



    // data source node
    // folder node
    // bucket node



    public static class ImageFileObjectNode extends DefaultMutableTreeNode {

        private boolean areChildrenDefined = false;

        private boolean leaf;

        private FileObject fileObject;

        private FileSelector selector;


        private List<ImageFileObjectNode> childNodes = new ArrayList<ImageFileObjectNode>();

        public ImageFileObjectNode(FileObject _fobj, FileSelector _selector) {
            selector = _selector;
            fileObject = _fobj;

            try {
                if (fileObject.getType() == FileType.FOLDER) {
                    leaf = false;
                } else {
                    if (ImageIOManager.getInstance().isLoadableImage(fileObject)) {
                        try {
                            ImageIODescriptor desc = ImageIOManager.getInstance().getDescriptor(fileObject);
                            IImageDataSource source = desc.createLoadableImage(fileObject);
                            System.out.println("reading image info ...");
                            ImageInfo info = source.readImageInfo();
                            System.out.println("info name : " + info.getDataFile());
                            System.out.println("num images : " + info.getNumImages());
                        } catch(BrainflowException e) {
                            throw new RuntimeException(e);
                        }
                    }
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

        public List<ImageFileObjectNode> getChildNodes() {
            return childNodes;
        }

        public List<ImageFileObjectNode> fetchChildNodes(ImageNodeWorker worker) {


            try {
                log.finest("fetching child nodes");
                FileObject[] children = fileObject.findFiles(selector);
                log.finest("found " + children.length + " nodes");
                IImageDataSource[] limgs = ImageIOManager.getInstance().findLoadableImages(children);

                // first add folders
                for (int i = 0; i < children.length; i++) {
                    if (children[i].getType() == FileType.FOLDER) {

                        log.finest("adding folder node " + children[i].getName().getPath());
                        ImageFileObjectNode node = new ImageFileObjectNode(children[i], selector);
                        childNodes.add(node);
                        worker.addNode(node);
                    }
                }

                // then add confirmed loadable images
                for (int i = 0; i < limgs.length; i++) {
                    ImageFileObjectNode node = new ImageFileObjectNode(limgs[i].getHeaderFile(), selector);
                    node.setUserObject(limgs[i]);
                    childNodes.add(node);
                    worker.addNode(node);
                }
            } catch (FileSystemException e) {
                throw new RuntimeException(e);
            }


            areChildrenDefined = true;

            return childNodes;

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

