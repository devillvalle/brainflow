package com.brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 3:43:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DepthFirstAdapter extends AnalysisAdapter {


    public DepthFirstAdapter() {

    }



    public void caseComparisonNode(ComparisonNode node) {
        System.out.println("parent of " + node + " is : " + node.getParent());
        node.left().apply(this);
        node.right().apply(this);


    }

    public void caseVariableNode(VariableNode node) {
        System.out.println("parent of " + node + " is : " + node.getParent());

    }

    public void caseConstantNode(ConstantNode node) {
        System.out.println("parent of " + node + " is : " + node.getParent());
        
    }
}
