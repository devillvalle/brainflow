package com.brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 5:26:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnalysisAdapter implements TreeWalker {

    private INode rootNode;


    public void start(INode rootNode) {
        this.rootNode = rootNode;
        rootNode.apply(this);
    }

    public void caseComparisonNode(ComparisonNode node) {
        node.left().apply(this);
        node.right().apply(this);

    }

    public void caseVariableNode(VariableNode node) {
        System.out.println(node);

    }

    public void caseConstantNode(ConstantNode node) {
        System.out.println(node);

    }

    public void caseImageDataNode(ImageDataNode node) {
        System.out.println(node);
    }
}
