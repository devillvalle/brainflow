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


    public abstract AbstractNode visitImage(ImageDataNode other, BinaryOperand op);
    public abstract AbstractNode visitMask(MaskDataNode other, BinaryOperand op);
    public abstract AbstractNode visitConstant(ConstantNode other, BinaryOperand op);

    public abstract void accept(LeafNode leaf, BinaryOperand op);
}
