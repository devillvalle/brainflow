package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.io.BrainIO;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.math.ArrayUtils;
import com.brainflow.utils.DataType;
import com.brainflow.utils.Index3D;
import com.brainflow.application.TestUtils;


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


    private int planeSize;

    private int dim0;

    public BasicImageData3D(BasicImageData3D src) {
        super((ImageSpace3D) src.getImageSpace(), src.getDataType());
        fillBuffer(src.storage, space.getNumSamples());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
    }


    public BasicImageData3D(ImageSpace3D space, DataType _type) {
        super(space, _type);
        data = allocateBuffer(space.getNumSamples());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);

    }

    public BasicImageData3D(ImageSpace3D space, Object array) {
        super(space,establishDataType(array));
        storage = array;

        data = allocateBuffer(space.getNumSamples());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);

    }



    public double maxValue() {
        if (!recomputeMax) {
            return maxValue;
        }

        int sz = data.getSize();
        double _max = -Double.MAX_VALUE;
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

    public final double minValue() {
        if (!recomputeMin) {
            return minValue;
        }

        int sz = data.getSize();
        double _min = Double.MAX_VALUE;
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
        // todo is this  what we really want to do?
        return new ImageInfo(this);
    }

    public ImageSpace3D getImageSpace() {
        return (ImageSpace3D)space;

    }

    public final Index3D indexToGrid(int idx, Index3D voxel) {
        voxel.setZ(idx / planeSize);
        int remainder = (idx % planeSize);
        voxel.setY(remainder / space.getDimension(Axis.X_AXIS));
        voxel.setX(remainder % space.getDimension(Axis.X_AXIS));

        return voxel;

    }

    public final int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
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


    public final double getValue(int x, int y, int z) {
        return data.getElemDouble(indexOf(x, y, z));
    }




    public final void setValue(int idx, double val) {
        data.setElemDouble(idx, val);
    }

    public final void setValue(int x, int y, int z, double val) {
        data.setElemDouble(indexOf(x, y, z), val);
    }


    public byte[] toBytes() {
        byte[] idat = ArrayUtils.scaleToBytes(storage, minValue(), maxValue(), 255);
        return idat;
    }


    public ImageIterator iterator() {
        return new Iterator3D(this);
    }

    public static class Iterator3D implements ImageIterator {

        private int index;

        private IImageData3D data;

        private ImageSpace3D space;

        private int len;
        private int end;
        private int begin = 0;
        private int planeSize;

        public Iterator3D(IImageData3D _data) {
            data = _data;
            space = data.getImageSpace();
            index = 0;
            planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
            len = space.getNumSamples();
            end = len;
        }

        public final double next() {
            double dat = data.getValue(index);
            index++;
            return dat;
        }

        public final void advance() {
            index++;
        }

        public void set(double val) {
            data.setValue(index, val);
        }

        public double previous() {
            return data.getValue(--index);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double jump(int number) {
            index += number;
            return data.getValue(number);
        }

        public boolean canJump(int number) {
            //todo what on earth?
            if ( ((index + number) < end) && ((index - number) >= begin) )
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return data.getValue(index);
        }

        public double nextPlane() {
            index += planeSize;
            return data.getValue(index);
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
            return data.getValue(index);
        }

        public double previousPlane() {
            index -= planeSize;
            return data.getValue(index);
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
            IImageData data = TestUtils.quickDataSource("mean-BRB-EPI-001.nii").load();
            Index3D vox = new Index3D();

            System.out.println("class : " + data.getClass());
            System.out.println("double min : " + Double.MIN_VALUE);
            System.out.println("double -max : " + -Double.MAX_VALUE);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
