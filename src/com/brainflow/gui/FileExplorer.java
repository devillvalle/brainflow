package com.brainflow.gui;

import com.brainflow.utils.ResourceLoader;
import com.brainflow.application.presentation.ImageFileExplorer;
import org.apache.commons.vfs.*;


import javax.swing.*;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
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


    protected static FileSystemView fsv = FileSystemView.getFileSystemView();

    private static Logger log = Logger.getLogger(FileExplorer.class.getName());

    private List<FileObject> rootList = new ArrayList<FileObject>();

    protected JTree fileTree;

    protected DefaultTreeModel treeModel;

    private ImageIcon folderIcon = new ImageIcon(ResourceLoader.getResource("resources/icons/folder.png"));

    private ImageIcon folderOpenIcon = new ImageIcon(ResourceLoader.getResource("resources/icons/folderOpen.png"));

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

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel());
            FileExplorer explorer = new FileExplorer(VFS.getManager().resolveFile("C:/javacode"), new FileSelector() {
                public boolean includeFile(FileSelectInfo fileSelectInfo) throws Exception {
                    return true;
                }

                public boolean traverseDescendents(FileSelectInfo fileSelectInfo) throws Exception {
                    if (fileSelectInfo.getDepth() == 0) {
                        return true;
                    } else
                        return false;
                    
                }
            });


            JFrame frame = new JFrame();
            frame.add(explorer.getComponent(), BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void init() {


        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("File Systems");
        treeModel = new DefaultTreeModel(rootNode);

        fileTree = new JTree(treeModel);
        fileTree.setCellRenderer(new FileTreeCellRenderer());


        for (FileObject root : rootList) {

            addFileRoot(root);
        }


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


    public JComponent getComponent() {
        return fileTree;
    }

    private static class FileTreeNode implements TreeNode {
        /**
         * Node file.
         */
        private File file;

        /**
         * Children of the node file.
         */
        private File[] children;

        /**
         * Parent node.
         */
        private TreeNode parent;

        /**
         * Indication whether this node corresponds to a file system root.
         */
        private boolean isFileSystemRoot;

        /**
         * Creates a new file tree node.
         *
         * @param file             Node file
         * @param isFileSystemRoot Indicates whether the file is a file system root.
         * @param parent           Parent node.
         */
        public FileTreeNode(File file, boolean isFileSystemRoot, TreeNode parent) {
            this.file = file;
            this.isFileSystemRoot = isFileSystemRoot;
            this.parent = parent;
            this.children = this.file.listFiles();
            if (this.children == null)
                this.children = new File[0];
        }

        /**
         * Creates a new file tree node.
         *
         * @param children Children files.
         */
        public FileTreeNode(File[] children) {
            this.file = null;
            this.parent = null;
            this.children = children;
        }

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.TreeNode#children()
        */
        public Enumeration<?> children() {
            final int elementCount = this.children.length;
            return new Enumeration<File>() {
                int count = 0;

                /*
                * (non-Javadoc)
                *
                * @see java.util.Enumeration#hasMoreElements()
                */
                public boolean hasMoreElements() {
                    return this.count < elementCount;
                }

                /*
                * (non-Javadoc)
                *
                * @see java.util.Enumeration#nextElement()
                */
                public File nextElement() {
                    if (this.count < elementCount) {
                        return FileTreeNode.this.children[this.count++];
                    }
                    throw new NoSuchElementException("Vector Enumeration");
                }
            };

        }

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.TreeNode#getAllowsChildren()
        */
        public boolean getAllowsChildren() {
            return true;
        }

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.TreeNode#getChildAt(int)
        */
        public TreeNode getChildAt(int childIndex) {
            return new FileTreeNode(this.children[childIndex],
                    this.parent == null, this);
        }

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.TreeNode#getChildCount()
        */
        public int getChildCount() {
            return this.children.length;
        }

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
        */
        public int getIndex(TreeNode node) {
            FileTreeNode ftn = (FileTreeNode) node;
            for (int i = 0; i < this.children.length; i++) {
                if (ftn.file.equals(this.children[i]))
                    return i;
            }
            return -1;
        }

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.TreeNode#getParent()
        */
        public TreeNode getParent() {
            return this.parent;
        }

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.TreeNode#isLeaf()
        */
        public boolean isLeaf() {
            return (this.getChildCount() == 0);
        }
    }


    class FileObjectNode extends DefaultMutableTreeNode {

        private boolean areChildrenDefined = false;


        private boolean leaf;

        private FileObject fileObject;

        public FileObjectNode(FileObject _fobj) {
            fileObject = _fobj;
            try {

                if (fileObject.getType() == FileType.FOLDER) {
                    leaf = false;
                } else {
                    leaf = true;
                }
            } catch (FileSystemException e) {
                e.printStackTrace();
            }

        }

        public FileObject getFileObject() {
            return fileObject;
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

                FileObject[] children = fileObject.findFiles(selector);

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
                    URI uri = new URI(fileObject.getName().getURI());

                    return uri.getHost() + ":" + uri.getPath();
                } catch (URISyntaxException e) {
                    return fileObject.getName().getURI();
                }
            } else
                return fileObject.getName().getBaseName();
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


    private class FileTreeCellRenderer extends DefaultTreeCellRenderer {
        /**
         * Icon cache to speed the rendering.
         */
        private Map<String, Icon> iconCache = new HashMap<String, Icon>();

        /**
         * Root name cache to speed the rendering.
         */
        private Map<FileObject, String> rootNameCache = new HashMap<FileObject, String>();

        /*
        * (non-Javadoc)
        *
        * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
        *      java.lang.Object, boolean, boolean, boolean, int, boolean)
        */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {

            FileObjectNode ftn;

            if (value instanceof FileObjectNode) {
                ftn = (FileObjectNode) value;
            } else {
                return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }


            FileObject file = ftn.getFileObject();
            String filename = "";
            Icon icon = null;

            try {
                if (file != null) {


                    if (file.equals(file.getFileSystem().getRoot())) {
                        // long start = System.currentTimeMillis();
                        filename = this.rootNameCache.get(file);
                        //if (filename == null) {
                        //    filename = fsv.getSystemDisplayName(file);
                        //    this.rootNameCache.put(file, filename);
                        //}
                        // long end = System.currentTimeMillis();
                        // System.out.println(filename + ":" + (end - start));
                    } else {
                        filename = file.getName().getBaseName();
                    }

                    if (FileType.FOLDER == file.getType()) {
                        icon = folderIcon;
                    }
                }


                JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
                        filename, sel, expanded, leaf, row, hasFocus);
                if (icon != null) {
                    result.setIcon(icon);

                }

                return result;


            } catch (Exception e) {
                log.severe(e.getMessage());
                throw new RuntimeException(e);
            }


        }
    }
}