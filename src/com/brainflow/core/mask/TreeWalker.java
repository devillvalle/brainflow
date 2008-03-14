package com.brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 3:40:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TreeWalker {

    public void caseComparisonNode(ComparisonNode node);

    public void caseVariableNode(VariableNode node);

    public void caseConstantNode(ConstantNode node);

    public void caseImageDataNode(ImageDataNode node);

    public void caseMaskDataNode(MaskDataNode node);




}
