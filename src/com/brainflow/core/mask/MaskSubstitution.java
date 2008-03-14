package com.brainflow.core.mask;

import test.Testable;
import com.brainflow.image.data.BivariateMaskNode3D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.BooleanMaskNode3D;
import com.brainflow.image.operations.Operations;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.image.operations.BooleanOperation;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 11, 2008
 * Time: 8:24:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskSubstitution extends AnalysisAdapter {


    


    @Override
   @Testable
    public void inComparison(ComparisonNode node) {
       System.out.println("in : " + node);
        if ( (node.left() instanceof ImageDataNode) && (node.right() instanceof ImageDataNode) ) {
            ImageDataNode lnode = (ImageDataNode)node.left();
            ImageDataNode rnode = (ImageDataNode)node.right();

            BivariateMaskNode3D data = new BivariateMaskNode3D((IImageData3D)lnode.getData(), (IImageData3D)rnode.getData(), Operations.lookup(node.getOp()));
            node.replaceBy(new MaskDataNode(data));
        } else if ((node.left() instanceof MaskDataNode) && (node.right() instanceof MaskDataNode) ) {
            MaskDataNode lnode = (MaskDataNode)node.left();
            MaskDataNode rnode = (MaskDataNode)node.right();

            BinaryOperation op = Operations.lookup(node.getOp());
            if (!(op instanceof BooleanOperation)) {
                throw new SemanticError("illegal operation : " + op);
            }

            BooleanOperation bop = (BooleanOperation)op;

            BooleanMaskNode3D  data = new BooleanMaskNode3D(lnode.getData(), rnode.getData(), bop);
            node.replaceBy(new MaskDataNode(data));
        } else {
            System.out.println("NEITHER ********************************************************");
        }

    }

    public void outComparison(ComparisonNode node) {
        System.out.println("out : " + node);
    }

    public INode outStart(INode rootNode) {
        return rootNode;
    }
}
