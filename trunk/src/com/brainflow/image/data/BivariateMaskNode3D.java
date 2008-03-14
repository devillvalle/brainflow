package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.operations.Operations;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.utils.Index3D;
import com.brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 11, 2008
 * Time: 8:45:00 AM
 * To change this template use File | Settings | File Templates.
 */

public class BivariateMaskNode3D implements IMaskedData3D {

    public final IImageData3D left;

    public final IImageData3D right;

    public final BinaryOperation operation;

    public BivariateMaskNode3D(IImageData3D _left, IImageData3D _right, BinaryOperation _operation) {
        left = _left;
        right = _right;
        operation = _operation;
    }

    public int cardinality() {
        BivariateMaskedDataNodeIterator iter = new BivariateMaskedDataNodeIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (iter.next() > 0) {
                count++;
            }
        }

        return count;
    }

    public boolean isTrue(int index) {
        return operation.isTrue(left.getValue(index), right.getValue(index));
    }

    public boolean isTrue(int x, int y, int z) {
        return operation.isTrue(left.getValue(x, y, z), right.getValue(x, y, z));
    }

    public ImageSpace3D getImageSpace() {
        return left.getImageSpace();
    }

    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        return operation.isTrue(left.getRealValue(realx, realy, realz, interp), right.getRealValue(realx, realy, realz, interp)) ? 1 : 0;
    }

    public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return operation.isTrue(left.getValue(x, y, z, interp), right.getValue(x, y, z, interp)) ? 1 : 0;
    }

    public double getValue(int x, int y, int z) {
        return operation.isTrue(left.getValue(x, y, z), right.getValue(x, y, z)) ? 1 : 0;
    }

    public int indexOf(int x, int y, int z) {
        return left.indexOf(x, y, z);
    }

    public Index3D indexToGrid(int idx, Index3D voxel) {
        return left.indexToGrid(idx, voxel);
    }

    public void setValue(int x, int y, int z, double val) {
        throw new UnsupportedOperationException();
    }

    public Anatomy getAnatomy() {
        return left.getAnatomy();
    }

    public DataType getDataType() {
        return DataType.INTEGER;
    }

    public int getDimension(Axis axisNum) {
        return left.getDimension(axisNum);
    }

    public ImageInfo getImageInfo() {
        return new ImageInfo(this);
    }

    public String getImageLabel() {
        return left.getImageLabel() + ":" + right.getImageLabel();
    }

    public double getValue(int index) {
        return operation.isTrue(left.getValue(index), right.getValue(index)) ? 1 : 0;
    }

    public ImageIterator iterator() {
        return new BivariateMaskedDataNodeIterator();
    }

    public double maxValue() {
        //todo not strictly correct
        return 1;
    }

    public double minValue() {
        //todo not strictly correct
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int numElements() {
        return left.numElements();
    }

    public void setValue(int idx, double val) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return left.getImageLabel() + " " + operation + " " + right.getImageLabel();
    }

    class BivariateMaskedDataNodeIterator implements ImageIterator {

        ImageIterator iter;

        public BivariateMaskedDataNodeIterator() {
            iter = left.iterator();
        }

        public double next() {
            advance();
            return getValue(iter.index());

        }

        public void advance() {
            iter.advance();
        }

        public double previous() {
            iter.jump(-1);
            return getValue(iter.index());

        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public double jump(int number) {
            return iter.jump(number);
        }

        public boolean canJump(int number) {
            return iter.canJump(number);
        }

        public double nextRow() {
            return iter.nextRow();
        }

        public double nextPlane() {
            return iter.nextPlane();
        }

        public boolean hasNextRow() {
            return iter.hasNextRow();
        }

        public boolean hasNextPlane() {
            return iter.hasNextPlane();
        }

        public boolean hasPreviousRow() {
            return iter.hasPreviousRow();
        }

        public boolean hasPreviousPlane() {
            return iter.hasPreviousPlane();
        }

        public double previousRow() {
            return iter.previousRow();
        }

        public double previousPlane() {
            return iter.previousPlane();
        }

        public void set(double val) {
            throw new UnsupportedOperationException();
        }

        public int index() {
            return iter.index();
        }
    }
}
