package com.brainflow.application.presentation;

import com.brainflow.application.BrainflowProject;
import com.brainflow.application.ILoadableImage;
import com.brainflow.gui.AbstractPresenter;
import com.brainflow.core.ImageView;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageLayer;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import net.antonioshome.swing.treewrapper.TreeWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 4:54:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectView extends ImageViewPresenter {

    private BrainflowProject project;
    private TreeWrapper projectTree;
    private JTree tree;

    public ProjectView(BrainflowProject _project) {
        project = _project;
    }


    public void viewSelected(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    class ImageDisplayModelNode extends DefaultMutableTreeNode {

        private IImageDisplayModel model;

        public ImageDisplayModelNode(IImageDisplayModel _model, boolean allowsChildren) {
            super(_model, allowsChildren);
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

        public int getChildCount() {
            return model.getNumLayers();
        }

        
    }

}
