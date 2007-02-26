package com.brainflow.application.presentation;

import com.brainflow.application.BrainflowProject;
import com.brainflow.application.ILoadableImage;
import com.brainflow.application.toplevel.BrainflowProjectListener;
import com.brainflow.application.toplevel.BrainflowProjectEvent;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.core.ImageView;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageLayer;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.MutableTreeNode;

import net.antonioshome.swing.treewrapper.TreeWrapper;

import java.util.Iterator;
import java.util.Enumeration;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 4:54:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectView extends ImageViewPresenter implements BrainflowProjectListener {

    private BrainflowProject project;

    private ProjectNode rootNode;
    //private TreeWrapper projectTree;
    private JTree tree;

    public ProjectView(BrainflowProject _project) {
        project = _project;
        project.addListDataListener(this);

        rootNode = new ProjectNode(project);

        tree = new JTree(rootNode);

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

    public void modelAdded(BrainflowProjectEvent event) {
        rootNode.add(new ImageDisplayModelNode(event.getModel(), true));
        
        
    }

    public void modelRemoved(BrainflowProjectEvent event) {
        
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

    class ProjectNode implements MutableTreeNode {

        private BrainflowProject project;


        public ProjectNode(BrainflowProject _project) {
 
            this.project = _project;

            Iterator<IImageDisplayModel> iter = project.iterator();
            while (iter.hasNext()) {
                add(new ImageDisplayModelNode(iter.next(), true));
            }
        }


        public void insert(MutableTreeNode child, int index) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void remove(int index) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void remove(MutableTreeNode node) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setUserObject(Object object) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void removeFromParent() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setParent(MutableTreeNode newParent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public TreeNode getChildAt(int childIndex) {
            return
        }

        public int getChildCount() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public TreeNode getParent() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public int getIndex(TreeNode node) {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public boolean getAllowsChildren() {
            return true;
        }

        public Enumeration children() {
            return Collections.enumeration(project.getModelList());
        }

        public boolean isLeaf() {
            return false;
        }


        
    }

    class ImageDisplayModelNode extends DefaultMutableTreeNode {

        private IImageDisplayModel model;

        public ImageDisplayModelNode(IImageDisplayModel _model) {
            model = _model;
            
            model.addListDataListener(new ListDataListener() {

                public void intervalAdded(ListDataEvent e) {
                    int idx = e.getIndex0();
                    ImageLayer layer = model.getImageLayer(idx);
                    ImageDisplayModelNode.this.insert(new DefaultMutableTreeNode(layer, false), idx);
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
