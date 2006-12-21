package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.io.AnalyzeIO;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.ArrayUtils;
import com.brainflow.utils.DataType;
import com.brainflow.utils.Index3D;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BasicImageData3D extends BasicImageData implements IImageData3D {


    int planeSize;
    private int[] dimArray = null;

    public BasicImageData3D(BasicImageData3D src) {
        space = new ImageSpace3D((ImageSpace3D) src.getImageSpace());
        datatype = src.getDataType();
        fillBuffer(src.storage, space.getNumSamples());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dimArray = space.getDimensionVector();
    }


    public BasicImageData3D(IImageSpace _space, DataType _type) {
        assert _space.getNumDimensions() == 3;
        space = _space;
        datatype = _type;
        data = allocateBuffer(space.getNumSamples());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dimArray = space.getDimensionVector();

    }

    public BasicImageData3D(IImageSpace _space, Object array) {
        assert _space.getNumDimensions() == 3;
        space = _space;
        storage = array;
        establishDataType(storage);
        data = allocateBuffer(space.getNumSamples());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dimArray = space.getDimensionVector();
    }

    public int getDimensionSize(int dimnum) {
        return dimArray[dimnum];
    }

    public double getMaxValue() {
        if (!recomputeMax) {
            return maxValue;
        }

        int sz = data.getSize();
        double _max = Double.MIN_VALUE;
        for (int i = 0; i < sz; i++) {
            double val = data.getElemDouble(i);
            if (val > _max) {
                _max = val;

            }
        }


        maxValue = _max;
        recomputeMax = false;

        return maxValue;

    }

    public final double getMinValue() {
        if (!recomputeMin) {
            return minValue;
        }

        int sz = data.getSize();
        double _min = Double.MIN_VALUE;
        for (int i = 0; i < sz; i++) {
            double val = data.getElemDouble(i);
            if (val < _min) {
                _min = val;

            }
        }


        minValue = _min;
        recomputeMin = false;

        return minValue;

    }

    public ImageInfo getImageInfo() {
        return new ImageInfo(this);
    }

    public final Index3D voxelOf(int idx, Index3D voxel) {
        voxel.setZ(idx / planeSize);
        int remainder = (idx % planeSize);
        voxel.setY(remainder / space.getDimension(Axis.X_AXIS));
        voxel.setX(remainder % space.getDimension(Axis.X_AXIS));

        return voxel;

    }

    public final int indexOf(int x, int y, int z) {
        return (z * planeSize) + dimArray[0] * y + x;
    }

    public BasicImageData2D getOrthogonalCut(int dimIndex, int orientation) {
        return null;
    }

    public final double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return interp.interpolate(x, y, z, this);
    }


    public final double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).fractionalSample(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).fractionalSample(realy);
        double z = space.getImageAxis(Axis.Z_AXIS).fractionalSample(realz);

        if (x < 0 | y < 0 | z < 0) return 0;


        return interp.interpolate(x, y, z, this);
    }

    public final double getValue(int index) {
        return data.getElemDouble(index);
    }

    public final int getInt(int index) {
        return data.getElem(index);
    }

    public final double getValue(int x, int y, int z) {
        return data.getElemDouble(indexOf(x, y, z));
    }

    public final float getFloat(int x, int y, int z) {
        return data.getElemFloat(indexOf(x, y, z));
    }

    public final int getInt(int x, int y, int z) {
        return data.getElem(indexOf(x, y, z));
    }


    public final void setValue(int idx, double val) {
        data.setElemDouble(idx, val);
    }

    public final void setValue(int x, int y, int z, double val) {
        data.setElemDouble(indexOf(x, y, z), val);
    }

    public final void setFloat(int x, int y, int z, float val) {
        data.setElemFloat(indexOf(x, y, z), val);
    }

    public final void setInt(int x, int y, int z, int val) {
        data.setElem(indexOf(x, y, z), val);
    }

    public byte[] toBytes() {
        byte[] idat = ArrayUtils.scaleToBytes(storage, getMinValue(), getMaxValue(), 255);
        return idat;
    }


    public ImageIterator iterator() {
        return new Iterator3D();
    }

    final class Iterator3D implements ImageIterator {

        private int index;

        private int len = space.getNumSamples();
        private int end = len;
        private int begin = 0;
        private int planeSize;

        public Iterator3D() {
            index = 0;
            planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        }

        public final double next() {
            double dat = data.getElemDouble(index);
            index++;
            return dat;
        }

        public final void advance() {
            index++;
        }

        public void set(double val) {
            data.setElemDouble(index, val);
        }

        public double previous() {
            return data.getElemDouble(--index);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double jump(int number) {
            index += number;
            return data.getElemDouble(number);
        }

        public boolean canJump(int number) {
            int tmp = index + number;
            if ((index + number) < end && (index - number >= begin))
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return data.getElemDouble(index);
        }

        public double nextPlane() {
            index += planeSize;
            return data.getElemDouble(index);
        }

        public boolean hasNextRow() {
            if ((index + space.getDimension(Axis.X_AXIS)) < len)
                return true;
            return false;
        }

        public boolean hasNextPlane() {
            if ((index + planeSize) < len)
                return true;
            return false;
        }

        public boolean hasPreviousRow() {
            if ((index - space.getDimension(Axis.X_AXIS)) >= begin)
                return true;
            return false;
        }

        public boolean hasPreviousPlane() {
            if ((index - planeSize) >= begin)
                return true;
            return false;
        }

        public double previousRow() {
            index -= space.getDimension(Axis.X_AXIS);
            return data.getElemDouble(index);
        }

        public double previousPlane() {
            index -= planeSize;
            return data.getElemDouble(index);
        }

        public int index() {
            return index;
        }


    }

    public String toString() {
        return this.getImageLabel();
    }


    public static void main(String[] args) {
        try {
            BasicImageData3D data1 = (BasicImageData3D) AnalyzeIO.readAnalyzeImage("c:/brains/icbm452_atlas_warp5_sinc");
            Index3D vox = new Index3D();

            int idx = data1.indexOf(0, 16, 72);
            data1.voxelOf(idx, vox);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
