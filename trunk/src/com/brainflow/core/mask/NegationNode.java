package com.brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Nov 2, 2008
 * Time: 4:27:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class NegationNode extends AbstractNode {

    private INode negatable;

    public NegationNode(INode negatable) {
        this.negatable = negatable;
    }

    public INode getNegatable() {
        return negatable;
    }

    public int depth() {
        return 1 + negatable.depth();
    }

    public void apply(TreeWalker walker) {
       walker.caseNegationNode(this);
    }

    @Override
    public String toString() {
        return "(-)" + negatable.toString();
    }
}
