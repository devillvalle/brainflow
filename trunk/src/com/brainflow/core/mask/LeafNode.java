package com.brainflow.core.mask;

import com.brainflow.image.operations.BinaryOperand;
import com.brainflow.image.operations.UnaryOperand;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 14, 2008
 * Time: 4:45:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LeafNode extends INode, LeafVisitable {


    public abstract LeafNode visitImage(ImageDataNode other, BinaryOperand op);

    public abstract LeafNode visitMask(MaskDataNode other, BinaryOperand op);

    public abstract LeafNode visitConstant(ConstantNode other, BinaryOperand op);

    public abstract LeafNode visitUnary(UnaryOperand op);

    //public abstract LeafNode accept(LeafNode leaf, BinaryOperand op);
}
