package com.brainflow.gui;

import com.brainflow.utils.ResourceLoader;
import org.apache.commons.vfs.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Aug 20, 2004
 * Time: 11:16:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileExplorer extends AbstractPresenter {


    private Logger log = Logger.getLogger(getClass().getName());

    private List<FileObject> rootList = new ArrayList<FileObject>();

    protected JTree fileTree;

    protected DefaultTreeModel treeModel;

    //private ImageIcon folderIcon = new ImageIcon(ResourceLoader.getResource("resources/icons/folder.png"));
    //private ImageIcon folderOpenIcon = new ImageIcon(ResourceLoader.getResource("resources/icons/folderOpen.png"));

    private ImageIcon leafIcon = new ImageIcon(ResourceLoader.getResource("resources/icons/intf_obj.gif"));

    protected FileSelector selector;

    public FileExplorer(FileObject _rootObject, FileSelector _selector) {
        rootList.add(_rootObject);

        selector = _selector;
        init();
    }


    public JTree getJTree() {
        return fileTree;
    }


    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        fileTree.addTreeSelectionListener(tsl);
    }

    public void addTreeExpansionListener(TreeExpansionListener tel) {
        fileTree.addTreeExpansionListener(tel);
    }


    private void init() {
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        //renderer.setOpenIcon(folderOpenIcon);
        //renderer.setClosedIcon(folderIcon);
        renderer.setLeafIcon(leafIcon);


        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("File Systems");
        treeModel = new DefaultTreeModel(rootNode);

        fileTree = new JTree(treeModel);
        //fileTree.setRootVisible(false);
        for (FileObject root : rootList) {
            //rootNode.add(createTreeNode(root));
            addFileRoot(root);
        }


        fileTree.setCellRenderer(renderer);
        fileTree.setDragEnabled(true);
        fileTree.scrollPathToVisible(new TreePath(rootNode.getPath()));


    }

    public void addFileRoot(FileObject fobj) {
        DefaultMutableTreeNode node = createTreeNode(fobj);

        MutableTreeNode root = (MutableTreeNode) treeModel.getRoot();
        treeModel.insertNodeInto(node, root,
                root.getChildCount());

        fileTree.scrollPathToVisible(new TreePath(node.getPath()));


    }

    protected DefaultMutableTreeNode createTreeNode(FileObject fobj) {
        return new FileObjectNode(fobj);
    }


    public static void main(String args[]) {
        try {
            FileExplorer fe = new FileExplorer(VFS.getManager().resolveFile(System.getProperty("user.dir")), null);
            JFrame jf = new JFrame("Tree Demo");
            jf.add(fe.getComponent(), "Center");
            jf.pack();
            jf.setVisible(true);
        } catch (FileSystemException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public JComponent getComponent() {
        return fileTree;
    }


    class FileObjectNode extends DefaultMutableTreeNode {
        private boolean areChildrenDefined = false;


        private boolean leaf;
        private FileObject fobj;

        public FileObjectNode(FileObject _fobj) {
            fobj = _fobj;
            try {

                if (fobj.getType() == FileType.FOLDER) {
                    leaf = false;
                } else {
                    leaf = true;
                }
            } catch (FileSystemException e) {
                e.printStackTrace();
            }

        }

        public boolean isLeaf() {
            return leaf;
        }

        public int getChildCount() {
            if (!areChildrenDefined)
                defineChildNodes();
            return (super.getChildCount());
        }

        private void defineChildNodes() {

            areChildrenDefined = true;

            try {

                FileObject[] children = fobj.findFiles(selector);

                for (int i = 0; i < children.length; i++) {
                    add(new FileObjectNode(children[i]));
                }
            } catch (FileSystemException e) {
                e.printStackTrace();
            }
        }

        public String toString() {
            if (this == getRoot()) {
                try {
                    URI uri = new URI(fobj.getName().getURI());

                    return uri.getHost() + ":" + uri.getPath();
                } catch (URISyntaxException e) {
                    return fobj.getName().getURI();
                }
            } else
                return fobj.getName().getBaseName();
        }
    }


    class CustomFileSelector implements FileSelector {


        public CustomFileSelector() {

        }

        /**
         * Determines if a file or folder should be selected.
         */
        public boolean includeFile(final FileSelectInfo fileInfo)
                throws FileSystemException {

            if (fileInfo.getDepth() == 0)
                return false;

            else
                return true;
        }

        /**
         * Determines whether a folder should be traversed.
         */
        public boolean traverseDescendents(final FileSelectInfo fileInfo) {
            if (fileInfo.getDepth() == 0) {
                return true;
            } else
                return false;
        }
    }

}
