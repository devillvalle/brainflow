package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.Index3D;
import cern.colt.bitvector.BitVector;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 11:53:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryImageData3D extends BinaryImageData implements IImageData3D {


    private int planeSize;
    
    private int dim0;

    public BinaryImageData3D(ImageSpace3D _space) {
        space = _space;
        allocateBits();

        if (getBitVector().size() != getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("BitVector has wrong number of samples: " + bits.size());
        }

        init();
    }

    private BinaryImageData3D(ImageSpace3D _space, BitVector bits) {
        space = _space;
        this.bits = bits;

        if (bits.size() != getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("BitVector has wrong number of samples: " + bits.size());
        }

        init();


    }

    public BinaryImageData3D(MaskedData3D src) {
        space = src.getImageSpace();
        bits = new BitVector(space.getNumSamples());

        ImageIterator iter = src.iterator();
        while (iter.hasNext()) {
            int i = iter.index();
            double val = iter.next();
            if (val > 0) {
                bits.set(i);
            }
        }

    }


    public BinaryImageData3D(ImageSpace3D _space, boolean[] elements) {
        space = _space;
        allocateBits(elements);
        init();
    }

    private void init() {
        //todo these constants shouldf be moved to image space
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);

    }

    public BinaryImageData3D OR(BinaryImageData data) {
        if (data.getImageSpace().getNumSamples() != this.getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("argument must have same dimensions as image for OR operation");
        }

        BitVector ret = bits.copy();
        ret.or(data.getBitVector());
        return new BinaryImageData3D((ImageSpace3D) getImageSpace(), ret);
    }

    public BinaryImageData3D AND(BinaryImageData data) {
        if (data.getImageSpace().getNumSamples() != this.getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("argument must have same dimensions as image for AND operation");
        }


        BitVector ret = bits.copy();
        ret.and(data.getBitVector());
        return new BinaryImageData3D((ImageSpace3D)getImageSpace(), ret);

    }

    public Index3D voxelOf(int idx, Index3D voxel) {
        voxel.setZ(idx / planeSize);
        int remainder = (idx % planeSize);
        voxel.setY(remainder / space.getDimension(Axis.X_AXIS));
        voxel.setX(remainder % space.getDimension(Axis.X_AXIS));

        return voxel;

    }

    public final int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
    }


    public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return interp.interpolate(x, y, z, this);

    }

    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).fractionalSample(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).fractionalSample(realy);
        double z = space.getImageAxis(Axis.Z_AXIS).fractionalSample(realy);
        return interp.interpolate(x, y, z, this);
    }

    public double getValue(int index) {
        return getBitVector().getQuick(index) ? 1.0 : 0.0;

    }

    public final int getInt(int index) {
        return getBitVector().getQuick(index) ? 1 : 0;

    }

    public final double getValue(int x, int y, int z) {
        return getBitVector().getQuick(indexOf(x, y, z)) ? 1.0 : 0.0;
    }

    public final float getFloat(int x, int y, int z) {
       return getBitVector().getQuick(indexOf(x, y, z)) ? 1f : 0f;
    }

    public int getInt(int x, int y, int z) {
        return getBitVector().getQuick(indexOf(x, y, z)) ? 1 : 0;
    }

    public final void setValue(int idx, double val) {
        getBitVector().putQuick(idx, val != 0);

    }

    public final void setValue(int x, int y, int z, double val) {
        getBitVector().putQuick(indexOf(x,y,z), val > 0);

    }

    public final void setFloat(int x, int y, int z, float val) {
        getBitVector().putQuick(indexOf(x,y,z), val > 0);

    }

    public final void setInt(int x, int y, int z, int val) {
        getBitVector().putQuick(indexOf(x,y,z), val > 0);
    }
}