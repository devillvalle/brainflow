package com.brainflow.core.mask;

import com.brainflow.core.IImageDisplayModel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 5:40:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskExpressionBuilder extends AnalysisAdapter {

    private IImageDisplayModel model;

    public MaskExpressionBuilder(IImageDisplayModel model) {
        this.model = model;
    }

    public void caseVariableNode(VariableNode node) {
        node.replaceBy(new VariableNode("Moth fucker!"));
        
        
    }

    
}
