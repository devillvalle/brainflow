package com.brainflow.core.mask;

import com.brainflow.image.operations.BinaryOperand;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.image.operations.Operations;
import com.brainflow.image.data.IMaskedData3D;
import com.brainflow.image.data.MaskedData3D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.MaskPredicate;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 7, 2008
 * Time: 5:04:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConstantNode extends LeafNode implements LeafVisitable {


    private double value;

    public ConstantNode(double value) {
        this.value = value;
    }

    public String toString() {
        return "" + value;
    }

    public double getValue() {
        return value;
    }

    public int depth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        return leaf.visitConstant(this, op);
    }

    public LeafNode visitImage(ImageDataNode left, BinaryOperand op) {

        final BinaryOperation bop = Operations.lookup(op);

        IMaskedData3D mdat = new MaskedData3D((IImageData3D) left.getData(), new MaskPredicate() {
            public boolean mask(double ovalue) {
                return bop.isTrue(value, ovalue);
            }
        });

        return new MaskDataNode(mdat);

    }

    public LeafNode visitConstant(ConstantNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode visitMask(MaskDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLeaf() {
        return true;
    }

    public void apply(TreeWalker walker) {
        walker.caseConstantNode(this);
    }

    public List<INode> getChildren() {
        return Arrays.asList();
    }
}
