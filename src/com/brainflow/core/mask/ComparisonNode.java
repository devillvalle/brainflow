package com.brainflow.core.mask;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:14:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComparisonNode extends AbstractNode {


    private INode left;

    private INode right;

    private Operation op;



    public ComparisonNode(INode left, INode right, Operation op) {
        this.left = left;
        this.right = right;
        this.op = op;

        left.setParent(this);
        right.setParent(this);
    }

    public List<INode> getChildren() {
        return Arrays.asList(left, right);
    }


    public INode left() {
        return left;
    }

    public INode right() {
        return right;
    }

    public boolean isLeaf() {
        return false;
    }

    public void apply(TreeWalker walker) {
        walker.caseComparisonNode(this);
    }

    public void replaceChild(INode oldChild, INode newChild) {
        if (left == oldChild) {
            left = newChild;
        } else if (right == oldChild) {
            right = newChild;
        }
    }

    public String toString() {
        return "ComparisonNode{" +
                "left=" + left +
                ", right=" + right +
                ", operation='" + op + '\'' +
                '}';
    }
}
