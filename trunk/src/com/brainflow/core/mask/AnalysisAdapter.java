package com.brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 5:26:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnalysisAdapter implements TreeWalker {

    protected RootNode rootNode;



    public RootNode start(RootNode rootNode) {
        this.rootNode = rootNode;
        inRootNode(rootNode);
        caseRootNode(rootNode);
        return outRootNode(rootNode);
    }

    public void caseRootNode(RootNode node) {

        node.getChild().apply(this);


    }

    public void inRootNode(RootNode rootNode) {
        
    }

    public RootNode outRootNode(RootNode rootNode) {
        return rootNode;

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

    public void caseNegationNode(NegationNode node) {
        inNegation(node);
        node.getNegatable().apply(this);
        outNegation(node);
    }

    public void inNegation(NegationNode node) {
        //System.out.println("in negation " + node);
        System.out.println("ngetable in class : " + node.getNegatable().getClass());

    }

    public void outNegation(NegationNode node) {
        //System.out.println("out negation " + node);
        System.out.println("ngetable out class : " + node.getNegatable().getClass());

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

    public void caseMaskDataNode(MaskDataNode node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void inMaskData(MaskDataNode node) {

    }

    public void outMaskData(MaskDataNode node) {

    }

}
