package com.brainflow.core.mask;

import test.Testable;
import com.brainflow.image.data.*;
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
        System.out.println("in class left: " + node.left().getClass());
        System.out.println("in class right: " + node.right().getClass());
        System.out.println("in depth : " + node.depth());

        if (node.left() instanceof LeafNode && node.right() instanceof LeafNode) {
            LeafNode left = (LeafNode) node.left();
            LeafNode right = (LeafNode) node.right();

            LeafNode mnode = left.accept(right, node.getOp());
            node.replaceBy(mnode);
            //left.reduce(right)

        }


        /*if ((node.left() instanceof ImageDataNode) && (node.right() instanceof ImageDataNode)) {
          ImageDataNode lnode = (ImageDataNode) node.left();
          ImageDataNode rnode = (ImageDataNode) node.right();

          BivariateMaskNode3D data = new BivariateMaskNode3D((IImageData3D) lnode.getData(), (IImageData3D) rnode.getData(), Operations.lookup(node.getOp()));
          node.replaceBy(new MaskDataNode(data));
      } else if ((node.left() instanceof ImageDataNode) && (node.right() instanceof MaskDataNode)) {
          ImageDataNode lnode = (ImageDataNode) node.left();
          MaskDataNode rnode = (MaskDataNode) node.right();

          BinaryOperation op = Operations.lookup(node.getOp());

          if (!(op instanceof BooleanOperation)) {
              throw new SemanticError("illegal operation : " + op);
          }

          BooleanOperation bop = (BooleanOperation) op;
          IMaskedData3D mdat = new MaskedData3D((IImageData3D)lnode.getData(), new MaskPredicate() {
              public boolean mask(double value) {
                  return value > 0;
              }
          });

          BooleanMaskNode3D data = new BooleanMaskNode3D(mdat, rnode.getData(), bop);
          node.replaceBy(new MaskDataNode(data));

      } else if ((node.left() instanceof MaskDataNode) && (node.right() instanceof ImageDataNode)) {

          MaskDataNode lnode = (MaskDataNode) node.right();
          ImageDataNode rnode = (ImageDataNode) node.left();


          BinaryOperation op = Operations.lookup(node.getOp());

          if (!(op instanceof BooleanOperation)) {
              throw new SemanticError("illegal operation : " + op);
          }

          BooleanOperation bop = (BooleanOperation) op;
          IMaskedData3D mdat = new MaskedData3D((IImageData3D)rnode.getData(), new MaskPredicate() {
              public boolean mask(double value) {
                  return value > 0;
              }
          });

          BooleanMaskNode3D data = new BooleanMaskNode3D(lnode.getData(), mdat, bop);
          node.replaceBy(new MaskDataNode(data));

      } else if ((node.left() instanceof MaskDataNode) && (node.right() instanceof MaskDataNode)) {
          MaskDataNode lnode = (MaskDataNode) node.left();
          MaskDataNode rnode = (MaskDataNode) node.right();

          BinaryOperation op = Operations.lookup(node.getOp());

          if (!(op instanceof BooleanOperation)) {
              throw new SemanticError("illegal operation : " + op);
          }

          BooleanOperation bop = (BooleanOperation) op;

          BooleanMaskNode3D data = new BooleanMaskNode3D(lnode.getData(), rnode.getData(), bop);
          node.replaceBy(new MaskDataNode(data));
      }  */

    }

    public void outComparison(ComparisonNode node) {
        System.out.println("out class left: " + node.left().getClass());
        System.out.println("out class right: " + node.right().getClass());
        System.out.println("out depth : " + node.depth());

        /*if (node.left() instanceof MaskDataNode && node.right() instanceof MaskDataNode) {
            MaskDataNode left = (MaskDataNode) node.left();
            MaskDataNode right = (MaskDataNode) node.right();

            LeafNode mnode = left.accept(right, node.getOp());
            node.replaceBy(mnode);

        }   */

    }

    @Override
    public INode outStart(INode rootNode) {

        if (rootNode instanceof ComparisonNode) {
            ComparisonNode node= (ComparisonNode)rootNode;

            if (node.left() instanceof LeafNode && node.right() instanceof LeafNode) {
                LeafNode left = (LeafNode) node.left();
                LeafNode right = (LeafNode) node.right();

                LeafNode mnode = left.accept(right, node.getOp());

                rootNode = mnode;
            }

        }

        return rootNode;

    }
}
