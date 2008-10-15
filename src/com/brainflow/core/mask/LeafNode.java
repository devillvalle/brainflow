package com.brainflow.core.mask;

import com.brainflow.image.operations.BinaryOperand;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 14, 2008
 * Time: 4:45:04 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class LeafNode extends AbstractNode implements LeafVisitable {


    public abstract LeafNode visitImage(ImageDataNode other, BinaryOperand op);
    public abstract LeafNode visitMask(MaskDataNode other, BinaryOperand op);
    public abstract LeafNode visitConstant(ConstantNode other, BinaryOperand op);

    public abstract LeafNode accept(LeafNode leaf, BinaryOperand op);
}
