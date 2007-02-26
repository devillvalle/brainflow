package com.brainflow.application.presentation;

import com.brainflow.application.BrainflowProject;
import com.brainflow.application.toplevel.BrainflowProjectEvent;
import com.brainflow.application.toplevel.BrainflowProjectListener;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;
import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 4:54:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectView extends ImageViewPresenter {

    private BrainflowProject project;

    private DefaultTreeModel treeModel;
    private ProjectNode rootNode;

    private JTree tree;

    public ProjectView(BrainflowProject _project) {
        project = _project;
        rootNode = new ProjectNode(project);

        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);


    }


    public void viewSelected(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return tree;
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
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void contentsChanged(BrainflowProjectEvent event) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void intervalRemoved(BrainflowProjectEvent event) {
            //To change body of implemented methods use File | Settings | File Templates.
        }


        public boolean isLeaf() {
            return false;
        }


    }

    class ImageDisplayModelNode extends DefaultMutableTreeNode {

        private IImageDisplayModel model;

        public ImageDisplayModelNode(IImageDisplayModel _model) {
            super(_model);
            model = _model;

            model.addListDataListener(new ListDataListener() {

                public void intervalAdded(ListDataEvent e) {
                    int idx = e.getIndex0();
                    ImageLayer layer = model.getImageLayer(idx);
                    ImageDisplayModelNode.this.add(new DefaultMutableTreeNode(layer, false));
                    treeModel.nodesWereInserted(ImageDisplayModelNode.this, new int[]{ImageDisplayModelNode.this.getChildCount() - 1});
                }

                public void intervalRemoved(ListDataEvent e) {
                    int idx = e.getIndex0();
                    ImageDisplayModelNode.this.remove(idx);

                }

                public void contentsChanged(ListDataEvent e) {
                    // not sure what to do
                }
            });
        }

        public boolean isLeaf() {
            return false;
        }


    }

}
