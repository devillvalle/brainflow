package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
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
public class BivariateMaskedData3D implements IMaskedData3D {

    public final IImageData3D left;

    public final IImageData3D right;

    public final BinaryOperation operation;

    public BivariateMaskedData3D(IImageData3D _left, IImageData3D _right, BinaryOperation _operation) {
        left = _left;
        right = _right;
        operation = _operation;
    }

    public int cardinality() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int isTrue(int index) {
        return operation.compute(left.getValue(index), right.getValue(index));
    }

    public int isTrue(int x, int y, int z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageSpace3D getImageSpace() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getValue(int x, int y, int z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int indexOf(int x, int y, int z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Index3D indexToGrid(int idx, Index3D voxel) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setValue(int x, int y, int z, double val) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Anatomy getAnatomy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DataType getDataType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getDimension(Axis axisNum) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageInfo getImageInfo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getImageLabel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getValue(int index) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageIterator iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double maxValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double minValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int numElements() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setValue(int idx, double val) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
