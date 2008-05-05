package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.utils.DataType;
import com.brainflow.utils.NumberUtils;


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


    public UByteImageData2D(ImageSpace2D _space) {
        super(_space,DataType.BYTE);
       
        data = new byte[space.getNumSamples()];


    }

    public UByteImageData2D(ImageSpace2D space, byte[] _data) {
        super(space, DataType.BYTE);
        if (_data.length != space.getNumSamples()) {
            throw new IllegalArgumentException("supplied data array has incorrect length");
        }


        data = _data;


    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D)space;
         
    }

    



    public double maxValue() {
        if (!recomputeMax) {
            return maxValue;
        }

        int sz = data.length;
        double _max = -Double.MAX_VALUE;

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

    public double minValue() {
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

    public double value(double x, double y, InterpolationFunction2D interp) {
        return interp.interpolate(x, y, this);
    }

    public double worldValue(double realx, double realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).fractionalSample(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).fractionalSample(realy);
        return interp.interpolate(x, y, this);

    }

    public double value(int index) {
        return data[index];
    }

    public void setValue(int idx, double val) {
        data[idx] = (byte)val;
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

    public double value(int x, int y) {
        return NumberUtils.ubyte(data[indexOf(x, y)]);
    }



    public void setValue(int x, int y, double val) {
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
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.nextPlane(): only zero plane in 2D iterator!");
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
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.previousPlane(): only zero plane in 2D iterator!");
        }

        public int index() {
            return index;
        }


    }


}
