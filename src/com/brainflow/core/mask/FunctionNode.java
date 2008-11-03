package com.brainflow.core.mask;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.operations.BinaryOperand;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 30, 2008
 * Time: 6:19:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionNode extends AbstractNode implements ValueNode<IImageData> {

    private IFunction<IImageData> function;

    public FunctionNode(IFunction<IImageData> function) {
        this.function = function;
    }

    public String getSymbol() {
        return function.getName();
    }

    public boolean isNumber() {
        return false;
    }

    public IImageData evaluate() {
        return function.evaluate();
    }

    public int depth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void apply(TreeWalker walker) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return function.getName();
    }

    public LeafNode visitImage(ImageDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode visitMask(MaskDataNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode visitConstant(ConstantNode other, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LeafNode accept(LeafNode leaf, BinaryOperand op) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
