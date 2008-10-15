package com.brainflow.core.mask;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.BivariateMaskNode3D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.operations.BinaryOperand;
import com.brainflow.image.operations.Operations;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 7:06:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDataNode extends LeafNode implements LeafVisitable {

    private IImageData data;

    public ImageDataNode(IImageData data) {
        this.data = data;
    }

    public IImageData getData() {
        return data;
    }

    public int depth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void accept(LeafNode leaf, BinaryOperand op) {
        leaf.visitImage(this, op);
    }

    public AbstractNode visitImage(ImageDataNode other, BinaryOperand op) {
        // other.visitImageDataNode(this)
         BivariateMaskNode3D bdata = new BivariateMaskNode3D((IImageData3D) getData(), (IImageData3D) other.getData(), Operations.lookup(op));
         return new MaskDataNode(bdata);
    }

    public AbstractNode visitMask(MaskDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AbstractNode visitConstant(ConstantNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void apply(TreeWalker walker) {
        walker.caseImageDataNode(this);
    }

    @Override
    public String toString() {
        return data.getImageLabel();
    }
}
