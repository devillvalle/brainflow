package com.brainflow.image.operations;

import com.brainflow.image.data.BinaryImageData2D;
import com.brainflow.image.data.MaskedData2D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 10, 2007
 * Time: 1:04:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryDataOperation2D implements IBinaryOperation2D {

    private BinaryDataOperation2D left;

    private BinaryDataOperation2D right;

    private BinaryOperation operation;

    private BinaryImageData2D result;

    public BinaryDataOperation2D(BinaryDataOperation2D left, BinaryDataOperation2D right) {
        this.left = left;
        this.right = right;
    }

    public BinaryDataOperation2D(BinaryImageData2D left) {
        this.left = new BinaryDataOperation2D(left);
    }

    public BinaryDataOperation2D(MaskedData2D left) {
        this.left = new BinaryDataOperation2D(new BinaryImageData2D(left));
    }

    public BinaryDataOperation2D(MaskedData2D left, MaskedData2D right, BinaryOperation operation) {
        this.left = new BinaryDataOperation2D(new BinaryImageData2D(left));
        this.right = new BinaryDataOperation2D(new BinaryImageData2D(right));
        this.operation = operation;

    }

    public BinaryDataOperation2D(BinaryImageData2D left, BinaryImageData2D right, BinaryOperation operation) {
        this.left = new BinaryDataOperation2D(left);
        this.right = new BinaryDataOperation2D(right);
        this.operation = operation;
    }

    public void clear() {
        result = null;
        if (left != null) {
            left.clear();
        }
        if (right != null) {
            right.clear();
        }
    }

    public BinaryImageData2D compute() {
        if (right == null) {
            return left.compute();
        }

        if (result != null) {
            return result;
        } else {

            if (operation == BinaryOperation.OR) {
                result = left.compute().OR(right.compute());
            } else if (operation == BinaryOperation.AND) {
                result = left.compute().AND(right.compute());
            } else {
                throw new AssertionError();

            }
        }

        return result;
    }
}
