package com.brainflow.application.presentation;

import com.brainflow.application.BrainflowProject;
import com.brainflow.application.dnd.DnDUtils;
import com.brainflow.application.toplevel.BrainflowProjectEvent;
import com.brainflow.application.toplevel.BrainflowProjectListener;
import com.brainflow.core.*;
import com.brainflow.core.layer.AbstractLayer;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.io.IImageDataSource;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.datatransfer.Transferable;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 4:54:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTreeView extends ImageViewPresenter implements MouseListener, MouseMotionListener {

    private BrainflowProject project;

    private DefaultTreeModel treeModel;

    private ProjectNode rootNode;

    private JTree tree;

    public ProjectTreeView(BrainflowProject _project) {
        project = _project;
        rootNode = new ProjectNode(project);

        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);

        initDnD();

    }

    private void initDnD() {
        // todo hackalicious
        tree.setDragEnabled(true);

        tree.addMouseListener(this);
        tree.addMouseMotionListener(this);


        TransferHandler handler = new TransferHandler() {
            public int getSourceActions(JComponent c) {
                return TransferHandler.COPY;
            }

            protected Transferable createTransferable(JComponent c) {

                Transferable ret = null;
                if (c instanceof JTree) {
                    JTree tree = (JTree) c;
                    TreePath path = tree.getSelectionPath();
                    Object[] opath = path.getPath();


                    if (opath.length > 0) {
                        Object obj = opath[opath.length - 1];
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
                        if (node.isLeaf()) {
                            Object layer = node.getUserObject();
                            if (layer instanceof ImageLayer) {
                                ImageLayer ilayer = (ImageLayer) layer;
                                ret = DnDUtils.createTransferable(ilayer);
                            }

                        }

                    }


                }

                return ret;


            }
        };

        tree.setTransferHandler(handler);
    }


    public void viewSelected(ImageView view) {
        TreePath path = new TreePath(view.getModel());
        //tree.setS

    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return tree;
    }

    private MouseEvent firstMouseEvent = null;

    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        firstMouseEvent = e;
        e.consume();
    }

    public void mouseDragged(MouseEvent e) {
        System.out.println("dragged!");
        if (firstMouseEvent != null) {
            e.consume();
            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
            //Arbitrarily define a 5-pixel shift as the
            //official beginning of a drag.
            if (dx > 1 || dy > 1) {

                //This is a drag, not a click.
                JComponent c = (JComponent) e.getSource();
                //Tell the transfer handler to initiate the drag.
                System.out.println("exporting as drag!");
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


    class ProjectNode extends DefaultMutableTreeNode implements BrainflowProjectListener {

        private BrainflowProject project;

        public ProjectNode(BrainflowProject _project) {
            super(_project);

            project = _project;

            project.addListDataListener(this);

            Iterator<IImageDisplayModel> iter = project.iterator();

            while (iter.hasNext()) {
                add(new ImageDisplayModelNode(iter.next()));
            }
        }

        public void modelAdded(BrainflowProjectEvent event) {
            add(new ImageDisplayModelNode(event.getModel()));
            treeModel.nodesWereInserted(this, new int[]{getChildCount() - 1});

        }

        public void modelRemoved(BrainflowProjectEvent event) {
            Enumeration en = children();
            while (en.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
                if (node.getUserObject() == event.getModel()) {
                    remove(node);
                }
            }

        }

        public void intervalAdded(BrainflowProjectEvent event) {
            System.out.println("interval added!");
        }

        public void contentsChanged(BrainflowProjectEvent event) {
            System.out.println("contents changed!");
        }

        public void intervalRemoved(BrainflowProjectEvent event) {
            System.out.println("interval removed!");
        }


        public boolean isLeaf() {
            return false;
        }


    }

    class ImageLayerNode extends DefaultMutableTreeNode {

        ImageLayer layer;

        public ImageLayerNode(ImageLayer layer) {
            super(layer);
            this.layer = layer;

        }

        public ImageLayer getUserObject() {
            return layer;
        }
    }

    class ImageDisplayModelNode extends DefaultMutableTreeNode {

        private IImageDisplayModel model;

        private ImageModelListener listener = new ImageModelListener(this);

        public ImageDisplayModelNode(IImageDisplayModel _model) {
            super(_model);
            model = _model;

            model.addImageDisplayModelListener(listener);

            for (int i = 0; i < model.getNumLayers(); i++) {
                ImageLayer layer = model.getLayer(i);
                add(new ImageLayerNode(layer));
                treeModel.nodesWereInserted(ImageDisplayModelNode.this, new int[]{ImageDisplayModelNode.this.getChildCount() - 1});


            }
        }

        public IImageDisplayModel getUserObject() {
            return model;
        }

        public IImageDisplayModel getModel() {
            return model;
        }


        public boolean isLeaf() {
            return false;
        }


    }

    class ImageModelListener implements ImageDisplayModelListener {

        private ImageDisplayModelNode node;

        ImageModelListener(ImageDisplayModelNode node) {
            this.node = node;
        }

        public void intervalAdded(ListDataEvent e) {
            int idx = e.getIndex0();
            ImageLayer layer = node.getModel().getLayer(idx);
            node.add(new ImageLayerNode(layer));
            treeModel.nodesWereInserted(node, new int[]{node.getChildCount() - 1});
        }

        public void intervalRemoved(ListDataEvent e) {
            int idx = e.getIndex0();
            node.remove(idx);

        }


        public void contentsChanged(ListDataEvent e) {
            System.out.println("contents changed!");
        }


        public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

    }

}
