package com.brainflow.core.mask;

import com.brainflow.image.data.IMaskedData3D;
import com.brainflow.image.operations.BinaryOperand;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 12, 2008
 * Time: 9:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskDataNode extends LeafNode implements LeafVisitable {


    private IMaskedData3D mask;

    public MaskDataNode(IMaskedData3D mask) {
        this.mask = mask;
    }

    public void apply(TreeWalker walker) {
        walker.caseMaskDataNode(this);
    }

    public IMaskedData3D getData() {
        return mask;
    }

    public void accept(LeafNode leaf, BinaryOperand op) {
        leaf.visitMask(this, op );
    }

    public AbstractNode visitImage(ImageDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AbstractNode visitConstant(ConstantNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AbstractNode visitMask(MaskDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int depth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String toString() {
        return mask.toString();
    }
}
