package com.brainflow.image.data;

import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.Axis;
import com.brainflow.utils.NumberUtils;
import com.brainflow.utils.DataType;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 14, 2007
 * Time: 11:36:20 PM
 * To change this template use File | Settings | File Templates.
 */


public class UByteImageData2D extends AbstractImageData implements IImageData2D {

    private boolean recomputeMax = true;

    private boolean recomputeMin = true;

    private double minValue;

    private double maxValue;

    private byte[] data;


    public UByteImageData2D(IImageSpace _space) {
        if (_space.getNumDimensions() != 2) {
            throw new IllegalArgumentException("ImageSpace must have dimension = 2.");
        }

        space = _space;

        datatype = DataType.BYTE;
        data = new byte[space.getNumSamples()];


    }

    public UByteImageData2D(IImageSpace _space, byte[] _data) {
        if (_space.getNumDimensions() != 2) {
            throw new IllegalArgumentException("ImageSpace must have dimension = 2.");
        }

        space = _space;

        datatype = DataType.BYTE;
        data = _data;


    }

    public double getMaxValue() {
        if (!recomputeMax) {
            return maxValue;
        }

        int sz = data.length;
        double _max = Double.MIN_VALUE;

        for (int i = 0; i < sz; i++) {
            double val = NumberUtils.ubyte(data[i]);
            if (val > _max) {
                _max = val;
            }
        }
        
        maxValue = _max;
        recomputeMax = false;

        return maxValue;
    }

    public double getMinValue() {
        if (!recomputeMin) {
            return minValue;
        }

        int sz = data.length;
        double _min = Double.MAX_VALUE;
        for (int i = 0; i < sz; i++) {
            double val = NumberUtils.ubyte(data[i]);
            if (val < _min) {
                _min = val;

            }
        }
        minValue = _min;
        recomputeMin = false;

        return minValue;

    }


    public ImageIterator iterator() {
        return new Iterator2D();
    }


    public int indexOf(int x, int y) {
        return space.getDimension(Axis.X_AXIS) * y + x;
    }

    public double getValue(double x, double y, InterpolationFunction2D interp) {
        return interp.interpolate(x, y, this);
    }

    public double getRealValue(double realx, double realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).fractionalSample(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).fractionalSample(realy);
        return interp.interpolate(x, y, this);

    }

    public byte[] getByteArray() {
        return data;
    }

    public final void set(int i, byte val) {
        data[i] = val;
    }
    public final byte get(int i) {
        return data[i];
    }

    public final byte get(int x, int y) {
        return data[indexOf(x, y)];
    }

    public double getValue(int x, int y) {
        return NumberUtils.ubyte(data[indexOf(x, y)]);
    }

    public float getFloat(int x, int y) {
        return NumberUtils.ubyte(data[indexOf(x, y)]);
    }

    public int getInt(int x, int y) {
        return NumberUtils.ubyte(data[indexOf(x, y)]);
    }

    public void setValue(int x, int y, double val) {
        data[indexOf(x, y)] = (byte) val;
    }

    public void setFloat(int x, int y, float val) {
        data[indexOf(x, y)] = (byte) val;
    }

    public void setInt(int x, int y, int val) {
        data[indexOf(x, y)] = (byte) val;
    }

    final class Iterator2D implements ImageIterator {

        private int index;

        private int len = space.getNumSamples();
        private int end = len;
        private int begin = 0;

        public Iterator2D() {
            index = 0;
        }

        public void advance() {
            index++;
        }

        public final double next() {
            double dat = NumberUtils.ubyte(data[index]);
            index++;
            return dat;
        }

        public void set(double val) {
            data[index] = (byte) val;
        }

        public double previous() {
            return NumberUtils.ubyte(data[--index]);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double element(int number) {
            index = number;
            return NumberUtils.ubyte(data[number]);
        }

        public double jump(int number) {
            index += number;
            return NumberUtils.ubyte(data[number]);
        }

        public boolean canJump(int number) {
            if ((index + number) < end && (index - number >= begin))
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return NumberUtils.ubyte(data[index]);

        }

        public double nextPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.nextPlane(): only one plane in 2D iterator!");
        }

        public boolean hasNextRow() {
            if ((index + space.getDimension(Axis.X_AXIS)) < len)
                return true;
            return false;
        }

        public boolean hasNextPlane() {
            return false;
        }

        public boolean hasPreviousRow() {
            if ((index - space.getDimension(Axis.X_AXIS)) >= begin)
                return true;
            return false;
        }

        public boolean hasPreviousPlane() {
            return false;
        }

        public double previousRow() {
            index -= space.getDimension(Axis.X_AXIS);
            return NumberUtils.ubyte(data[index]);
        }

        public double previousPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.previousPlane(): only one plane in 2D iterator!");
        }

        public int index() {
            return index;
        }


    }


}
