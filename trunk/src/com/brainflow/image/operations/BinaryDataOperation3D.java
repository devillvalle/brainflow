package com.brainflow.image.operations;

import com.brainflow.image.data.BinaryImageData2D;
import com.brainflow.image.data.MaskedData2D;
import com.brainflow.image.data.BinaryImageData3D;
import com.brainflow.image.data.MaskedData3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 10, 2007
 * Time: 1:04:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryDataOperation3D implements IBinaryOperation3D {

    private BinaryDataOperation3D left;

    private BinaryDataOperation3D right;

    private BinaryOperation operation;

    private BinaryImageData3D result;

    public BinaryDataOperation3D(BinaryDataOperation3D left, BinaryDataOperation3D right) {
        this.left = left;
        this.right = right;
    }

    public BinaryDataOperation3D(BinaryImageData3D left) {
        this.left = new BinaryDataOperation3D(left);
    }

    public BinaryDataOperation3D(MaskedData3D left) {
        this.left = new BinaryDataOperation3D(new BinaryImageData3D(left));
    }

    public BinaryDataOperation3D(MaskedData3D left, MaskedData3D right, BinaryOperation operation) {
        this.left = new BinaryDataOperation3D(new BinaryImageData3D(left));
        this.right = new BinaryDataOperation3D(new BinaryImageData3D(right));
        this.operation = operation;

    }

    public BinaryDataOperation3D(BinaryImageData3D left, BinaryImageData3D right, BinaryOperation operation) {
        this.left = new BinaryDataOperation3D(left);
        this.right = new BinaryDataOperation3D(right);
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

    public BinaryImageData3D compute() {
        if (right == null) {
            return left.compute();
        }

        if (result != null) {
            return result;
        }

        switch(operation) {
            case OR :
                result =  left.compute().OR(right.compute());
                break;
            case AND :
                result = left.compute().AND(right.compute());
                break;
            default :
                throw new AssertionError();

        }

        return result;
    }
}