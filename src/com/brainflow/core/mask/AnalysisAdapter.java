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
        inStart(rootNode);
        rootNode.apply(this);
        outStart(rootNode);
    }

    public void inStart(INode rootNode) {

    }

    public void outStart(INode rootNode) {

    }

    public void caseComparisonNode(ComparisonNode node) {
        inComparison(node);
        node.left().apply(this);
        node.right().apply(this);
        outComparison(node);

    }

    public void inComparison(ComparisonNode node) {

    }

    public void outComparison(ComparisonNode node) {

    }



    public void caseVariableNode(VariableNode node) {


    }

    public void inVariable(VariableNode node) {

    }

    public void outVariable(VariableNode node) {

    }

    public void caseConstantNode(ConstantNode node) {


    }

    public void inConstant(ConstantNode node) {
  
    }

    public void outConstant(ConstantNode node) {

    }

    public void caseImageDataNode(ImageDataNode node) {

    }

    public void inImageData(ImageDataNode node) {

    }

    public void outImageData(ImageDataNode node) {

    }
}
