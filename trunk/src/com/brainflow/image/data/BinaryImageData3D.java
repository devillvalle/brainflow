package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.math.Index3D;
import cern.colt.bitvector.BitVector;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 11:53:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryImageData3D extends BinaryImageData implements IMaskedData3D {


    private int planeSize;

    private int dim0;

    public BinaryImageData3D(IImageSpace3D _space) {
        super(_space);

        init();
    }



    private BinaryImageData3D(IImageSpace3D space, BitVector bits) {
        super(space, bits);
        init();
    }

    public ImageSpace3D getImageSpace() {
        return (ImageSpace3D)space;
    }

    public BinaryImageData3D(IMaskedData3D src) {
        super(src.getImageSpace());

        
        BitVector bits = getBitVector();
        ImageIterator iter = src.iterator();

        while (iter.hasNext()) {
            int i = iter.index();
            double val = iter.next();
            if (val > 0) {
                bits.set(i);
            }
        }

        init();

    }


    public BinaryImageData3D(ImageSpace3D space, boolean[] elements) {
        super(space, elements);

        init();
    }

    private void init() {
        //todo these constants should be moved to image space
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);

    }

    public BinaryImageData3D OR(BinaryImageData data) {
        if (data.getImageSpace().getNumSamples() != this.getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("argument must have same dimensions as image for OR operation");
        }

        BitVector ret = getBitVector().copy();
        ret.or(data.getBitVector());
        return new BinaryImageData3D(getImageSpace(), ret);
    }

    public BinaryImageData3D AND(BinaryImageData data) {
        if (data.getImageSpace().getNumSamples() != this.getImageSpace().getNumSamples()) {
            throw new IllegalArgumentException("argument must have same dimensions as image for AND operation");
        }


        BitVector ret = getBitVector().copy();
        ret.and(data.getBitVector());
        return new BinaryImageData3D(getImageSpace(), ret);

    }

    public Index3D indexToGrid(int idx) {
       return getImageSpace().indexToGrid(idx);

    }

    public final int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
    }


    public double value(float x, float y, float z, InterpolationFunction3D interp) {
        return interp.interpolate(x, y, z, this);

    }

    public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        double z = space.getImageAxis(Axis.Z_AXIS).gridPosition(realz);
        return interp.interpolate(x, y, z, this);
    }

    public double value(int index) {
        return getBitVector().getQuick(index) ? 1.0 : 0.0;

    }

    public final int getInt(int index) {
        return getBitVector().getQuick(index) ? 1 : 0;

    }

    public final double value(int x, int y, int z) {
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
        getBitVector().putQuick(indexOf(x, y, z), val > 0);

    }

    public final void setFloat(int x, int y, int z, float val) {
        getBitVector().putQuick(indexOf(x, y, z), val > 0);
    }

    public final void setInt(int x, int y, int z, int val) {
        getBitVector().putQuick(indexOf(x, y, z), val > 0);
    }

    public final boolean isTrue(int x, int y, int z) {
        return getBitVector().getQuick(indexOf(x, y, z));
    }

    public final boolean isTrue(int index) {
        return getBitVector().getQuick(index);

    }

    public DataWriter3D createWriter(boolean clear) {
        throw new UnsupportedOperationException();
    }
}
