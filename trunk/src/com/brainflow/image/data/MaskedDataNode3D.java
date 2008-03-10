package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.utils.Index3D;
import com.brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 19, 2007
 * Time: 1:16:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskedDataNode3D implements IMaskedData3D {

    private IMaskedData3D left;

    private IMaskedData3D right;

    private BinaryOperation operation = BinaryOperation.AND;


    private String imageLabel = "";

    public MaskedDataNode3D(IMaskedData3D left, IMaskedData3D right) {
        this.left = left;
        this.right = right;

    }

    public MaskedDataNode3D(IMaskedData3D left, IMaskedData3D right, BinaryOperation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;

    }



    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        return operation.compute((int) left.getRealValue(realx, realy, realz, interp), (int) right.getRealValue(realx, realy, realz, interp));
    }

    public int isTrue(int index) {
        return operation.compute(left.isTrue(index), right.isTrue(index));
    }

    public int isTrue(int x, int y, int z) {
        return operation.compute(left.isTrue(x, y, z), right.isTrue(x, y, z));
    }

    public double getValue(int index) {
        return operation.compute(left.isTrue(index), right.isTrue(index));
    }

    public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return operation.compute((int) left.getValue(x, y, z, interp), (int) right.getRealValue(x, y, z, interp));
    }

    public double getValue(int x, int y, int z) {
        return operation.compute(left.isTrue(x, y, z), right.isTrue(x, y, z));
    }

    public int indexOf(int x, int y, int z) {
        return left.indexOf(x, y, z);
    }


    public void setValue(int idx, double val) {
        throw new RuntimeException("illegal operation");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setValue(int x, int y, int z, double val) {
        throw new RuntimeException("illegal operation");
    }

    public Index3D indexToGrid(int idx, Index3D voxel) {
        return left.indexToGrid(idx, voxel);
    }

    public Anatomy getAnatomy() {
        return left.getAnatomy();
    }

    public DataType getDataType() {
        return left.getDataType();
    }

    public int getDimension(Axis axisNum) {
        return left.getDimension(axisNum);
    }


    public ImageInfo getImageInfo() {
        return left.getImageInfo();
    }

    public String getImageLabel() {
        return imageLabel;
    }

    public ImageSpace3D getImageSpace() {
        return left.getImageSpace();
    }

    public double maxValue() {
        return Math.max(left.maxValue(), right.maxValue());
    }

    public double minValue() {
        return Math.min(left.minValue(), right.minValue());

    }

    public int numElements() {
        return left.numElements();
    }

    public ImageIterator iterator() {
        return new MaskedDataNodeIterator();
    }

    

    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    public int cardinality() {
        MaskedDataNodeIterator iter = new MaskedDataNodeIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (iter.next() > 0) {
                count++;
            }
        }

        return count;
    }


    class MaskedDataNodeIterator implements ImageIterator {

        ImageIterator iter;

        public MaskedDataNodeIterator() {
            iter = left.iterator();
        }

        public double next() {
            advance();
            return operation.compute(left.isTrue(iter.index()), right.isTrue(iter.index()));

        }

        public void advance() {
            iter.advance();
        }

        public double previous() {
            iter.jump(-1);
            return operation.compute(left.isTrue(iter.index()), right.isTrue(iter.index()));

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
