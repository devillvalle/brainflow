package com.brainflow.core.mask;

import com.jidesoft.status.StatusBar;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.BF;
import com.brainflow.core.ImageView;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.layer.IMaskProperty;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.layer.MaskLayer3D;
import com.brainflow.core.layer.ImageLayer3D;
import com.brainflow.application.BrainFlowException;
import com.brainflow.application.presentation.binding.ExtBind;
import com.brainflow.gui.MultiSelectToggleBar;
import com.brainflow.image.data.IImageData;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 14, 2008
 * Time: 11:21:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryExpressionTester {


    JFrame frame = new JFrame();

    JTextArea textArea = new JTextArea();

    JButton parseButton = new JButton("Parse");

    JTree expressionTree = new JTree();

    StatusBar status = new StatusBar();

    LabelStatusBarItem statusLabel = new LabelStatusBarItem();

    IImageDisplayModel model;

    ImageView dataView;

    ImageView maskView;

    //ImageView maskView;

    public BinaryExpressionTester() {

        //textArea.setColumns(10);
        textArea.setRows(10);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        model = loadModel();

        dataView = new ImageView(model);
        maskView = new ImageView(createMaskModel(model));

        JPanel leftPanel = wrapImageView(dataView);
        JPanel rightPanel = wrapImageView(maskView);

        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);


        mainPanel.add(splitpane, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(textArea), BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEtchedBorder());

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(parseButton, BorderLayout.NORTH);


        textArea.setText("" + "V4 > V1 and V2 > 1.0");


        parseExpression();

        frame.add(expressionTree, BorderLayout.EAST);

        status.add(statusLabel);
        statusLabel.setText("Ready");
        frame.add(status, BorderLayout.SOUTH);


        frame.pack();
        frame.setVisible(true);

        parseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parseExpression();


            }
        });


    }

    private IImageDisplayModel createMaskModel(IImageDisplayModel model) {
        IImageDisplayModel imodel = new ImageDisplayModel("maskmodel");
        for (int i = 0; i < model.getNumLayers(); i++) {
            ImageLayer3D layer = model.getLayer(i);
            MaskLayer3D masklayer = new MaskLayer3D(layer.getMaskProperty().buildMask());
            imodel.addLayer(masklayer);
        }

        return imodel;

    }

    public JPanel wrapImageView(ImageView view) {
        MultiSelectToggleBar toggleBar = new MultiSelectToggleBar(Arrays.asList(1));
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(toggleBar, BorderLayout.NORTH);
        viewPanel.add(view, BorderLayout.CENTER);

        ExtBind.get().bindContent(view.getModel().getListModel(), toggleBar);
        ExtBind.get().bindToggleIndices(view.getModel().getVisibleSelection(), toggleBar);

        return viewPanel;


    }


    public void parseExpression() {
        BinaryExpressionParser parser = new BinaryExpressionParser(new Context() {
            public Object getValue(String symbol) {
                int index = mapIndex(symbol);

                if (index < 0 || (index > model.getNumLayers() - 1)) {
                    throw new IllegalArgumentException("illegal layer index " + index);
                }

                return model.getLayer(index).getData();

            }


            private int mapIndex(String varName) {
                if (!varName.toUpperCase().startsWith("V")) {
                    throw new IllegalArgumentException("illegal variable name : " + varName);
                }

                String numberPart = varName.substring(1);
                return Integer.parseInt(numberPart) - 1;


            }
        });


        try

        {
            INode node = parser.createParser().parse(textArea.getText());

            RootNode root = new RootNode(node);
            VariableSubstitution varsub = new VariableSubstitution(model);
            RootNode vnode = varsub.start(root);

            MaskSubstitution masksub = new MaskSubstitution();
            RootNode mnode = masksub.start(vnode);

            // model.getLayer(model.getNumLayers()-1).getMaskProperty().putMask(IMaskProperty.MASK_KEY.EXPRESSION_MASK, mnode.getData());

            //ComparisonNode cnode = (ComparisonNode)node;
            //MaskDataNode masknode = (MaskDataNode)cnode.left();
            // System.out.println("cardinality " + masknode.getData().cardinality());

            expressionTree.setModel(createTreeModel(mnode.getChild()));
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setText("Expression Parsed Successfully");


        }

        catch (
                Exception ex
                )

        {
            Toolkit.getDefaultToolkit().beep();
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(ex.getMessage());
            ex.printStackTrace();
        }

    }


    private IImageDisplayModel loadModel() {
        try {
            return BF.createModel(BF.getDataURL("mask1.nii"), BF.getDataURL("mask2.nii"), BF.getDataURL("mask3.nii"), BF.getDataURL("mask4.nii"));
        } catch (BrainFlowException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    class ExpressionNode implements TreeNode {

        INode inode;

        ExpressionNode(INode node) {
            this.inode = node;
        }

        public ExpressionNode getChildAt(int childIndex) {
            return new ExpressionNode(inode.getChildren().get(childIndex));
        }

        public int getChildCount() {
            return inode.getChildren().size();
        }

        public TreeNode getParent() {

            return new ExpressionNode(inode.getParent());
        }

        public int getIndex(TreeNode node) {
            if (node instanceof ExpressionNode) {
                ExpressionNode testnode = (ExpressionNode) node;
                for (int i = 0; i < getChildCount(); i++) {
                    ExpressionNode tnode = getChildAt(i);
                    if (tnode.inode == testnode.inode) {
                        return i;
                    }
                }

                return -1;
            } else {
                return -1;
            }
        }

        @Override
        public String toString() {
            return inode.toString();
        }

        public boolean getAllowsChildren() {
            return false;
        }

        public boolean isLeaf() {
            return getChildCount() == 0;
        }

        public Enumeration children() {

            return new Enumeration() {
                int i = 0;

                public boolean hasMoreElements() {
                    return (i < (getChildCount() - 1));
                }

                public Object nextElement() {
                    return getChildAt(i++);
                }
            };

        }
    }


    private TreeModel createTreeModel(INode root) {
        return new DefaultTreeModel(new ExpressionNode(root));
    }


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            LookAndFeelFactory.installJideExtension();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        new BinaryExpressionTester();
    }
}
