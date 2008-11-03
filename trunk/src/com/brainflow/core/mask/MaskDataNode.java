package com.brainflow.core.mask;

import com.brainflow.image.data.*;
import com.brainflow.image.operations.BinaryOperand;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.image.operations.Operations;
import com.brainflow.image.operations.BooleanOperation;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 12, 2008
 * Time: 9:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskDataNode extends AbstractNode implements LeafNode, LeafVisitable {


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

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        return leaf.visitMask(this, op );
    }

    public LeafNode visitImage(ImageDataNode left, BinaryOperand op) {
        BinaryOperation bop = Operations.lookup(op);
        if (!(bop instanceof BooleanOperation)) {
            throw new SemanticError("illegal operation : " + op);
        }

        BooleanOperation boolop = (BooleanOperation) bop;

        IMaskedData3D mdat = new MaskedData3D((IImageData3D) left.evaluate(), new MaskPredicate() {
            public boolean mask(double value) {
                return value > 0;
            }
        });

        BooleanMaskNode3D data = new BooleanMaskNode3D(mdat, mask, boolop);
        return new MaskDataNode(data);

    }

    public LeafNode visitConstant(ConstantNode left, BinaryOperand op) {
        BinaryOperation bop = Operations.lookup(op);

        if (!(bop instanceof BooleanOperation)) {
            throw new SemanticError("illegal operation : " + op);
        }

        BooleanOperation boolop = (BooleanOperation) bop;

        IImageData3D cdat = ImageData.createConstantData(left.evaluate().doubleValue(), mask.getImageSpace());

        IMaskedData3D mdat = new MaskedData3D(cdat, new MaskPredicate() {
            public boolean mask(double value) {
                return value > 0;
            }
        });

        BooleanMaskNode3D data = new BooleanMaskNode3D(mdat, mask, boolop);
        return new MaskDataNode(data);

    }

    public LeafNode visitMask(MaskDataNode left, BinaryOperand op) {
        BinaryOperation bop = Operations.lookup(op);

        if (!(bop instanceof BooleanOperation)) {
            throw new SemanticError("illegal operation : " + op);
        }

        BooleanOperation boolop = (BooleanOperation) bop;


        BooleanMaskNode3D data = new BooleanMaskNode3D(left.getData(), mask, boolop);
        return new MaskDataNode(data);
    }

    public int depth() {
        return 0;
    }

    public String toString() {
        //return mask.toString() + " (" + mask.cardinality() + ")";

        return mask.toString();
    }
}
