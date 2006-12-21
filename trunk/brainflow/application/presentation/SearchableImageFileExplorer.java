package com.brainflow.application.presentation;

import com.brainflow.gui.AbstractPresenter;
import com.jidesoft.list.ListModelWrapper;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.tree.FilterableTreeModel;
import com.jidesoft.tree.QuickTreeFilterField;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 7:30:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchableImageFileExplorer extends AbstractPresenter {

    private JPanel mainPanel = new JPanel(new BorderLayout(6, 6));
    private ImageFileExplorer explorer;


    public SearchableImageFileExplorer(ImageFileExplorer explorer) {
        this.explorer = explorer;
        init();
    }

    private void init() {
        JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        final TreeModel treeModel = explorer.getTreeModel();
        final QuickTreeFilterField field = new QuickTreeFilterField(treeModel) {
            protected FilterableTreeModel createDisplayTreeModel(TreeModel treeModel) {
                return new FilterableTreeModel(treeModel) {
                    @Override
                    protected void configureListModelWrapper(ListModelWrapper wrapper, Object node) {

                        if (node instanceof ImageFileExplorer.ImageFileObjectNode) {
                            ImageFileExplorer.ImageFileObjectNode inode = (ImageFileExplorer.ImageFileObjectNode) node;

                            if (inode.areChildrenDefined() || inode.isLeaf()) {
                                super.configureListModelWrapper(wrapper, node);
                            }

                        }
                    }
                };
            }
        };


        field.setSearchingDelay(2000);
        quickSearchPanel.add(field);


        JPanel treePanel = new JPanel(new BorderLayout(2, 2));


        field.setTree(explorer.getJTree());
        SearchableUtils.installSearchable(field.getTree());

        JTree tree = field.getTree();
        tree.setModel(field.getDisplayTreeModel());


        treePanel.add(new JScrollPane(tree));

        mainPanel.add(treePanel);
        mainPanel.add(quickSearchPanel, BorderLayout.BEFORE_FIRST_LINE);


    }

    public JComponent getComponent() {
        return mainPanel;
    }

    private String getFilterStatus(TreeModel displayModel, TreeModel originalModel) {
        int count = displayModel.getChildCount(displayModel.getRoot());
        String text = count + " out of " + originalModel.getChildCount(originalModel.getRoot()) + " files";
        return text;
    }

}
