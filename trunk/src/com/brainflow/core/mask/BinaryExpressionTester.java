package com.brainflow.core.mask;

import com.jidesoft.status.StatusBar;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.BF;
import com.brainflow.application.BrainFlowException;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

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

    public BinaryExpressionTester() {

        textArea.setColumns(50);
        textArea.setRows(10);

        frame.add(textArea, BorderLayout.CENTER);
        frame.add(parseButton, BorderLayout.NORTH);


        textArea.setText("" + "V1 > 0 and V2 > 1");

        BinaryExpressionParser parser = new BinaryExpressionParser();
        INode node = parser.createParser().parse(textArea.getText());

        expressionTree = new JTree(createTreeModel(node));
        expressionTree.setBorder(BorderFactory.createEtchedBorder());
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


        model = loadModel();

        parseExpression();


    }


    public void parseExpression() {
        BinaryExpressionParser parser = new BinaryExpressionParser();
        try {
            INode node = parser.createParser().parse(textArea.getText());
            VariableSubstitution varsub = new VariableSubstitution(model);
            node = varsub.start(node);

            MaskSubstitution masksub = new MaskSubstitution();
            node = masksub.start(node);
            
            expressionTree.setModel(createTreeModel(node));
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setText("Expression Parsed Successfully");


        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(ex.getMessage());
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

        AbstractNode inode;

        ExpressionNode(AbstractNode node) {
            this.inode = node;
        }

        public ExpressionNode getChildAt(int childIndex) {
            return new ExpressionNode((AbstractNode) inode.getChildren().get(childIndex));
        }

        public int getChildCount() {
            return inode.getChildren().size();
        }

        public TreeNode getParent() {
            return new ExpressionNode((AbstractNode) inode.getParent());
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
            return false;  //To change body of implemented methods use File | Settings | File Templates.
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
        return new DefaultTreeModel(new ExpressionNode((AbstractNode) root));
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
